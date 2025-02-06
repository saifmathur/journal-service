package com.journal.journal_service.repository;

import com.journal.journal_service.models.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReminderRepo extends JpaRepository<Reminder, Long> {
//    void saveAndFlush(@NotNull Reminder reminder);

    List<Reminder> findByUserIdAndIsActiveAndIsDeletedFalse(Long userId, boolean b);

    Reminder findByUserIdAndIdAndIsDeletedFalse(Long userId, Long reminderId);

    List<Reminder> findByUserIdAndIsDeletedFalseOrderByLastModifiedDesc(Long userId);
}
