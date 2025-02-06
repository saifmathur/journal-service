package com.journal.journal_service.services;

import com.journal.journal_service.dto.ReminderDto;
import com.journal.journal_service.dto.TaskFormDto;
import com.journal.journal_service.models.Reminder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ReminderService {
    List<Reminder> createReminder(ReminderDto reminderDto) throws Exception;

    List<Reminder> getAllRemindersByUserId() throws Exception;

    Map<String,Object> setReminderActiveOrInactive(Long id) throws Exception;
}
