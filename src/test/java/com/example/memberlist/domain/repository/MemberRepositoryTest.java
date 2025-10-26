package com.example.memberlist.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for MemberRepository.
 * Uses @DataJpaTest for lightweight repository testing with an in-memory database.
 * Tests are transactional and rolled back automatically after each test.
 */
@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Member testMember1;
    private Member testMember2;
    private Member deletedMember;

    @BeforeEach
    void setUp() {
        // @DataJpaTest provides automatic transaction rollback,
        // so the database is automatically cleaned between tests.
        // We clear the entity manager to ensure a fresh state.
        entityManager.clear();

        // Create test data
        testMember1 = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        testMember1.updateInfo("山田太郎", "やまだたろう", "yamada@example.com",
                "エンジニア", "東京都", null, "よろしくお願いします");

        testMember2 = new Member("鈴木花子", "すずきはなこ", "suzuki@example.com");
        testMember2.updateInfo("鈴木花子", "すずきはなこ", "suzuki@example.com",
                "デザイナー", "大阪府", null, null);

        deletedMember = new Member("佐藤次郎", "さとうじろう", "sato@example.com");
        deletedMember.delete();
    }

    @Test
    void testSaveMember() {
        // When
        Member saved = memberRepository.save(testMember1);

        // Then
        assertNotNull(saved.getId());
        assertEquals("山田太郎", saved.getName());
        assertEquals("やまだたろう", saved.getNameKana());
        assertEquals("yamada@example.com", saved.getEmail());
        assertEquals("エンジニア", saved.getPosition());
        assertEquals("東京都", saved.getLocation());
        assertFalse(saved.isDeleted());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void testFindById() {
        // Given
        Member saved = memberRepository.save(testMember1);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<Member> found = memberRepository.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("山田太郎", found.get().getName());
        assertEquals("yamada@example.com", found.get().getEmail());
    }

    @Test
    void testFindActiveById_WithActiveMember() {
        // Given
        Member saved = memberRepository.save(testMember1);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<Member> found = memberRepository.findActiveById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertFalse(found.get().isDeleted());
    }

    @Test
    void testFindActiveById_WithDeletedMember() {
        // Given
        Member saved = memberRepository.save(deletedMember);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<Member> found = memberRepository.findActiveById(saved.getId());

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByEmail_WithExistingEmail() {
        // Given
        memberRepository.save(testMember1);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<Member> found = memberRepository.findByEmail("yamada@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("山田太郎", found.get().getName());
        assertEquals("yamada@example.com", found.get().getEmail());
    }

    @Test
    void testFindByEmail_WithNonExistingEmail() {
        // Given
        memberRepository.save(testMember1);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<Member> found = memberRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByEmail_ExcludesDeletedMembers() {
        // Given
        memberRepository.save(deletedMember);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<Member> found = memberRepository.findByEmail("sato@example.com");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByName_WithExistingName() {
        // Given
        memberRepository.save(testMember1);
        entityManager.flush();
        entityManager.clear();

        // When
        List<Member> found = memberRepository.findByName("山田太郎");

        // Then
        assertThat(found).hasSize(1);
        assertEquals("山田太郎", found.get(0).getName());
        assertEquals("yamada@example.com", found.get(0).getEmail());
    }

    @Test
    void testFindByName_WithMultipleMatches() {
        // Given
        Member duplicate = new Member("山田太郎", "やまだたろう", "yamada2@example.com");
        memberRepository.save(testMember1);
        memberRepository.save(duplicate);
        entityManager.flush();
        entityManager.clear();

        // When
        List<Member> found = memberRepository.findByName("山田太郎");

        // Then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Member::getName).containsOnly("山田太郎");
    }

    @Test
    void testFindByName_WithNonExistingName() {
        // Given
        memberRepository.save(testMember1);
        entityManager.flush();
        entityManager.clear();

        // When
        List<Member> found = memberRepository.findByName("存在しない名前");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void testFindByName_ExcludesDeletedMembers() {
        // Given
        memberRepository.save(deletedMember);
        entityManager.flush();
        entityManager.clear();

        // When
        List<Member> found = memberRepository.findByName("佐藤次郎");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void testFindByNameKana_WithExistingNameKana() {
        // Given
        memberRepository.save(testMember1);
        entityManager.flush();
        entityManager.clear();

        // When
        List<Member> found = memberRepository.findByNameKana("やまだたろう");

        // Then
        assertThat(found).hasSize(1);
        assertEquals("やまだたろう", found.get(0).getNameKana());
        assertEquals("山田太郎", found.get(0).getName());
    }

    @Test
    void testFindByNameKana_ExcludesDeletedMembers() {
        // Given
        memberRepository.save(deletedMember);
        entityManager.flush();
        entityManager.clear();

        // When
        List<Member> found = memberRepository.findByNameKana("さとうじろう");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void testFindAll() {
        // Given
        memberRepository.save(testMember1);
        memberRepository.save(testMember2);
        memberRepository.save(deletedMember);
        entityManager.flush();
        entityManager.clear();

        // When
        List<Member> all = memberRepository.findAll();

        // Then - findAll returns all members including deleted ones
        assertThat(all).hasSize(3);
    }

    @Test
    void testFindAllActive() {
        // Given
        memberRepository.save(testMember1);
        memberRepository.save(testMember2);
        memberRepository.save(deletedMember);
        entityManager.flush();
        entityManager.clear();

        // When
        List<Member> active = memberRepository.findAllActive();

        // Then - findAllActive excludes deleted members
        assertThat(active).hasSize(2);
        assertThat(active).extracting(Member::getName)
                .containsExactlyInAnyOrder("山田太郎", "鈴木花子");
        assertThat(active).allMatch(m -> !m.isDeleted());
    }

    @Test
    void testFindAllActive_OrderedByCreatedAtDesc() {
        // Given - save members in sequence
        Member first = memberRepository.save(testMember1);
        entityManager.flush();
        Member second = memberRepository.save(testMember2);
        entityManager.flush();
        entityManager.clear();

        // When
        List<Member> active = memberRepository.findAllActive();

        // Then - should be ordered by createdAt descending (newest first)
        // Since 'second' was saved after 'first', it should appear first in the results
        assertThat(active).hasSize(2);
        assertThat(active).extracting(Member::getId)
                .containsExactly(second.getId(), first.getId());
    }

    @Test
    void testDeleteMember() {
        // Given
        Member saved = memberRepository.save(testMember1);
        entityManager.flush();
        entityManager.clear();

        // When
        memberRepository.deleteById(saved.getId());
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Member> found = memberRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateMember() {
        // Given
        Member saved = memberRepository.save(testMember1);
        entityManager.flush();
        entityManager.clear();

        // When
        Member toUpdate = memberRepository.findById(saved.getId()).orElseThrow();
        toUpdate.updateInfo("山田太郎（更新）", "やまだたろう", "yamada.updated@example.com",
                "シニアエンジニア", "神奈川県", "https://example.com/new.jpg", "更新しました");
        Member updated = memberRepository.save(toUpdate);
        entityManager.flush();
        entityManager.clear();

        // Then
        Member found = memberRepository.findById(updated.getId()).orElseThrow();
        assertEquals("山田太郎（更新）", found.getName());
        assertEquals("yamada.updated@example.com", found.getEmail());
        assertEquals("シニアエンジニア", found.getPosition());
        assertEquals("神奈川県", found.getLocation());
        assertEquals("https://example.com/new.jpg", found.getProfileImageUrl());
        assertEquals("更新しました", found.getSelfIntroduction());
    }

    @Test
    void testLogicalDelete() {
        // Given
        Member saved = memberRepository.save(testMember1);
        Long savedId = saved.getId();
        entityManager.flush();
        entityManager.clear();

        // When - perform logical delete
        Member toDelete = memberRepository.findById(savedId).orElseThrow();
        toDelete.delete();
        memberRepository.save(toDelete);
        entityManager.flush();
        entityManager.clear();

        // Then - member still exists in database but is marked as deleted
        Optional<Member> found = memberRepository.findById(savedId);
        assertTrue(found.isPresent());
        assertTrue(found.get().isDeleted());

        // And - should not appear in active queries
        Optional<Member> activeFound = memberRepository.findActiveById(savedId);
        assertFalse(activeFound.isPresent());

        List<Member> allActive = memberRepository.findAllActive();
        assertThat(allActive).isEmpty();
    }

    @Test
    void testCount() {
        // Given
        memberRepository.save(testMember1);
        memberRepository.save(testMember2);
        memberRepository.save(deletedMember);
        entityManager.flush();

        // When
        long count = memberRepository.count();

        // Then - count includes deleted members
        assertEquals(3, count);
    }

    @Test
    void testExistsById() {
        // Given
        Member saved = memberRepository.save(testMember1);
        entityManager.flush();

        // When/Then
        assertTrue(memberRepository.existsById(saved.getId()));
        assertFalse(memberRepository.existsById(999L));
    }
}
