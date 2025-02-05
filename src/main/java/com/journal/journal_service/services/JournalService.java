package com.journal.journal_service.services;

import com.journal.journal_service.dto.TaskFormDto;
import com.journal.journal_service.models.JournalEntry;

import java.util.List;
import java.util.Map;

public interface JournalService {

    String createJournalEntry(TaskFormDto taskFormDto) throws Exception;

    List<JournalEntry> getAllEntriesByUserId() throws Exception;

    List<JournalEntry> deleteEntry(Long id) throws Exception;

    Map<String, Object> journalStats() throws Exception;

    byte[] exportAllEntries() throws Exception;
}
