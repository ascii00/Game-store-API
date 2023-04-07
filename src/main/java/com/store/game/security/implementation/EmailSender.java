package com.store.game.security.implementation;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@Slf4j
public class EmailSender {

    private final SendGrid sendGrid;
    @Value("${email.from}")
    private String emailFrom;

    @Autowired
    public EmailSender(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendEmail(String to, String subject, String content) {
        Content contentEmail = new Content("text/plain", content);
        Email fromEmail = new Email(emailFrom);
        Email toEmail = new Email(to);
        Mail mail = new Mail(fromEmail, subject, toEmail, contentEmail);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            log.info(response.getStatusCode() +
                            " "
                            + response.getBody() +
                            " "
                            + response.getHeaders());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
