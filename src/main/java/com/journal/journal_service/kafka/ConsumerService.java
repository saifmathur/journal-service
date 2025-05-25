package com.journal.journal_service.kafka;


import com.journal.journal_service.models.Reminder;
import com.journal.journal_service.repository.ReminderRepo;
import com.journal.journal_service.services.MailingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {


    @Autowired
    MailingService mailingService;

    @Autowired
    ReminderRepo reminderRepo;


    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2), // 2s → 4s → 8s
            dltTopicSuffix = "-dlt", // messages go to reminder-topic-dlt after retries fail
            autoCreateTopics = "true"
    )
    @KafkaListener(topics = "reminder-topic", groupId = "reminder-group")
    public void consumeMessage(String topic, String reminderId) throws Exception {
        // Logic to process the consumed message
        log.info("Consumed message from topic {}: {}", topic, reminderId);
        try {
            Reminder reminder1 = reminderRepo.findByIdAndIsDeletedFalse(Long.valueOf(reminderId));
            mailingService.sendEmail(reminder1);
        }catch (Exception e) {
            log.error("Error sending email for reminder {}: {}", reminderId, e.getMessage());
            throw e; // Re-throw the exception to ensure it can be handled by the Kafka listener
        }
    }
}
