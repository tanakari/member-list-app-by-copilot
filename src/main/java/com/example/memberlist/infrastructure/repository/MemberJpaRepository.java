package com.example.memberlist.infrastructure.repository;

import com.example.memberlist.domain.model.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * JPA repository interface for Member entity.
 * Provides database access using Spring Data JPA.
 */
@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    /**
     * Finds all members that are not deleted.
     *
     * @return list of active members
     */
    @Query("SELECT m FROM Member m WHERE m.isDeleted = false ORDER BY m.createdAt DESC")
    List<Member> findAllActive();

    /**
     * Finds a member by email address (excluding deleted members).
     *
     * @param email the email address
     * @return an Optional containing the member if found, empty otherwise
     */
    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.isDeleted = false")
    Optional<Member> findByEmailAndNotDeleted(@Param("email") String email);

    /**
     * Checks if a member with the given email exists (excluding deleted members).
     *
     * @param email the email address
     * @return true if a member with the email exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE m.email = :email AND m.isDeleted = false")
    boolean existsByEmailAndNotDeleted(@Param("email") String email);
}
