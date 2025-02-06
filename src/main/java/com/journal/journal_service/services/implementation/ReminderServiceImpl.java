package com.journal.journal_service.services.implementation;

import com.journal.journal_service.dto.ReminderDto;
import com.journal.journal_service.models.Reminder;
import com.journal.journal_service.repository.ReminderRepo;
import com.journal.journal_service.services.JournalService;
import com.journal.journal_service.services.ReminderService;
import com.journal.journal_service.utility.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReminderServiceImpl implements ReminderService {
    private static final Logger log = LoggerFactory.getLogger(ReminderServiceImpl.class);
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ReminderRepo reminderRepo;

    @Override
    public List<Reminder> createReminder(ReminderDto reminderDto) throws Exception {
        try {
            Long userId = jwtUtil.getUserId();
            Reminder reminder = new Reminder();
            reminder.setUserId(userId);
            reminder.setTitle(reminderDto.getTitle());
            reminder.setNotes(reminderDto.getNotes());
            reminder.setReminderDate(LocalDate.from(ZonedDateTime.parse(reminderDto.getReminderDate())));
            reminder.setReminderTime(LocalTime.from((ZonedDateTime.parse(reminderDto.getReminderTime()))));
            reminder.setPriority(reminderDto.getPriority());
            reminder.setFrequency(reminderDto.getFrequency());
            reminder.setLastModified(LocalDateTime.now());

            reminderRepo.saveAndFlush(reminder);

            return reminderRepo.findByUserIdAndIsDeletedFalseOrderByLastModifiedDesc(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error creating reminder" + e.getMessage());
        }
    }

    @Override
    public List<Reminder> getAllRemindersByUserId() throws Exception {
        try {
            return reminderRepo.findByUserIdAndIsDeletedFalseOrderByLastModifiedDesc(jwtUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error fetching reminders" + e.getMessage());
        }
    }

    @Override
    public Map<String,Object> setReminderActiveOrInactive(Long reminderId) throws Exception {
        Map<String,Object> res = new HashMap<>();
        try {
            Reminder reminder = reminderRepo.findByUserIdAndIdAndIsDeletedFalse(jwtUtil.getUserId(), reminderId);
            boolean state = reminder.isActive();
            if (state) {
                reminder.setActive(false);
            }
            if(!state){
                reminder.setActive(true);
            }
            reminder.setLastModified(LocalDateTime.now());
            reminderRepo.saveAndFlush(reminder);
            res.put("reminders",getAllRemindersByUserId());
            res.put("state",state);
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
