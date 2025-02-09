package com.journal.journal_service.services;

import com.journal.journal_service.models.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public interface MailingService {

    void sendEmail(Reminder reminder) throws Exception;


}
