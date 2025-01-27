package com.journal.journal_service.controller.journalApp;

import com.journal.journal_service.dto.TaskFormDto;
import com.journal.journal_service.models.JournalEntry;
import com.journal.journal_service.services.JournalService;
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

    @GetMapping("/getEntries")
    public List<JournalEntry> getEntries() throws Exception {
        try{
            return journalService.getAllEntriesByUserId();
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
