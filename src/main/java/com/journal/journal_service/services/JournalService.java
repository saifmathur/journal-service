package com.journal.journal_service.services;

import com.journal.journal_service.dto.TaskFormDto;
import com.journal.journal_service.models.JournalEntry;

import java.util.List;

public interface JournalService {

    String createJournalEntry(TaskFormDto taskFormDto) throws Exception;

    List<JournalEntry> getAllEntriesByUserId(Long userId);
}
