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

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${emailsender}")
    private String fromSender;


    public void sendEmail(EmailRequest emailRequest) {
        Email from = new Email(fromSender);
        Email to = new Email(emailRequest.getTo());
        String subject = emailRequest.getSubject();
        Content content = new Content("text/plain", emailRequest.getBody());

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
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
