package com.journal.journal_service.repository;

import com.journal.journal_service.models.JournalEntry;
import com.journal.journal_service.models.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalRepo extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUserId(Long userId);

    List<JournalEntry> findByUserIdAndIsActive(Long userId, boolean b);
}
