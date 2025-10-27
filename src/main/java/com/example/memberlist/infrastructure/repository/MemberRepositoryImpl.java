package com.example.memberlist.infrastructure.repository;

import com.example.memberlist.domain.model.Member;
import com.example.memberlist.domain.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Implementation of MemberRepository using Spring Data JPA.
 * Bridges the domain repository interface with JPA repository.
 */
@Component
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public MemberRepositoryImpl(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id);
    }

    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAllActive();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmailAndNotDeleted(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmailAndNotDeleted(email);
    }

    @Override
    public void delete(Member member) {
        member.delete();
        memberJpaRepository.save(member);
    }
}
