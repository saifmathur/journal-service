package com.journal.journal_service.services.implementation;



import com.journal.journal_service.constants.AppConstants;
import com.journal.journal_service.models.ReportAnalyzer;
import com.journal.journal_service.repository.ReportAnalyzerRepo;
import com.journal.journal_service.services.StorageService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {

    private static final Logger log = LoggerFactory.getLogger(OpenAIService.class);

    @Autowired
    Environment environment;

    @Autowired
    private WebClient webClient;

    @Autowired
    StorageService storageService;

    @Autowired
    ReportAnalyzerRepo reportAnalyzerRepo;

    public Map<String, Object> OpenAIService(String prompt) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + environment.getProperty("OPEN_AI_KEY"));
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", environment.getProperty("OPEN_AI_MODEL"));
        requestBody.put("messages", new JSONArray().put(new JSONObject().put("role", "user").put("content", prompt)));

        Map<String, Object> requirements = new HashMap<>();
        requirements.put("headers", headers);
        requirements.put("requestBody", requestBody);

        return requirements;
    }

    public String callOpenAI(String url, HttpHeaders headers, JSONObject requestBody) throws Exception {
        String apiUrl = environment.getProperty("OPEN_AI_MASTER") + url;

        return webClient.post()
                .uri(apiUrl)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestBody.toString()), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void getResumePointsBasedOnJobDescription(ReportAnalyzer report) throws Exception {
        try {
            String resume = storageService.extractTextFromS3PDF(report.getBucketFilePath());
            Map<String, Object> newPrompt = OpenAIService("Resume Text: \n"+resume + "Job Description Text: \n" +report.getJobDescription() + AppConstants.OPEN_AI_PROMPT_FOR_JOB_DESCRIPTION_RESUME);
            String response = callOpenAI(environment.getProperty("OPEN_AI_TEXT_RESPONSE"), (HttpHeaders) newPrompt.get("headers"), (JSONObject) newPrompt.get("requestBody"));
            JSONObject responseObj = new JSONObject(response);
            log.info(responseObj.toString());
            String content = String.valueOf(responseObj.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .get("content"));
            content = content.replace("\"", "");
            String s3FilePath = AppConstants.FOLDER_PREFIX_PATH + "user_" + report.getUserId().toString() + "/"+ AppConstants.FOLDER_PREFIX_PATH_GENERATED + report.getReportName();
            storageService.generateAndUploadPdf(content, s3FilePath);

            report.setGeneratedFilePath(s3FilePath);
            reportAnalyzerRepo.saveAndFlush(report);

            report.setStatus(AppConstants.GENERATED_REPORT);
            reportAnalyzerRepo.saveAndFlush(report);
        } catch (Exception e) {
            report.setStatus(AppConstants.FAILED_REPORT);
            reportAnalyzerRepo.saveAndFlush(report);
            throw new Exception(e);
        }
    }



}
