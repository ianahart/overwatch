package com.hart.overwatch.stripepaymentintent;

import java.io.IOException;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.email.EmailQueueService;
import com.hart.overwatch.email.request.EmailRequest;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentSearchResultDto;
import com.hart.overwatch.stripepaymentintent.projection.StripePaymentIntentApplicationFeeProjection;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.user.User;
import com.hart.overwatch.util.MyUtil;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.Document;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class StripePaymentIntentService {

    private final StripePaymentIntentRepository stripePaymentIntentRepository;

    private final EmailQueueService emailQueueService;

    private final PaginationService paginationService;


    public StripePaymentIntentService(StripePaymentIntentRepository stripePaymentIntentRepository,
            EmailQueueService emailQueueService, PaginationService paginationService) {
        this.stripePaymentIntentRepository = stripePaymentIntentRepository;
        this.emailQueueService = emailQueueService;
        this.paginationService = paginationService;
    }

    public StripePaymentIntent getStripePaymentIntentById(Long stripePaymentIntentId) {
        return stripePaymentIntentRepository.findById(stripePaymentIntentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Could not find stripe payment intent with the id %d",
                                stripePaymentIntentId)));
    }

    public void createStripePaymentIntent(User user, User reviewer, PaymentIntent paymentIntent,
            Long applicationFee) {
        StripePaymentIntent stripePaymentIntent = new StripePaymentIntent();
        stripePaymentIntent.setUser(user);
        stripePaymentIntent.setReviewer(reviewer);
        stripePaymentIntent.setCurrency(paymentIntent.getCurrency());
        stripePaymentIntent.setPaymentIntentId(paymentIntent.getId());
        stripePaymentIntent.setAmount(paymentIntent.getAmount());
        stripePaymentIntent.setApplicationFee(applicationFee);
        stripePaymentIntent.setStatus(PaymentIntentStatus.PAID);
        stripePaymentIntent.setClientSecret(paymentIntent.getClientSecret());
        stripePaymentIntent.setDescription(paymentIntent.getDescription());

        stripePaymentIntentRepository.save(stripePaymentIntent);
        String reviewerText =
                String.format("%s has paid you $%s %s for reviewing their code!\n package:%s",
                        user.getFullName(), convertCentsToDollars(paymentIntent.getAmount()),
                        paymentIntent.getCurrency(), paymentIntent.getDescription());
        String userText = String.format("You paid %s $%s %s for reviewing your code!\n package: %s",
                reviewer.getFullName(), convertCentsToDollars(paymentIntent.getAmount()),
                paymentIntent.getCurrency(), paymentIntent.getDescription());
        queueEmail(reviewer, "Payment Recieved", reviewerText);
        queueEmail(user, "Payment Notification", userText);
    }

    private String convertCentsToDollars(Long cents) {
        double dollars = cents / 100.0;

        return String.format("$%.2f", dollars);
    }

    private void queueEmail(User recipient, String subject, String body) {
        if (body != null && !body.isEmpty()) {
            EmailRequest emailRequest = new EmailRequest(recipient.getEmail(), subject, body);
            emailQueueService.queueEmail(emailRequest);
        }
    }

    public PaginationDto<StripePaymentIntentDto> getUserStripePaymentIntents(Long userId, int page,
            int pageSize, String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<StripePaymentIntentDto> result = this.stripePaymentIntentRepository
                .getStripePaymentIntentsByUserId(pageable, userId);

        return new PaginationDto<StripePaymentIntentDto>(result.getContent(), result.getNumber(),
                pageSize, result.getTotalPages(), direction, result.getTotalElements());
    }

    public void updateStatus(PaymentIntentStatus status, Long stripePaymentIntentId) {
        StripePaymentIntent stripePaymentIntent = getStripePaymentIntentById(stripePaymentIntentId);

        if (stripePaymentIntent != null) {
            stripePaymentIntent.setStatus(status);
            stripePaymentIntentRepository.save(stripePaymentIntent);
        }
    }

    private Long getTotalRevenue() {
        List<StripePaymentIntentApplicationFeeProjection> applicationFees =
                stripePaymentIntentRepository.findAllBy();
        if (applicationFees.isEmpty()) {
            return 0L;
        }
        return applicationFees.stream()
                .map(StripePaymentIntentApplicationFeeProjection::getApplicationFee)
                .reduce(0L, (a, b) -> a + b);
    }

    public StripePaymentIntentSearchResultDto getAllStripePaymentIntents(String search, int page,
            int pageSize, String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        String searchQuery =
                search.equals("all") ? "" : Jsoup.clean(search.toLowerCase(), Safelist.none());
        Page<FullStripePaymentIntentDto> data = this.stripePaymentIntentRepository
                .getStripePaymentIntentsBySearch(pageable, searchQuery);

        Long totalRevenue = getTotalRevenue();

        PaginationDto<FullStripePaymentIntentDto> result =
                new PaginationDto<FullStripePaymentIntentDto>(data.getContent(), data.getNumber(),
                        pageSize, data.getTotalPages(), direction, data.getTotalElements());
        return new StripePaymentIntentSearchResultDto(result, totalRevenue);
    }


    private void appendTransactionsToPdf(Document document,
            List<FullStripePaymentIntentDto> transactions) {

        Table table = new Table(10);

        table.setKeepTogether(false);
        table.setWidth(UnitValue.createPercentValue(100));
        table.setFixedLayout();

        table.addCell(new Cell().add(new Paragraph(new Text("Id")))
                .setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(new Text("Amount")))
                .setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(new Text("Application Fee")))
                .setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(new Text("Date")))
                .setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(new Text("Currency")))
                .setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(new Text("Status")))
                .setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(new Text("User Name")))
                .setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(new Text("Reviewer Name")))
                .setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(new Text("User Email")))
                .setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(new Text("Reviewer Email")))
                .setTextAlignment(TextAlignment.CENTER));

        for (FullStripePaymentIntentDto transaction : transactions) {
            table.addCell(new Paragraph(String.valueOf(transaction.getId())));
            table.addCell(new Paragraph(convertCentsToDollars(transaction.getAmount())));
            table.addCell(new Paragraph(convertCentsToDollars(transaction.getApplicationFee())));
            table.addCell(new Paragraph(MyUtil.formatDate(transaction.getCreatedAt())));
            table.addCell(new Paragraph(transaction.getCurrency()));
            table.addCell(new Paragraph(transaction.getStatus().toString()));
            table.addCell(new Paragraph(transaction.getUserFullName()));
            table.addCell(new Paragraph(transaction.getReviewerFullName()));
            table.addCell(new Paragraph(transaction.getUserEmail()));
            table.addCell(new Paragraph(transaction.getReviewerEmail()));

            if (table.getNumberOfRows() > 20) {
                document.add(table);
                table = new Table(10);
                table.addCell(new Cell().add(new Paragraph(new Text("Id")))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(new Text("Amount")))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(new Text("Application Fee")))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(new Text("Date")))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(new Text("Currency")))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(new Text("Status")))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(new Text("User Name")))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(new Text("Reviewer Name")))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(new Text("User Email")))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(new Text("Reviewer Email")))
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }
        if (table.getNumberOfRows() > 0) {
            document.add(table);
        }
    }

    public void exportStripePaymentIntentsToPdf(HttpServletResponse response, String search,
            int page, int pageSize, String direction) throws IOException {

        StripePaymentIntentSearchResultDto data =
                getAllStripePaymentIntents(search, page, pageSize, direction);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        document.add(new Paragraph("Transactions Report").setFontSize(16));

        List<FullStripePaymentIntentDto> transactions = data.getResult().getItems();
        appendTransactionsToPdf(document, transactions);

        document.close();
    }

}
