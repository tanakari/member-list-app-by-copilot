package com.example.memberlist.domain.repository;

import com.example.memberlist.domain.model.Member;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Member aggregate.
 * Defines the contract for persistence operations on Member entities.
 */
public interface MemberRepository {

    /**
     * Saves a member.
     *
     * @param member the member to save
     * @return the saved member
     */
    Member save(Member member);

    /**
     * Finds a member by ID.
     *
     * @param id the member ID
     * @return an Optional containing the member if found, empty otherwise
     */
    Optional<Member> findById(Long id);

    /**
     * Finds all members that are not deleted.
     *
     * @return list of active members
     */
    List<Member> findAll();

    /**
     * Finds a member by email address.
     *
     * @param email the email address
     * @return an Optional containing the member if found, empty otherwise
     */
    Optional<Member> findByEmail(String email);

    /**
     * Checks if a member with the given email exists (excluding deleted members).
     *
     * @param email the email address
     * @return true if a member with the email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Deletes a member.
     *
     * @param member the member to delete
     */
    void delete(Member member);
}
