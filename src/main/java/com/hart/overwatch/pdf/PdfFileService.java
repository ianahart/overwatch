package com.hart.overwatch.pdf;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto;
import com.hart.overwatch.util.MyUtil;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.servlet.http.HttpServletResponse;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.Document;


@Service
public class PdfFileService {
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
            table.addCell(new Paragraph(MyUtil.convertCentsToDollars(transaction.getAmount())));
            table.addCell(
                    new Paragraph(MyUtil.convertCentsToDollars(transaction.getApplicationFee())));
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

    public void generatePdfFile(HttpServletResponse response,
            List<FullStripePaymentIntentDto> transactions) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        document.add(new Paragraph("Transactions Report").setFontSize(16));

        appendTransactionsToPdf(document, transactions);

        document.close();
    }
}

