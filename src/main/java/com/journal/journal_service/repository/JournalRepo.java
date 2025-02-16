package com.journal.journal_service.repository;

import com.journal.journal_service.dto.WorkTypeDto;
import com.journal.journal_service.models.JournalEntry;
import com.journal.journal_service.models.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JournalRepo extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUserId(Long userId);

    List<JournalEntry> findByUserIdAndIsActive(Long userId, boolean b);

    JournalEntry findByUserIdAndId(Long userId, Long id);

    JournalEntry findByIdAndIsActive(Long taskId, boolean b);

    List<JournalEntry> findByUserIdAndIsActiveOrderByLastModifiedDesc(Long userId, boolean b);

    @Query(value = "select\n" +
            "  je.work_type_id as workTypeId,\n" +
            "  wt.work_type as workType,\n" +
            "  COUNT(je.id) as totalEntries\n" +
            "from\n" +
            "  journal_entry je\n" +
            "  join work_type wt on je.work_type_id = wt.id\n" +
            "where je.user_id = :userId\n" +
            "group by\n" +
            "  je.work_type_id,\n" +
            "  wt.work_type;",nativeQuery = true)
    List<Object[]> getStatsGroupedByWorkType(@Param("userId") Long userId);

}
