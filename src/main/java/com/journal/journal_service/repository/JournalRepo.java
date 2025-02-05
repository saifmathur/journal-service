package com.journal.journal_service.repository;

import com.journal.journal_service.dto.WorkTypeDto;
import com.journal.journal_service.models.JournalEntry;
import com.journal.journal_service.models.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JournalRepo extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUserId(Long userId);

    List<JournalEntry> findByUserIdAndIsActive(Long userId, boolean b);

    JournalEntry findByUserIdAndId(Long userId, Long id);

    JournalEntry findByIdAndIsActive(Long taskId, boolean b);

    List<JournalEntry> findByUserIdAndIsActiveOrderByLastModifiedDesc(Long userId, boolean b);

    @Query(value = "SELECT \n" +
            "    je.work_type_id as workTypeId, \n" +
            "    wt.work_type AS workType, \n" +
            "    COUNT(je.id) AS totalEntries\n" +
            "FROM \n" +
            "    journal_entry je\n" +
            "JOIN \n" +
            "    work_type wt \n" +
            "ON \n" +
            "    je.work_type_id = wt.id\n" +
            "GROUP BY \n" +
            "    je.work_type_id, wt.work_type;",nativeQuery = true)
    List<Object[]> getStatsGroupedByWorkType();

}
