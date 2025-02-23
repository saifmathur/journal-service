package com.journal.journal_service.controller.journalApp;


import com.journal.journal_service.dto.ReminderDto;
import com.journal.journal_service.dto.ReportAnalyzerDto;
import com.journal.journal_service.models.Reminder;
import com.journal.journal_service.models.ReportAnalyzer;
import com.journal.journal_service.services.ReportAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analyzer")
public class ReportAnalyzerController {

    @Autowired
    ReportAnalyzerService reportAnalyzerService;

    @PostMapping("/createReport")
    public List<ReportAnalyzer> createReport(@ModelAttribute ReportAnalyzerDto reportAnalyzerDto) throws Exception {
        try {
            return reportAnalyzerService.createReport(reportAnalyzerDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/getAllReports")
    public List<ReportAnalyzer> getAllReports() throws Exception {
        try{
            return reportAnalyzerService.getAllReports();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    @GetMapping("/downloadReport/{id}")
    public ResponseEntity<Resource> downloadReport(@PathVariable Long id) throws Exception {
        try{
            return reportAnalyzerService.downloadReport(id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
