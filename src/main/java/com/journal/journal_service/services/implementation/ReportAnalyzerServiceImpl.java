package com.journal.journal_service.services.implementation;

import com.journal.journal_service.constants.AppConstants;
import com.journal.journal_service.dto.ReportAnalyzerDto;
import com.journal.journal_service.models.Reminder;
import com.journal.journal_service.models.ReportAnalyzer;
import com.journal.journal_service.repository.ReportAnalyzerRepo;
import com.journal.journal_service.services.ReportAnalyzerService;
import com.journal.journal_service.services.StorageService;
import com.journal.journal_service.utility.JwtUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class ReportAnalyzerServiceImpl implements ReportAnalyzerService {

    private static final Logger log = LoggerFactory.getLogger(ReportAnalyzerServiceImpl.class);

    @Autowired
    ReportAnalyzerRepo reportAnalyzerRepo;

    @Autowired
    StorageService storageService;

    @Autowired
    JwtUtil jwtUtil;


    @Override
    public List<ReportAnalyzer> createReport(ReportAnalyzerDto reportAnalyzerDto) throws Exception {
        log.info(String.valueOf(reportAnalyzerDto));
        MultipartFile file = reportAnalyzerDto.getResume();
        Long userId = jwtUtil.getUserId();
        if (file == null || file.isEmpty()) {
            throw new Exception("No file uploaded or file is empty.");
        }
        if (!file.getContentType().equals("application/pdf")) {
            throw new Exception("Only PDF files are allowed.");
        }
        ReportAnalyzer reportAnalyzer = new ReportAnalyzer();
        reportAnalyzer.setReportName(reportAnalyzerDto.getReportName());
        reportAnalyzer.setJobDescription(reportAnalyzerDto.getJobDescription());
        String s3FilePath = AppConstants.FOLDER_PREFIX_PATH + "user_" + userId.toString() + "/" + file.getOriginalFilename();
        String response = storageService.uploadFile(file, s3FilePath);
        log.info("uploaded to s3: " + response);
        reportAnalyzer.setUserId(userId);
        reportAnalyzer.setBucketFilePath(response);
        reportAnalyzer.setStatus(AppConstants.QUEUED_REPORT);
        reportAnalyzerRepo.saveAndFlush(reportAnalyzer);

        return reportAnalyzerRepo.findAll();
    }

    @Override
    public List<ReportAnalyzer> getAllReports() throws Exception {
        try {
            return reportAnalyzerRepo.findByUserIdAndIsDeletedFalse(jwtUtil.getUserId());
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    @Override
    public ResponseEntity<Resource> downloadReport(Long reportId) throws Exception {
        try {
            ReportAnalyzer report = reportAnalyzerRepo.findById(reportId).orElse(null);
            if (report != null) {
                return storageService.downloadFile(report.getGeneratedFilePath());
            }
            else {
                return (ResponseEntity<Resource>) ResponseEntity.internalServerError();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }



}
