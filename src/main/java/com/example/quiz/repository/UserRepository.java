package com.example.quiz.repository;


import com.example.quiz.dto.repoMaps.LoginRequestListModel;
import com.example.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    // ✅ JPQL query (use entity name, not table name)
    @Query("SELECT new com.example.quiz.dto.repoMaps.LoginRequestListModel(" +
            "u.id, u.username, u.email, u.firstName, u.lastName,u.password) " +
            "FROM User u " +
            "WHERE (:usernameOrEmail IS NULL OR LOWER(u.username) = LOWER(:usernameOrEmail) " +
            "OR LOWER(u.email) = LOWER(:usernameOrEmail))")
    Optional<LoginRequestListModel> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);


}
