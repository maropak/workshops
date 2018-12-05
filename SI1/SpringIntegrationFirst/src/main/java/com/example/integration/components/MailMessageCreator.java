package com.example.integration.components;

import com.example.integration.model.Confirmation;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;

public class MailMessageCreator {

    public MailMessage createMail(Confirmation confirmation) {

        MailMessage message = new SimpleMailMessage();
        message.setFrom("news@example.com");
        message.setTo("marek@example.com");
        message.setSubject("New news" + confirmation.getConfirmationNumber());
        message.setText("New news created ");

        return message;
    }
}
