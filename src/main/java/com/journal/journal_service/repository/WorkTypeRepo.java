package com.journal.journal_service.repository;

import com.journal.journal_service.models.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkTypeRepo extends JpaRepository<WorkType, Long> {
    List<WorkType> findAll();
}
