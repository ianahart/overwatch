package com.hart.overwatch.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hart.overwatch.email.request.EmailRequest;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailSender {


    @Value("${emailsender}")
    private String fromSender;

    private final SendGrid sendGrid;

    public EmailSender(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }


    public void sendEmail(EmailRequest emailRequest) {
        Email from = new Email(fromSender);
        Email to = new Email(emailRequest.getTo());
        String subject = emailRequest.getSubject();
        String body = emailRequest.getBody()
                + "<br><br><a href='https://codeoverwatch.com/unsubscribe?email="
                + emailRequest.getTo()
                + "' style='color: red; text-decoration: none;'>Unsubscribe</a>";

        Content content = new Content("text/html", body);

        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            System.out.println();
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.printf("Sending email to: %s, Subject: %s%n", emailRequest.getTo(),
                    emailRequest.getSubject());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
