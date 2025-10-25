package com.example.jp.memberlist.domain.service;

import com.example.jp.memberlist.domain.model.Email;
import com.example.jp.memberlist.domain.repository.MemberRepository;

/** メンバードメインサービス */
public class MemberDomainService {
  private final MemberRepository memberRepository;

  public MemberDomainService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  /**
   * メールアドレスが重複しているか確認する
   *
   * @param email メールアドレス
   * @return 重複している場合true
   */
  public boolean isDuplicateEmail(Email email) {
    return memberRepository.existsByEmail(email);
  }

  /**
   * メールアドレスの重複をチェックする
   *
   * @param email メールアドレス
   * @throws IllegalStateException メールアドレスが既に登録されている場合
   */
  public void checkEmailDuplication(Email email) {
    if (isDuplicateEmail(email)) {
      throw new IllegalStateException("メールアドレスが既に登録されています");
    }
  }
}
