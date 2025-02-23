package com.journal.journal_service.repository;

import com.journal.journal_service.models.ReportAnalyzer;
import com.journal.journal_service.models.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportAnalyzerRepo extends JpaRepository<ReportAnalyzer, Long> {
    List<ReportAnalyzer> findByUserId(Long userId);

    List<ReportAnalyzer> findByUserIdAndIsDeletedFalse(Long userId);

    List<ReportAnalyzer> findByIsDeletedFalseAndStatusLike(String queuedReport);
}
