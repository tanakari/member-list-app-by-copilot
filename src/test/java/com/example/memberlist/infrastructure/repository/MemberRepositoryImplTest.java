package com.example.memberlist.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.memberlist.domain.model.Member;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for MemberRepositoryImpl.
 */
@ExtendWith(MockitoExtension.class)
class MemberRepositoryImplTest {

    @Mock
    private MemberJpaRepository memberJpaRepository;

    @InjectMocks
    private MemberRepositoryImpl memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member("山田太郎", "やまだたろう", "yamada@example.com");
    }

    @Test
    void testSave() {
        // Given
        when(memberJpaRepository.save(any(Member.class))).thenReturn(testMember);

        // When
        Member savedMember = memberRepository.save(testMember);

        // Then
        assertNotNull(savedMember);
        assertEquals("山田太郎", savedMember.getName());
        verify(memberJpaRepository, times(1)).save(testMember);
    }

    @Test
    void testFindById() {
        // Given
        Long memberId = 1L;
        when(memberJpaRepository.findById(memberId)).thenReturn(Optional.of(testMember));

        // When
        Optional<Member> foundMember = memberRepository.findById(memberId);

        // Then
        assertTrue(foundMember.isPresent());
        assertEquals("山田太郎", foundMember.get().getName());
        verify(memberJpaRepository, times(1)).findById(memberId);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        Long memberId = 999L;
        when(memberJpaRepository.findById(memberId)).thenReturn(Optional.empty());

        // When
        Optional<Member> foundMember = memberRepository.findById(memberId);

        // Then
        assertFalse(foundMember.isPresent());
        verify(memberJpaRepository, times(1)).findById(memberId);
    }

    @Test
    void testFindAll() {
        // Given
        Member member1 = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        Member member2 = new Member("鈴木花子", "すずきはなこ", "suzuki@example.com");
        List<Member> members = Arrays.asList(member1, member2);
        when(memberJpaRepository.findAllActive()).thenReturn(members);

        // When
        List<Member> foundMembers = memberRepository.findAll();

        // Then
        assertNotNull(foundMembers);
        assertEquals(2, foundMembers.size());
        verify(memberJpaRepository, times(1)).findAllActive();
    }

    @Test
    void testFindByEmail() {
        // Given
        String email = "yamada@example.com";
        when(memberJpaRepository.findByEmailAndNotDeleted(email)).thenReturn(Optional.of(testMember));

        // When
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        // Then
        assertTrue(foundMember.isPresent());
        assertEquals("山田太郎", foundMember.get().getName());
        verify(memberJpaRepository, times(1)).findByEmailAndNotDeleted(email);
    }

    @Test
    void testFindByEmailNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(memberJpaRepository.findByEmailAndNotDeleted(email)).thenReturn(Optional.empty());

        // When
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        // Then
        assertFalse(foundMember.isPresent());
        verify(memberJpaRepository, times(1)).findByEmailAndNotDeleted(email);
    }

    @Test
    void testExistsByEmail() {
        // Given
        String email = "yamada@example.com";
        when(memberJpaRepository.existsByEmailAndNotDeleted(email)).thenReturn(true);

        // When
        boolean exists = memberRepository.existsByEmail(email);

        // Then
        assertTrue(exists);
        verify(memberJpaRepository, times(1)).existsByEmailAndNotDeleted(email);
    }

    @Test
    void testExistsByEmailNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(memberJpaRepository.existsByEmailAndNotDeleted(email)).thenReturn(false);

        // When
        boolean exists = memberRepository.existsByEmail(email);

        // Then
        assertFalse(exists);
        verify(memberJpaRepository, times(1)).existsByEmailAndNotDeleted(email);
    }

    @Test
    void testDelete() {
        // Given
        when(memberJpaRepository.save(any(Member.class))).thenReturn(testMember);

        // When
        memberRepository.delete(testMember);

        // Then
        assertTrue(testMember.isDeleted());
        verify(memberJpaRepository, times(1)).save(testMember);
    }
}
