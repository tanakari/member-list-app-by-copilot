package com.example.memberlist.application.service;

import com.example.memberlist.application.exception.DuplicateEmailException;
import com.example.memberlist.domain.model.Member;
import com.example.memberlist.domain.repository.MemberRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for member management.
 * Handles business logic for member operations including listing and creating members.
 */
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final Validator validator;

    public MemberService(MemberRepository memberRepository, Validator validator) {
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    /**
     * Retrieves all active members.
     *
     * @return list of all active members
     */
    @Transactional(readOnly = true)
    public List<Member> listMembers() {
        return memberRepository.findAll();
    }

    /**
     * Creates a new member with the provided information.
     * Validates that the email address is not already registered.
     *
     * @param name member's name
     * @param nameKana member's name in hiragana
     * @param email member's email address
     * @param position member's position (optional)
     * @param location member's location (optional)
     * @param profileImageUrl member's profile image URL (optional)
     * @param selfIntroduction member's self introduction (optional)
     * @return the created member
     * @throws DuplicateEmailException if the email is already registered
     * @throws IllegalArgumentException if validation fails
     */
    public Member createMember(
            String name,
            String nameKana,
            String email,
            String position,
            String location,
            String profileImageUrl,
            String selfIntroduction) {

        // Check for duplicate email
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("メールアドレスが既に登録されています: " + email);
        }

        // Create new member
        Member member = new Member(name, nameKana, email);
        member.updateInfo(name, nameKana, email, position, location, profileImageUrl, selfIntroduction);

        // Validate member
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(errorMessage);
        }

        // Save and return
        return memberRepository.save(member);
    }
}
