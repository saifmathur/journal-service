package com.journal.journal_service.services.implementation;

import com.journal.journal_service.dto.TaskFormDto;
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
import java.util.Date;
import java.util.List;


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
            je.setUserId(userId);
            je.setEntryTitle(taskFormDto.getTaskName());
            je.setWorkType(taskFormDto.getTypeOfWork());
            je.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(taskFormDto.getDate()));
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
        try{
            Long userId = jwtUtil.getUserId();
            return journalRepo.findByUserIdAndIsActive(userId,true);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
