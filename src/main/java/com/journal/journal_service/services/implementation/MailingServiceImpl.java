package com.journal.journal_service.services.implementation;

import com.journal.journal_service.constants.AppConstants;
import com.journal.journal_service.models.Reminder;
import com.journal.journal_service.models.auth.User;
import com.journal.journal_service.repository.auth.UserRepo;
import com.journal.journal_service.services.MailingService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

@Service
public class MailingServiceImpl implements MailingService {

    private final JavaMailSender mailSender;

    @Autowired
    Environment environment;

    @Autowired
    UserRepo userRepo;

    String path = "src/main/resources/templates/EmailTemplate.html";

    @Autowired
    public MailingServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(Reminder reminder) throws Exception {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Optional<User> user = userRepo.findById(reminder.getUserId());
//
            String template = Files.readString(Paths.get(path), StandardCharsets.UTF_8)
                    .replace("{{name}}", user.get().getUserDetails().getFirstName() + " " + user.get().getUserDetails().getLastName())
                    .replace("{{reminderDate}}", reminder.getReminderDate().toString())
                    .replace("{{reminderTime}}", reminder.getReminderTime().toString())
                    .replace("{{reminderTitle}}",reminder.getTitle())
                    .replace("{{notes}}", reminder.getNotes()==null?"NA":reminder.getNotes())
                    .replace("{{link}}","https://journal-taupe-theta.vercel.app/reminders");


            helper.setTo(user.get().getUserDetails().getEmail());
            helper.setSubject(AppConstants.MAIL_SUBJECT_PREFIX + reminder.getTitle());
            helper.setText(template, true);
            helper.setFrom(Objects.requireNonNull(environment.getProperty("GMAIL_APP_MAIL_ID")));
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to send mail" + e.getMessage());

        }

    }
}
