package com.journal.journal_service.scheduler;


import com.journal.journal_service.constants.AppConstants;
import com.journal.journal_service.models.Reminder;
import com.journal.journal_service.repository.ReminderRepo;
import com.journal.journal_service.services.MailingService;
import com.journal.journal_service.services.implementation.JournalServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReminderScheduler.class);

    @Autowired
    ReminderRepo reminderRepo;

    @Autowired
    MailingService mailingService;

    @Scheduled(fixedRate = 30000)
    public void setReminders() throws Exception {
        try {
            List<Reminder> toBeSet = reminderRepo.findByIsActiveTrueAndIsDeletedFalse();
            List<Reminder> toSave = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (Reminder reminder : toBeSet) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(reminder.getReminderDate() + "T" + reminder.getReminderTime(), formatter);
                log.info("USER's selected time:" + String.valueOf(dateTime));
                ZonedDateTime istTime = ZonedDateTime.now(ZoneId.of(reminder.getUserTimeZone()));
                log.info("TIME before converting ::" + istTime.toString());
                LocalDateTime localISTTime = istTime.toLocalDateTime();
                log.info("NOW " + localISTTime);
                if (dateTime.isEqual(localISTTime) || dateTime.isBefore(localISTTime)) {
                    //call mailing service here
                    mailingService.sendEmail(reminder);
                    if (reminder.getFrequency() == null) {
                        reminder.setActive(false);
                        reminder.setDeleted(true);
                    } else {
                        int frequency = getFrequency(reminder.getFrequency());
                        log.info("Adding " + frequency + " to " + dateTime + " ------> next reminder date --->" + LocalDate.from(dateTime.plusDays(frequency)));
                        reminder.setReminderDate(LocalDate.from(dateTime.plusDays(frequency)));
                    }
                }
                toSave.add(reminder);
            }
            reminderRepo.saveAllAndFlush(toSave);
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    private Integer getFrequency(String frequency) {
        switch (frequency) {
            case AppConstants.WEEKLY -> {
                return AppConstants.WEEKLY_DAYS;
            }
            case AppConstants.MONTHLY -> {
                return AppConstants.MONTHLY_DAYS;
            }
            case AppConstants.DAILY -> {
                return AppConstants.DAILY_DAY;
            }
            default -> {
                return 0;
            }
        }
    }

}
