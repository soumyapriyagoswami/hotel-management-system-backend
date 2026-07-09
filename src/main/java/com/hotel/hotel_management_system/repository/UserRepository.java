package com.hotel.hotel_management_system.repository;

import com.hotel.hotel_management_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByEnabledTrue();

    List<User> findByRolesContaining(String role);

    @Query("SELECT u FROM User u WHERE u.lastLogin < :date AND u.enabled = true")
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);

    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :timestamp WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("timestamp") LocalDateTime timestamp);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}