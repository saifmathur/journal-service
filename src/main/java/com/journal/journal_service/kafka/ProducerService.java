package com.journal.journal_service.kafka;

import com.journal.journal_service.models.Reminder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {


    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReminder(String topic, String id) {
        kafkaTemplate.send(topic, id);
    }

}
