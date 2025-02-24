package com.journal.journal_service.controller;


import com.journal.journal_service.models.ReportAnalyzer;
import com.journal.journal_service.repository.ReportAnalyzerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    ReportAnalyzerRepo reportAnalyzerRepo;

    private final SimpMessagingTemplate messagingTemplate;

    public StatusController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/update")
    public void updateStatus(@RequestParam Long reportId, @RequestParam String status) {
        // Send updated status to all subscribers
        ReportAnalyzer report = reportAnalyzerRepo.findById(reportId).orElseThrow();
        messagingTemplate.convertAndSend("/topic/status", new ReportStatusUpdate(reportId, report.getStatus()));
    }

    static class ReportStatusUpdate {
        private Long reportId;
        private String status;

        public ReportStatusUpdate(Long reportId, String status) {
            this.reportId = reportId;
            this.status = status;
        }

        public Long getReportId() { return reportId; }
        public String getStatus() { return status; }
    }
}
