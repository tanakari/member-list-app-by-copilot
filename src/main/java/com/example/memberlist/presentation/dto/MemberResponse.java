package com.example.memberlist.presentation.dto;

import com.example.memberlist.domain.model.Member;
import java.time.LocalDateTime;

/**
 * Response DTO for member information.
 * Represents a single member in API responses.
 */
public record MemberResponse(
        Long id,
        String name,
        String nameKana,
        String email,
        String position,
        String location,
        String profileImageUrl,
        String selfIntroduction,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Creates a MemberResponse from a Member entity.
     *
     * @param member the member entity
     * @return the member response DTO
     */
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getNameKana(),
                member.getEmail(),
                member.getPosition(),
                member.getLocation(),
                member.getProfileImageUrl(),
                member.getSelfIntroduction(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
