package com.example.memberlist.presentation.controller;

import com.example.memberlist.application.service.MemberService;
import com.example.memberlist.domain.model.Member;
import com.example.memberlist.presentation.dto.ApiResponse;
import com.example.memberlist.presentation.dto.CreateMemberRequest;
import com.example.memberlist.presentation.dto.MemberResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for member management API.
 * Handles member list retrieval and creation endpoints.
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Retrieves all active members.
     * GET /api/members
     *
     * @return response containing list of all active members
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponse>>> listMembers() {
        List<Member> members = memberService.listMembers();
        List<MemberResponse> memberResponses = members.stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());

        ApiResponse<List<MemberResponse>> response = ApiResponse.success(
                "メンバー一覧の取得が完了しました",
                memberResponses
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new member.
     * POST /api/members
     *
     * @param request the member creation request with validation
     * @return response containing the created member information
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(
            @Valid @RequestBody CreateMemberRequest request) {

        Member member = memberService.createMember(
                request.name(),
                request.nameKana(),
                request.email(),
                request.position(),
                request.location(),
                request.profileImageUrl(),
                request.selfIntroduction()
        );

        MemberResponse memberResponse = MemberResponse.from(member);
        ApiResponse<MemberResponse> response = ApiResponse.success(
                "登録が完了しました",
                memberResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
