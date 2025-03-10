package com.journal.journal_service.controller.journalApp;

import com.journal.journal_service.dto.TaskFormDto;
import com.journal.journal_service.models.JournalEntry;
import com.journal.journal_service.services.JournalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


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

    @GetMapping("/deleteEntry/{id}")
    public List<JournalEntry> deleteEntry(@PathVariable Long id) throws Exception {
        try {
            return journalService.deleteEntry(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/journalStats")
    public Map<String,Object> journalStats() throws Exception {
        try {
            return journalService.journalStats();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/exportAllEntries")
    public ResponseEntity<byte[]> exportAllEntries() throws Exception {
        try {
            byte[] excelData =journalService.exportAllEntries();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "data-export.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
