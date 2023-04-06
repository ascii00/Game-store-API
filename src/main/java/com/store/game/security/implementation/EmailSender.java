package com.store.game.security.implementation;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class EmailSender {

    private final SendGrid sendGrid;

    @Autowired
    public EmailSender(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendEmail(String to, String subject, String content, String templateId) {
        Email fromEmail = new Email("ascii.test.api@gmail.com");
        Email toEmail = new Email(to);

        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);
        personalization.setSubject(subject);
        personalization.addDynamicTemplateData("content", content);

        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            //log response
            //System.out.println(response.getStatusCode());
            //System.out.println(response.getBody());
            //System.out.println(response.getHeaders());
        } catch (IOException ex) {
            // log error
        }
    }
}
