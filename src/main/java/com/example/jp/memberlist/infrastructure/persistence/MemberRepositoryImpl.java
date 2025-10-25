package com.example.jp.memberlist.infrastructure.persistence;

import com.example.jp.memberlist.domain.model.*;
import com.example.jp.memberlist.domain.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

/** メンバーリポジトリの実装 */
@Repository
public class MemberRepositoryImpl implements MemberRepository {
  private final MemberJpaRepository jpaRepository;

  public MemberRepositoryImpl(MemberJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Member save(Member member) {
    MemberJpaEntity entity = toEntity(member);
    MemberJpaEntity saved = jpaRepository.save(entity);
    return toDomain(saved);
  }

  @Override
  public Optional<Member> findById(Long id) {
    return jpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public Optional<Member> findByEmail(Email email) {
    return jpaRepository.findByEmailAndIsDeletedFalse(email.getValue()).map(this::toDomain);
  }

  @Override
  public List<Member> findAll() {
    return jpaRepository.findByIsDeletedFalseOrderByCreatedAtDesc().stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository
        .findById(id)
        .ifPresent(
            entity -> {
              entity.setDeleted(true);
              jpaRepository.save(entity);
            });
  }

  @Override
  public boolean existsByEmail(Email email) {
    return jpaRepository.existsByEmailAndIsDeletedFalse(email.getValue());
  }

  /** ドメインモデルをJPAエンティティに変換 */
  private MemberJpaEntity toEntity(Member member) {
    return new MemberJpaEntity(
        member.getId(),
        member.getName().getValue(),
        member.getNameKana().getValue(),
        member.getEmail().getValue(),
        member.getPosition() != null ? member.getPosition().getValue() : null,
        member.getLocation() != null ? member.getLocation().getValue() : null,
        member.getProfileImageUrl(),
        member.getSelfIntroduction(),
        member.getCreatedAt(),
        member.getUpdatedAt(),
        member.isDeleted());
  }

  /** JPAエンティティをドメインモデルに変換 */
  private Member toDomain(MemberJpaEntity entity) {
    return new Member(
        entity.getId(),
        new Name(entity.getName()),
        new NameKana(entity.getNameKana()),
        new Email(entity.getEmail()),
        entity.getPosition() != null ? new Position(entity.getPosition()) : null,
        entity.getLocation() != null ? new Location(entity.getLocation()) : null,
        entity.getProfileImageUrl(),
        entity.getSelfIntroduction(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.isDeleted());
  }
}
