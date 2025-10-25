package com.example.jp.memberlist.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/** メンバーJPAエンティティ */
@Entity
@Table(name = "members")
public class MemberJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "name_kana", nullable = false, length = 100)
  private String nameKana;

  @Column(name = "email", nullable = false, length = 255, unique = true)
  private String email;

  @Column(name = "position", length = 100)
  private String position;

  @Column(name = "location", length = 200)
  private String location;

  @Column(name = "profile_image_url", columnDefinition = "TEXT")
  private String profileImageUrl;

  @Column(name = "self_introduction", columnDefinition = "TEXT")
  private String selfIntroduction;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public MemberJpaEntity() {}

  public MemberJpaEntity(
      Long id,
      String name,
      String nameKana,
      String email,
      String position,
      String location,
      String profileImageUrl,
      String selfIntroduction,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      boolean isDeleted) {
    this.id = id;
    this.name = name;
    this.nameKana = nameKana;
    this.email = email;
    this.position = position;
    this.location = location;
    this.profileImageUrl = profileImageUrl;
    this.selfIntroduction = selfIntroduction;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.isDeleted = isDeleted;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameKana() {
    return nameKana;
  }

  public void setNameKana(String nameKana) {
    this.nameKana = nameKana;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public String getSelfIntroduction() {
    return selfIntroduction;
  }

  public void setSelfIntroduction(String selfIntroduction) {
    this.selfIntroduction = selfIntroduction;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MemberJpaEntity that = (MemberJpaEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
