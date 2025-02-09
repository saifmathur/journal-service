package com.journal.journal_service.services.implementation;

import com.journal.journal_service.dto.ReminderDto;
import com.journal.journal_service.models.JournalEntry;
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
import java.time.*;
import java.util.ArrayList;
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
            if (reminderDto.getReminderId() != null) {
                reminder = reminderRepo.findByIdAndIsDeletedFalse(reminderDto.getReminderId());
            }
            reminder.setUserId(userId);
            reminder.setTitle(reminderDto.getTitle());
            reminder.setNotes(reminderDto.getNotes());
            Instant instant = Instant.parse(reminderDto.getReminderDate());
//            reminder.setReminderDate(instant.atZone(ZoneOffset.UTC).toLocalDate());
//            reminder.setReminderTime(instant.atZone(ZoneOffset.UTC).toLocalTime());
            LocalDate localDate = OffsetDateTime.parse(reminderDto.getReminderDate()).toLocalDate();
            LocalTime localTime = OffsetDateTime.parse(reminderDto.getReminderTime()).toLocalTime().withNano(0); //removes ms
            reminder.setReminderDate(localDate);
            reminder.setReminderTime(localTime);

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

    @Override
    public List<Reminder> deleteReminder(Long id) throws Exception {
        try {
            Long userId = jwtUtil.getUserId();
            Reminder je = reminderRepo.findByUserIdAndIdAndIsDeletedFalse(userId, id);
            je.setDeleted(true);
            reminderRepo.saveAndFlush(je);
            return reminderRepo.findByUserIdAndIsDeletedFalseOrderByLastModifiedDesc(userId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<Reminder> toggleAllReminders() throws Exception {
        try{
            List<Reminder> allActiveReminders = reminderRepo.findByUserIdAndIsActiveAndIsDeletedFalse(jwtUtil.getUserId(), true);
            List<Reminder> toBeSubmitted  = new ArrayList<>();
            for(Reminder r: allActiveReminders){
                r.setActive(false);
                toBeSubmitted.add(r);
            }
            reminderRepo.saveAllAndFlush(toBeSubmitted);
            return reminderRepo.findByUserIdAndIsDeletedFalseOrderByLastModifiedDesc(jwtUtil.getUserId());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
