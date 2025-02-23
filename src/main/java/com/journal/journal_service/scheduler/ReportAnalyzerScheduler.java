package com.journal.journal_service.scheduler;

import com.journal.journal_service.constants.AppConstants;
import com.journal.journal_service.models.ReportAnalyzer;
import com.journal.journal_service.repository.ReportAnalyzerRepo;
import com.journal.journal_service.services.implementation.OpenAIService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ReportAnalyzerScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReportAnalyzerScheduler.class);

    @Autowired
    ReportAnalyzerRepo reportAnalyzerRepo;

    @Autowired
    OpenAIService openAIService;


    @Scheduled(fixedRate = 30000)
    public void processReports() throws Exception {
        try {
            List<ReportAnalyzer> toBeProcessed = reportAnalyzerRepo.findByIsDeletedFalseAndStatusLike(AppConstants.QUEUED_REPORT);
            for(ReportAnalyzer report: toBeProcessed){
                report.setStatus(AppConstants.IN_PROGRESS_REPORT);
                reportAnalyzerRepo.saveAndFlush(report);
                openAIService.getResumePointsBasedOnJobDescription(report);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
