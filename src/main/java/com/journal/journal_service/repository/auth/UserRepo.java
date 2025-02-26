package com.journal.journal_service.repository.auth;

import com.journal.journal_service.models.auth.User;
import com.journal.journal_service.models.auth.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    User save(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    @Query("SELECT CASE WHEN COUNT(*) > 1 THEN TRUE ELSE FALSE END AS is_duplicate FROM User WHERE username = :username")
    Boolean isUserNameDuplicate(@Param("username") String username);

    Optional<User> findByUsernameAndIsActive(String username, boolean isActive);


    @Query(value = "select * from users_details where email = :email",nativeQuery = true)
    Optional<UserDetails> findByEmail(@Param("email") String email);

    User findByIdAndIsActiveTrue(Long userId);
}

