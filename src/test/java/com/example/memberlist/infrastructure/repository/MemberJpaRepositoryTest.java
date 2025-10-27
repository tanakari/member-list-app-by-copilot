package com.example.memberlist.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.memberlist.domain.model.Member;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Integration tests for MemberJpaRepository.
 */
@DataJpaTest
class MemberJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member("山田太郎", "やまだたろう", "yamada@example.com");
    }

    @Test
    void testSaveAndFindById() {
        // When
        Member savedMember = memberJpaRepository.save(testMember);
        entityManager.flush();

        // Then
        assertNotNull(savedMember.getId());
        Optional<Member> foundMember = memberJpaRepository.findById(savedMember.getId());
        assertTrue(foundMember.isPresent());
        assertEquals("山田太郎", foundMember.get().getName());
        assertEquals("やまだたろう", foundMember.get().getNameKana());
        assertEquals("yamada@example.com", foundMember.get().getEmail());
    }

    @Test
    void testFindAllActive() {
        // Given - Create and save multiple members
        Member member1 = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        Member member2 = new Member("鈴木花子", "すずきはなこ", "suzuki@example.com");
        Member member3 = new Member("佐藤次郎", "さとうじろう", "sato@example.com");
        member3.delete(); // Mark as deleted

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        entityManager.flush();

        // When
        List<Member> activeMembers = memberJpaRepository.findAllActive();

        // Then
        assertEquals(2, activeMembers.size());
        assertTrue(activeMembers.stream().anyMatch(m -> m.getEmail().equals("yamada@example.com")));
        assertTrue(activeMembers.stream().anyMatch(m -> m.getEmail().equals("suzuki@example.com")));
        assertFalse(activeMembers.stream().anyMatch(m -> m.getEmail().equals("sato@example.com")));
    }

    @Test
    void testFindAllActiveOrderByCreatedAtDesc() {
        // Given - Create members with different creation times
        Member member1 = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        Member savedMember1 = memberJpaRepository.save(member1);
        entityManager.flush();

        // Wait a bit to ensure different timestamps
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Member member2 = new Member("鈴木花子", "すずきはなこ", "suzuki@example.com");
        Member savedMember2 = memberJpaRepository.save(member2);
        entityManager.flush();

        // When
        List<Member> activeMembers = memberJpaRepository.findAllActive();

        // Then
        assertEquals(2, activeMembers.size());
        // Most recent first
        assertEquals(savedMember2.getId(), activeMembers.get(0).getId());
        assertEquals(savedMember1.getId(), activeMembers.get(1).getId());
    }

    @Test
    void testFindByEmailAndNotDeleted() {
        // Given
        memberJpaRepository.save(testMember);
        entityManager.flush();

        // When
        Optional<Member> foundMember = memberJpaRepository.findByEmailAndNotDeleted("yamada@example.com");

        // Then
        assertTrue(foundMember.isPresent());
        assertEquals("山田太郎", foundMember.get().getName());
    }

    @Test
    void testFindByEmailAndNotDeletedReturnsEmptyForNonExistent() {
        // When
        Optional<Member> foundMember = memberJpaRepository.findByEmailAndNotDeleted("nonexistent@example.com");

        // Then
        assertFalse(foundMember.isPresent());
    }

    @Test
    void testFindByEmailAndNotDeletedReturnsEmptyForDeleted() {
        // Given
        testMember.delete();
        memberJpaRepository.save(testMember);
        entityManager.flush();

        // When
        Optional<Member> foundMember = memberJpaRepository.findByEmailAndNotDeleted("yamada@example.com");

        // Then
        assertFalse(foundMember.isPresent());
    }

    @Test
    void testExistsByEmailAndNotDeleted() {
        // Given
        memberJpaRepository.save(testMember);
        entityManager.flush();

        // When
        boolean exists = memberJpaRepository.existsByEmailAndNotDeleted("yamada@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void testExistsByEmailAndNotDeletedReturnsFalseForNonExistent() {
        // When
        boolean exists = memberJpaRepository.existsByEmailAndNotDeleted("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    void testExistsByEmailAndNotDeletedReturnsFalseForDeleted() {
        // Given
        testMember.delete();
        memberJpaRepository.save(testMember);
        entityManager.flush();

        // When
        boolean exists = memberJpaRepository.existsByEmailAndNotDeleted("yamada@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    void testUpdateMember() {
        // Given
        Member savedMember = memberJpaRepository.save(testMember);
        entityManager.flush();
        Long memberId = savedMember.getId();

        // When
        savedMember.updateInfo("鈴木花子", "すずきはなこ", "suzuki@example.com",
                "エンジニア", "東京都", "https://example.com/image.jpg", "よろしくお願いします");
        memberJpaRepository.save(savedMember);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Member> updatedMember = memberJpaRepository.findById(memberId);
        assertTrue(updatedMember.isPresent());
        assertEquals("鈴木花子", updatedMember.get().getName());
        assertEquals("すずきはなこ", updatedMember.get().getNameKana());
        assertEquals("suzuki@example.com", updatedMember.get().getEmail());
        assertEquals("エンジニア", updatedMember.get().getPosition());
        assertEquals("東京都", updatedMember.get().getLocation());
        assertEquals("https://example.com/image.jpg", updatedMember.get().getProfileImageUrl());
        assertEquals("よろしくお願いします", updatedMember.get().getSelfIntroduction());
    }

    @Test
    void testDeleteMember() {
        // Given
        Member savedMember = memberJpaRepository.save(testMember);
        entityManager.flush();
        Long memberId = savedMember.getId();

        // When
        savedMember.delete();
        memberJpaRepository.save(savedMember);
        entityManager.flush();

        // Then
        Optional<Member> foundMember = memberJpaRepository.findById(memberId);
        assertTrue(foundMember.isPresent());
        assertTrue(foundMember.get().isDeleted());

        // Should not appear in active members
        List<Member> activeMembers = memberJpaRepository.findAllActive();
        assertFalse(activeMembers.stream().anyMatch(m -> m.getId().equals(memberId)));
    }
}
