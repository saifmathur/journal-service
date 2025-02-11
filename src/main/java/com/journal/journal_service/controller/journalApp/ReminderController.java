package com.journal.journal_service.controller.journalApp;


import com.journal.journal_service.dto.ReminderDto;
import com.journal.journal_service.dto.TaskFormDto;
import com.journal.journal_service.models.JournalEntry;
import com.journal.journal_service.models.Reminder;
import com.journal.journal_service.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reminder")
public class ReminderController {

    @Autowired
    ReminderService reminderService;


    @PostMapping("/createReminder")
    public List<Reminder> createJournalEntry(@RequestBody ReminderDto reminderDto) throws Exception {
        try {
            return reminderService.createReminder(reminderDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/getAllReminders")
    public List<Reminder> getEntries() throws Exception {
        try{
            return reminderService.getAllRemindersByUserId();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/setReminderActiveOrInactive/{id}")
    public ResponseEntity<Map<String,Object>> setReminderActiveOrInactive(@PathVariable Long id) throws Exception {
        try{
            return ResponseEntity.ok(reminderService.setReminderActiveOrInactive(id));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<List<Reminder>> deleteReminder (@PathVariable Long id) throws Exception {
        try{
            return ResponseEntity.ok(reminderService.deleteReminder(id));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/toggleAllReminders")
    public ResponseEntity<List<Reminder>> toggleAllReminders () throws Exception {
        try{
            return ResponseEntity.ok(reminderService.toggleAllReminders());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/getActiveReminders")
    public ResponseEntity<Integer> getActiveReminders () throws Exception {
        try{
            return ResponseEntity.ok(reminderService.getActiveReminders());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
