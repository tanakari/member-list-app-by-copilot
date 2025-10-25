package com.example.jp.memberlist.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** メンバーJPAリポジトリ */
@Repository
public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {
  /** メールアドレスで検索（削除されていないメンバーのみ） */
  Optional<MemberJpaEntity> findByEmailAndIsDeletedFalse(String email);

  /** すべてのメンバーを取得（削除されていないメンバーのみ） */
  List<MemberJpaEntity> findByIsDeletedFalseOrderByCreatedAtDesc();

  /** メールアドレスが存在するか確認（削除されていないメンバーのみ） */
  boolean existsByEmailAndIsDeletedFalse(String email);
}
