package com.example.jp.memberlist.domain.repository;

import com.example.jp.memberlist.domain.model.Email;
import com.example.jp.memberlist.domain.model.Member;
import java.util.List;
import java.util.Optional;

/** メンバーリポジトリのインターフェース */
public interface MemberRepository {
  /**
   * メンバーを保存する
   *
   * @param member 保存するメンバー
   * @return 保存されたメンバー
   */
  Member save(Member member);

  /**
   * IDでメンバーを検索する
   *
   * @param id メンバーID
   * @return メンバー（存在しない場合はEmpty）
   */
  Optional<Member> findById(Long id);

  /**
   * メールアドレスでメンバーを検索する（削除されていないメンバーのみ）
   *
   * @param email メールアドレス
   * @return メンバー（存在しない場合はEmpty）
   */
  Optional<Member> findByEmail(Email email);

  /**
   * すべてのメンバーを取得する（削除されていないメンバーのみ）
   *
   * @return メンバーのリスト
   */
  List<Member> findAll();

  /**
   * メンバーを削除する
   *
   * @param id メンバーID
   */
  void deleteById(Long id);

  /**
   * メールアドレスが既に存在するか確認する（削除されていないメンバーのみ）
   *
   * @param email メールアドレス
   * @return 存在する場合true
   */
  boolean existsByEmail(Email email);
}
