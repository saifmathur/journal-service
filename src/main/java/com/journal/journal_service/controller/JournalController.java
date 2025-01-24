package com.journal.journal_service.controller;

import com.journal.journal_service.dto.TaskFormDto;
import com.journal.journal_service.models.JournalEntry;
import com.journal.journal_service.services.JournalService;
import com.journal.journal_service.services.implementation.JournalServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/journal")
public class JournalController {
    private static final Logger log = LoggerFactory.getLogger(JournalController.class);

    @Autowired
    JournalService journalService;

    @RequestMapping("/testString")
    public String getTestString() {

        return "TEST STRING SENT";
    }

    @GetMapping("/getEntries/{userId}")
    public List<JournalEntry> getEntries(@PathVariable Long userId) throws Exception {
        try{
            return journalService.getAllEntriesByUserId(userId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PostMapping("/createJournalEntry")
    public String createJournalEntry(@RequestBody TaskFormDto taskFormDto) throws Exception {
        try {
            return journalService.createJournalEntry(taskFormDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
