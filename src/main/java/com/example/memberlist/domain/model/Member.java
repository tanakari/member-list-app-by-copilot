package com.example.memberlist.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Member entity representing a member in the system.
 * Aggregate root in DDD context.
 */
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "名前は必須です")
    @Size(max = 100, message = "名前は100文字以内で入力してください")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "読み仮名は必須です")
    @Size(max = 100, message = "読み仮名は100文字以内で入力してください")
    @Pattern(regexp = "^[ぁ-ん]+$", message = "読み仮名はひらがなで入力してください")
    @Column(name = "name_kana", nullable = false, length = 100)
    private String nameKana;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "有効なメールアドレスを入力してください")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Size(max = 100, message = "役職は100文字以内で入力してください")
    @Column(name = "position", length = 100)
    private String position;

    @Size(max = 200, message = "所在地は200文字以内で入力してください")
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
    private Boolean isDeleted = false;

    /**
     * Default constructor for JPA.
     */
    protected Member() {
    }

    /**
     * Constructor for creating a new member.
     *
     * @param name name of the member
     * @param nameKana kana reading of the name (hiragana only)
     * @param email email address
     */
    public Member(String name, String nameKana, String email) {
        this.name = name;
        this.nameKana = nameKana;
        this.email = email;
        this.isDeleted = false;
    }

    /**
     * Sets timestamps before persisting.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates timestamp before updating.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates member information.
     *
     * @param name name of the member
     * @param nameKana kana reading of the name
     * @param email email address
     * @param position position of the member
     * @param location location of the member
     * @param profileImageUrl profile image URL
     * @param selfIntroduction self introduction
     */
    public void updateInfo(String name, String nameKana, String email, String position,
            String location, String profileImageUrl, String selfIntroduction) {
        this.name = name;
        this.nameKana = nameKana;
        this.email = email;
        this.position = position;
        this.location = location;
        this.profileImageUrl = profileImageUrl;
        this.selfIntroduction = selfIntroduction;
    }

    /**
     * Performs logical deletion.
     */
    public void delete() {
        this.isDeleted = true;
    }

    /**
     * Checks if member is deleted.
     *
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return this.isDeleted;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameKana() {
        return nameKana;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getLocation() {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameKana='" + nameKana + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", location='" + location + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
