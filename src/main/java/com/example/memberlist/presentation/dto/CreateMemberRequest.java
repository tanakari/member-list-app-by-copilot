package com.example.memberlist.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new member.
 * Contains validation constraints matching the API specification.
 */
public record CreateMemberRequest(
        @NotBlank(message = "名前は必須です")
        @Size(max = 100, message = "名前は100文字以内で入力してください")
        String name,

        @NotBlank(message = "読み仮名は必須です")
        @Size(max = 100, message = "読み仮名は100文字以内で入力してください")
        @Pattern(regexp = "^[ぁ-ん]+$", message = "読み仮名はひらがなで入力してください")
        String nameKana,

        @NotBlank(message = "メールアドレスは必須です")
        @Email(message = "有効なメールアドレスを入力してください")
        @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
        String email,

        @Size(max = 100, message = "役職は100文字以内で入力してください")
        String position,

        @Size(max = 200, message = "所在地は200文字以内で入力してください")
        String location,

        String profileImageUrl,

        @Size(max = 1000, message = "自己紹介は1000文字以内で入力してください")
        String selfIntroduction
) {
}
