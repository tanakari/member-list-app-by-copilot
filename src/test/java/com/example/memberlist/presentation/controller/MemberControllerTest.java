package com.example.memberlist.presentation.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memberlist.application.exception.DuplicateEmailException;
import com.example.memberlist.application.service.MemberService;
import com.example.memberlist.domain.model.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit tests for MemberController.
 * Tests all endpoints and error handling scenarios.
 */
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    void testListMembersReturnsEmptyList() throws Exception {
        // Given
        when(memberService.listMembers()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("メンバー一覧の取得が完了しました"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(memberService).listMembers();
    }

    @Test
    void testListMembersReturnsMultipleMembers() throws Exception {
        // Given
        Member member1 = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        Member member2 = new Member("鈴木花子", "すずきはなこ", "suzuki@example.com");
        when(memberService.listMembers()).thenReturn(Arrays.asList(member1, member2));

        // When & Then
        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("メンバー一覧の取得が完了しました"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("山田太郎"))
                .andExpect(jsonPath("$.data[0].nameKana").value("やまだたろう"))
                .andExpect(jsonPath("$.data[0].email").value("yamada@example.com"))
                .andExpect(jsonPath("$.data[1].name").value("鈴木花子"))
                .andExpect(jsonPath("$.data[1].nameKana").value("すずきはなこ"))
                .andExpect(jsonPath("$.data[1].email").value("suzuki@example.com"));

        verify(memberService).listMembers();
    }

    @Test
    void testListMembersWithAllFields() throws Exception {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        member.updateInfo("山田太郎", "やまだたろう", "yamada@example.com",
                "エンジニア", "東京都", "https://example.com/image.jpg", "よろしくお願いします");
        when(memberService.listMembers()).thenReturn(List.of(member));

        // When & Then
        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data[0].name").value("山田太郎"))
                .andExpect(jsonPath("$.data[0].position").value("エンジニア"))
                .andExpect(jsonPath("$.data[0].location").value("東京都"))
                .andExpect(jsonPath("$.data[0].profileImageUrl").value("https://example.com/image.jpg"))
                .andExpect(jsonPath("$.data[0].selfIntroduction").value("よろしくお願いします"));

        verify(memberService).listMembers();
    }

    @Test
    void testCreateMemberWithRequiredFieldsOnly() throws Exception {
        // Given
        String requestBody = """
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com"
                }
                """;

        Member savedMember = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        when(memberService.createMember(
                "山田太郎", "やまだたろう", "yamada@example.com",
                null, null, null, null))
                .thenReturn(savedMember);

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("登録が完了しました"))
                .andExpect(jsonPath("$.data.name").value("山田太郎"))
                .andExpect(jsonPath("$.data.nameKana").value("やまだたろう"))
                .andExpect(jsonPath("$.data.email").value("yamada@example.com"));

        verify(memberService).createMember(
                "山田太郎", "やまだたろう", "yamada@example.com",
                null, null, null, null);
    }

    @Test
    void testCreateMemberWithAllFields() throws Exception {
        // Given
        String requestBody = """
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com",
                    "position": "エンジニア",
                    "location": "東京都",
                    "profileImageUrl": "https://example.com/image.jpg",
                    "selfIntroduction": "よろしくお願いします"
                }
                """;

        Member savedMember = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        savedMember.updateInfo("山田太郎", "やまだたろう", "yamada@example.com",
                "エンジニア", "東京都", "https://example.com/image.jpg", "よろしくお願いします");
        when(memberService.createMember(
                "山田太郎", "やまだたろう", "yamada@example.com",
                "エンジニア", "東京都", "https://example.com/image.jpg", "よろしくお願いします"))
                .thenReturn(savedMember);

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("登録が完了しました"))
                .andExpect(jsonPath("$.data.name").value("山田太郎"))
                .andExpect(jsonPath("$.data.nameKana").value("やまだたろう"))
                .andExpect(jsonPath("$.data.email").value("yamada@example.com"))
                .andExpect(jsonPath("$.data.position").value("エンジニア"))
                .andExpect(jsonPath("$.data.location").value("東京都"))
                .andExpect(jsonPath("$.data.profileImageUrl").value("https://example.com/image.jpg"))
                .andExpect(jsonPath("$.data.selfIntroduction").value("よろしくお願いします"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenNameIsMissing() throws Exception {
        // Given
        String requestBody = """
                {
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("名前は必須です"));

        verify(memberService, never()).createMember(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testCreateMemberReturnsErrorWhenNameIsBlank() throws Exception {
        // Given
        String requestBody = """
                {
                    "name": "",
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void testCreateMemberReturnsErrorWhenNameExceedsMaxLength() throws Exception {
        // Given
        String longName = "あ".repeat(101);
        String requestBody = String.format("""
                {
                    "name": "%s",
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com"
                }
                """, longName);

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors[0]").value("名前は100文字以内で入力してください"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenNameKanaIsMissing() throws Exception {
        // Given
        String requestBody = """
                {
                    "name": "山田太郎",
                    "email": "yamada@example.com"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors[0]").value("読み仮名は必須です"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenNameKanaIsNotHiragana() throws Exception {
        // Given
        String requestBody = """
                {
                    "name": "山田太郎",
                    "nameKana": "ヤマダタロウ",
                    "email": "yamada@example.com"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors[0]").value("読み仮名はひらがなで入力してください"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenNameKanaExceedsMaxLength() throws Exception {
        // Given
        String longNameKana = "あ".repeat(101);
        String requestBody = String.format("""
                {
                    "name": "山田太郎",
                    "nameKana": "%s",
                    "email": "yamada@example.com"
                }
                """, longNameKana);

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenEmailIsMissing() throws Exception {
        // Given
        String requestBody = """
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors[0]").value("メールアドレスは必須です"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenEmailIsInvalid() throws Exception {
        // Given
        String requestBody = """
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう",
                    "email": "invalid-email"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors[0]").value("有効なメールアドレスを入力してください"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenEmailExceedsMaxLength() throws Exception {
        // Given
        String longEmail = "a".repeat(250) + "@example.com";
        String requestBody = String.format("""
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう",
                    "email": "%s"
                }
                """, longEmail);

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenPositionExceedsMaxLength() throws Exception {
        // Given
        String longPosition = "あ".repeat(101);
        String requestBody = String.format("""
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com",
                    "position": "%s"
                }
                """, longPosition);

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors[0]").value("役職は100文字以内で入力してください"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenLocationExceedsMaxLength() throws Exception {
        // Given
        String longLocation = "あ".repeat(201);
        String requestBody = String.format("""
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com",
                    "location": "%s"
                }
                """, longLocation);

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors[0]").value("所在地は200文字以内で入力してください"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenSelfIntroductionExceedsMaxLength() throws Exception {
        // Given
        String longSelfIntroduction = "あ".repeat(1001);
        String requestBody = String.format("""
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com",
                    "selfIntroduction": "%s"
                }
                """, longSelfIntroduction);

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors[0]").value("自己紹介は1000文字以内で入力してください"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenEmailAlreadyExists() throws Exception {
        // Given
        String requestBody = """
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com"
                }
                """;

        when(memberService.createMember(
                "山田太郎", "やまだたろう", "yamada@example.com",
                null, null, null, null))
                .thenThrow(new DuplicateEmailException("メールアドレスが既に登録されています"));

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("メールアドレスが既に登録されています"));
    }

    @Test
    void testCreateMemberReturnsErrorWhenServiceThrowsIllegalArgumentException() throws Exception {
        // Given
        String requestBody = """
                {
                    "name": "山田太郎",
                    "nameKana": "やまだたろう",
                    "email": "yamada@example.com"
                }
                """;

        when(memberService.createMember(
                "山田太郎", "やまだたろう", "yamada@example.com",
                null, null, null, null))
                .thenThrow(new IllegalArgumentException("バリデーションに失敗しました"));

        // When & Then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("バリデーションエラーです"))
                .andExpect(jsonPath("$.errors[0]").value("バリデーションに失敗しました"));
    }

    @Test
    void testListMembersReturnsErrorOnServerException() throws Exception {
        // Given
        when(memberService.listMembers()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/members"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("サーバーエラーが発生しました"));
    }
}
