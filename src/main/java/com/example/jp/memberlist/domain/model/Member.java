package com.example.jp.memberlist.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/** メンバーを表すドメインエンティティ */
public class Member {
  private final Long id;
  private Name name;
  private NameKana nameKana;
  private Email email;
  private Position position;
  private Location location;
  private String profileImageUrl;
  private String selfIntroduction;
  private final LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean deleted;

  /** 新規メンバーを作成 */
  public Member(
      Name name,
      NameKana nameKana,
      Email email,
      Position position,
      Location location,
      String profileImageUrl,
      String selfIntroduction) {
    this(
        null,
        name,
        nameKana,
        email,
        position,
        location,
        profileImageUrl,
        selfIntroduction,
        LocalDateTime.now(),
        LocalDateTime.now(),
        false);
  }

  /** 既存メンバーを再構築 */
  public Member(
      Long id,
      Name name,
      NameKana nameKana,
      Email email,
      Position position,
      Location location,
      String profileImageUrl,
      String selfIntroduction,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      boolean deleted) {
    this.id = id;
    this.name = Objects.requireNonNull(name, "名前は必須です");
    this.nameKana = Objects.requireNonNull(nameKana, "読み仮名は必須です");
    this.email = Objects.requireNonNull(email, "メールアドレスは必須です");
    this.position = position;
    this.location = location;
    this.profileImageUrl = validateProfileImageUrl(profileImageUrl);
    this.selfIntroduction = validateSelfIntroduction(selfIntroduction);
    this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    this.deleted = deleted;
  }

  /** メンバー情報を更新 */
  public void update(
      Name name,
      NameKana nameKana,
      Email email,
      Position position,
      Location location,
      String profileImageUrl,
      String selfIntroduction) {
    this.name = Objects.requireNonNull(name, "名前は必須です");
    this.nameKana = Objects.requireNonNull(nameKana, "読み仮名は必須です");
    this.email = Objects.requireNonNull(email, "メールアドレスは必須です");
    this.position = position;
    this.location = location;
    this.profileImageUrl = validateProfileImageUrl(profileImageUrl);
    this.selfIntroduction = validateSelfIntroduction(selfIntroduction);
    this.updatedAt = LocalDateTime.now();
  }

  /** メンバーを論理削除 */
  public void delete() {
    this.deleted = true;
    this.updatedAt = LocalDateTime.now();
  }

  private String validateProfileImageUrl(String url) {
    if (url != null && url.length() > 2048) {
      throw new IllegalArgumentException("プロフィール写真URLは2048文字以内で入力してください");
    }
    return url;
  }

  private String validateSelfIntroduction(String introduction) {
    if (introduction != null && introduction.length() > 1000) {
      throw new IllegalArgumentException("自己紹介は1000文字以内で入力してください");
    }
    return introduction;
  }

  public boolean isDeleted() {
    return deleted;
  }

  // Getters
  public Long getId() {
    return id;
  }

  public Name getName() {
    return name;
  }

  public NameKana getNameKana() {
    return nameKana;
  }

  public Email getEmail() {
    return email;
  }

  public Position getPosition() {
    return position;
  }

  public Location getLocation() {
    return location;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public String getSelfIntroduction() {
    return selfIntroduction;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Member member = (Member) o;
    return Objects.equals(id, member.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
