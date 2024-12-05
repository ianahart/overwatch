package com.hart.overwatch.stripepaymentintent;

import java.io.IOException;
import java.nio.file.Path;
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
import com.hart.overwatch.pdf.PdfFileService;
import com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentSearchResultDto;
import com.hart.overwatch.stripepaymentintent.projection.StripePaymentIntentApplicationFeeProjection;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.csv.CsvFileService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.util.MyUtil;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class StripePaymentIntentService {

    private final StripePaymentIntentRepository stripePaymentIntentRepository;

    private final EmailQueueService emailQueueService;

    private final PaginationService paginationService;

    private final CsvFileService csvFileService;

    private final PdfFileService pdfFileService;


    public StripePaymentIntentService(StripePaymentIntentRepository stripePaymentIntentRepository,
            EmailQueueService emailQueueService, PaginationService paginationService,
            CsvFileService csvFileService, PdfFileService pdfFileService) {
        this.stripePaymentIntentRepository = stripePaymentIntentRepository;
        this.emailQueueService = emailQueueService;
        this.paginationService = paginationService;
        this.csvFileService = csvFileService;
        this.pdfFileService = pdfFileService;
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
                String.format("%s has paid you %s %s for reviewing their code!\n package:%s",
                        user.getFullName(), MyUtil.convertCentsToDollars(paymentIntent.getAmount()),
                        paymentIntent.getCurrency(), paymentIntent.getDescription());
        String userText = String.format("You paid %s %s %s for reviewing your code!\n package: %s",
                reviewer.getFullName(), MyUtil.convertCentsToDollars(paymentIntent.getAmount()),
                paymentIntent.getCurrency(), paymentIntent.getDescription());
        queueEmail(reviewer, "Payment Recieved", reviewerText);
        queueEmail(user, "Payment Notification", userText);
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


    public void exportStripePaymentIntentsToPdf(HttpServletResponse response, String search,
            int page, int pageSize, String direction) throws IOException {

        StripePaymentIntentSearchResultDto data =
                getAllStripePaymentIntents(search, page, pageSize, direction);


        List<FullStripePaymentIntentDto> transactions = data.getResult().getItems();
        pdfFileService.generatePdfFile(response, transactions);
    }

    public Path exportStripePaymentIntentsToCsv(String search, int page, int pageSize,
            String direction) throws IOException {

        StripePaymentIntentSearchResultDto result =
                getAllStripePaymentIntents(search, page, pageSize, direction);
        List<FullStripePaymentIntentDto> data = result.getResult().getItems();

        String fileName = "transactions.csv";
        return csvFileService.generateCsvFile(fileName, data);
    }

}
