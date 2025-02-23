package com.journal.journal_service.services;

import com.journal.journal_service.dto.ReportAnalyzerDto;
import com.journal.journal_service.models.Reminder;
import com.journal.journal_service.models.ReportAnalyzer;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReportAnalyzerService {
    List<ReportAnalyzer> createReport(ReportAnalyzerDto reportAnalyzerDto) throws Exception;

    List<ReportAnalyzer> getAllReports() throws Exception;;

    ResponseEntity<Resource> downloadReport(Long reportId) throws Exception;
}
