package com.example.memberlist.domain.repository;

import com.example.memberlist.domain.model.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Member entity.
 * Follows DDD repository pattern and Spring Data JPA conventions.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Find a member by email address, excluding logically deleted members.
     *
     * @param email email address to search for
     * @return Optional containing the member if found, empty otherwise
     */
    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.isDeleted = false")
    Optional<Member> findByEmail(@Param("email") String email);

    /**
     * Find members by name, excluding logically deleted members.
     *
     * @param name name to search for
     * @return list of members matching the name
     */
    @Query("SELECT m FROM Member m WHERE m.name = :name AND m.isDeleted = false")
    List<Member> findByName(@Param("name") String name);

    /**
     * Find all non-deleted members.
     *
     * @return list of all non-deleted members
     */
    @Query("SELECT m FROM Member m WHERE m.isDeleted = false ORDER BY m.createdAt DESC")
    List<Member> findAllActive();

    /**
     * Find a non-deleted member by ID.
     *
     * @param id member ID
     * @return Optional containing the member if found and not deleted, empty otherwise
     */
    @Query("SELECT m FROM Member m WHERE m.id = :id AND m.isDeleted = false")
    Optional<Member> findActiveById(@Param("id") Long id);

    /**
     * Find members by name kana, excluding logically deleted members.
     *
     * @param nameKana name kana to search for
     * @return list of members matching the name kana
     */
    @Query("SELECT m FROM Member m WHERE m.nameKana = :nameKana AND m.isDeleted = false")
    List<Member> findByNameKana(@Param("nameKana") String nameKana);
}
