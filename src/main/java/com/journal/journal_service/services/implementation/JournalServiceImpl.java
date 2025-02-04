package com.journal.journal_service.services.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.journal.journal_service.dto.TaskFormDto;
import com.journal.journal_service.dto.WorkTypeDto;
import com.journal.journal_service.models.JournalEntry;
import com.journal.journal_service.repository.JournalRepo;
import com.journal.journal_service.services.JournalService;
import com.journal.journal_service.utility.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class JournalServiceImpl implements JournalService {
    private static final Logger log = LoggerFactory.getLogger(JournalServiceImpl.class);

    @Autowired
    JournalRepo journalRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public String createJournalEntry(TaskFormDto taskFormDto) throws Exception {
        try {
            JournalEntry je = new JournalEntry();
            Long userId = jwtUtil.getUserId();
            if (taskFormDto.getTaskId() != null) {
                je = journalRepo.findByIdAndIsActive(taskFormDto.getTaskId(), true);
            }
            je.setUserId(userId);
            je.setEntryTitle(taskFormDto.getTaskName());
            je.setWorkType(taskFormDto.getTypeOfWork());
            je.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(taskFormDto.getDate()));
            je.setLastModified(LocalDateTime.now());
            je.setDescription(taskFormDto.getDescription());
            journalRepo.saveAndFlush(je);
            return "Journal Entry created for " + je.getDate();
        } catch (Exception e) {
            log.info("Error creating journal entry::" + e.getMessage());
            throw new Exception(e);
        }
    }

    @Override
    public List<JournalEntry> getAllEntriesByUserId() throws Exception {
        try {
            Long userId = jwtUtil.getUserId();
            return journalRepo.findByUserIdAndIsActiveOrderByLastModifiedDesc(userId, true);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<JournalEntry> deleteEntry(Long id) throws Exception {
        try {
            Long userId = jwtUtil.getUserId();
            JournalEntry je = journalRepo.findByUserIdAndId(userId, id);
            je.setActive(false);
            journalRepo.saveAndFlush(je);
            return journalRepo.findByUserIdAndIsActive(userId, true);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Map<String, Object> journalStats() throws Exception {
        Map<String, Object> res = new HashMap<>();
        try {
            Long userId = jwtUtil.getUserId();
            int count = journalRepo.findByUserIdAndIsActive(userId, true).size();
            List<Object[]> workTypeStats= journalRepo.getStatsGroupedByWorkType();
            workTypeStats.forEach(e->{
                String[] s = Arrays.deepToString(e).split(",");
                System.out.println(Arrays.deepToString(s).split(",")[2]);
                res.put(s[1].replace(" ",""),Arrays.deepToString(s).split(",")[2].replace("]",""));
            });

            res.put("totalEntryCount",count);

            return res;
        } catch (Exception e) {
            throw new Exception(e);
        }

    }
}
