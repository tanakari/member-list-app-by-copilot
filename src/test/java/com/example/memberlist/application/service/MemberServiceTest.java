package com.example.memberlist.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.memberlist.application.exception.DuplicateEmailException;
import com.example.memberlist.domain.model.Member;
import com.example.memberlist.domain.repository.MemberRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for MemberService.
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    private Validator validator;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        memberService = new MemberService(memberRepository, validator);
    }

    @Test
    void testListMembersReturnsAllActiveMembers() {
        // Given
        Member member1 = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        Member member2 = new Member("鈴木花子", "すずきはなこ", "suzuki@example.com");
        List<Member> expectedMembers = Arrays.asList(member1, member2);
        when(memberRepository.findAll()).thenReturn(expectedMembers);

        // When
        List<Member> actualMembers = memberService.listMembers();

        // Then
        assertEquals(expectedMembers, actualMembers);
        assertEquals(2, actualMembers.size());
        verify(memberRepository).findAll();
    }

    @Test
    void testListMembersReturnsEmptyListWhenNoMembers() {
        // Given
        when(memberRepository.findAll()).thenReturn(List.of());

        // When
        List<Member> actualMembers = memberService.listMembers();

        // Then
        assertNotNull(actualMembers);
        assertTrue(actualMembers.isEmpty());
        verify(memberRepository).findAll();
    }

    @Test
    void testCreateMemberSuccessWithRequiredFieldsOnly() {
        // Given
        String name = "山田太郎";
        String nameKana = "やまだたろう";
        String email = "yamada@example.com";
        Member savedMember = new Member(name, nameKana, email);

        when(memberRepository.existsByEmail(email)).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        Member result = memberService.createMember(name, nameKana, email, null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(nameKana, result.getNameKana());
        assertEquals(email, result.getEmail());
        verify(memberRepository).existsByEmail(email);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testCreateMemberSuccessWithAllFields() {
        // Given
        String name = "山田太郎";
        String nameKana = "やまだたろう";
        String email = "yamada@example.com";
        String position = "エンジニア";
        String location = "東京都";
        String profileImageUrl = "https://example.com/image.jpg";
        String selfIntroduction = "よろしくお願いします";

        Member savedMember = new Member(name, nameKana, email);
        savedMember.updateInfo(name, nameKana, email, position, location, profileImageUrl, selfIntroduction);

        when(memberRepository.existsByEmail(email)).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        Member result = memberService.createMember(name, nameKana, email, position, location,
                profileImageUrl, selfIntroduction);

        // Then
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(nameKana, result.getNameKana());
        assertEquals(email, result.getEmail());
        assertEquals(position, result.getPosition());
        assertEquals(location, result.getLocation());
        assertEquals(profileImageUrl, result.getProfileImageUrl());
        assertEquals(selfIntroduction, result.getSelfIntroduction());
        verify(memberRepository).existsByEmail(email);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testCreateMemberThrowsExceptionWhenEmailAlreadyExists() {
        // Given
        String name = "山田太郎";
        String nameKana = "やまだたろう";
        String email = "yamada@example.com";

        when(memberRepository.existsByEmail(email)).thenReturn(true);

        // When & Then
        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class,
                () -> memberService.createMember(name, nameKana, email, null, null, null, null));

        assertTrue(exception.getMessage().contains(email));
        verify(memberRepository).existsByEmail(email);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesNameIsRequired() {
        // Given
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember(null, "やまだたろう", "yamada@example.com",
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesNameIsNotBlank() {
        // Given
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("", "やまだたろう", "yamada@example.com",
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesNameMaxLength() {
        // Given
        String longName = "あ".repeat(101);
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember(longName, "やまだたろう", "yamada@example.com",
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesNameKanaIsRequired() {
        // Given
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", null, "yamada@example.com",
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesNameKanaIsNotBlank() {
        // Given
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", "", "yamada@example.com",
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesNameKanaIsHiragana() {
        // Given
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", "ヤマダタロウ", "yamada@example.com",
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesNameKanaMaxLength() {
        // Given
        String longNameKana = "あ".repeat(101);
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", longNameKana, "yamada@example.com",
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesEmailIsRequired() {
        // Given
        when(memberRepository.existsByEmail(any())).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", "やまだたろう", null,
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesEmailIsNotBlank() {
        // Given
        when(memberRepository.existsByEmail("")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", "やまだたろう", "",
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesEmailFormat() {
        // Given
        when(memberRepository.existsByEmail("invalid-email")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", "やまだたろう", "invalid-email",
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesEmailMaxLength() {
        // Given
        String longEmail = "a".repeat(250) + "@example.com";
        when(memberRepository.existsByEmail(longEmail)).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", "やまだたろう", longEmail,
                        null, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesPositionMaxLength() {
        // Given
        String longPosition = "あ".repeat(101);
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", "やまだたろう", "yamada@example.com",
                        longPosition, null, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesLocationMaxLength() {
        // Given
        String longLocation = "あ".repeat(201);
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", "やまだたろう", "yamada@example.com",
                        null, longLocation, null, null));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberAcceptsValidPositionLength() {
        // Given
        String validPosition = "あ".repeat(100);
        Member savedMember = new Member("山田太郎", "やまだたろう", "yamada@example.com");

        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        Member result = memberService.createMember("山田太郎", "やまだたろう", "yamada@example.com",
                validPosition, null, null, null);

        // Then
        assertNotNull(result);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testCreateMemberAcceptsValidLocationLength() {
        // Given
        String validLocation = "あ".repeat(200);
        Member savedMember = new Member("山田太郎", "やまだたろう", "yamada@example.com");

        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        Member result = memberService.createMember("山田太郎", "やまだたろう", "yamada@example.com",
                null, validLocation, null, null);

        // Then
        assertNotNull(result);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testCreateMemberValidatesSelfIntroductionMaxLength() {
        // Given
        String longSelfIntroduction = "あ".repeat(1001);
        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> memberService.createMember("山田太郎", "やまだたろう", "yamada@example.com",
                        null, null, null, longSelfIntroduction));

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMemberAcceptsValidSelfIntroductionLength() {
        // Given
        String validSelfIntroduction = "あ".repeat(1000);
        Member savedMember = new Member("山田太郎", "やまだたろう", "yamada@example.com");

        when(memberRepository.existsByEmail("yamada@example.com")).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        Member result = memberService.createMember("山田太郎", "やまだたろう", "yamada@example.com",
                null, null, null, validSelfIntroduction);

        // Then
        assertNotNull(result);
        verify(memberRepository).save(any(Member.class));
    }
}
