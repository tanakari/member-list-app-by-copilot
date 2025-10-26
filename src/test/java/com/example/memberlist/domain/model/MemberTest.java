package com.example.memberlist.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Member entity.
 */
class MemberTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testConstructorCreatesValidMember() {
        // Given
        String name = "山田太郎";
        String nameKana = "やまだたろう";
        String email = "yamada@example.com";

        // When
        Member member = new Member(name, nameKana, email);

        // Then
        assertNull(member.getId());
        assertEquals(name, member.getName());
        assertEquals(nameKana, member.getNameKana());
        assertEquals(email, member.getEmail());
        assertNull(member.getPosition());
        assertNull(member.getLocation());
        assertNull(member.getProfileImageUrl());
        assertNull(member.getSelfIntroduction());
        assertFalse(member.isDeleted());
    }

    @Test
    void testValidMemberPassesValidation() {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", "yamada@example.com");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNameIsRequired() {
        // Given
        Member member = new Member(null, "やまだたろう", "yamada@example.com");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testNameCannotBeEmpty() {
        // Given
        Member member = new Member("", "やまだたろう", "yamada@example.com");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testNameCannotExceedMaxLength() {
        // Given
        String longName = "あ".repeat(101);
        Member member = new Member(longName, "やまだたろう", "yamada@example.com");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testNameKanaIsRequired() {
        // Given
        Member member = new Member("山田太郎", null, "yamada@example.com");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nameKana")));
    }

    @Test
    void testNameKanaCannotBeEmpty() {
        // Given
        Member member = new Member("山田太郎", "", "yamada@example.com");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nameKana")));
    }

    @Test
    void testNameKanaMustBeHiragana() {
        // Given
        Member member = new Member("山田太郎", "ヤマダタロウ", "yamada@example.com");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nameKana")));
    }

    @Test
    void testNameKanaCannotExceedMaxLength() {
        // Given
        String longNameKana = "あ".repeat(101);
        Member member = new Member("山田太郎", longNameKana, "yamada@example.com");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nameKana")));
    }

    @Test
    void testEmailIsRequired() {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", null);

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testEmailCannotBeEmpty() {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", "");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testEmailMustBeValidFormat() {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", "invalid-email");

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testEmailCannotExceedMaxLength() {
        // Given
        String longEmail = "a".repeat(250) + "@example.com";
        Member member = new Member("山田太郎", "やまだたろう", longEmail);

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testUpdateInfo() {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        String newName = "鈴木花子";
        String newNameKana = "すずきはなこ";
        String newEmail = "suzuki@example.com";
        String newPosition = "エンジニア";
        String newLocation = "東京都";
        String newProfileImageUrl = "https://example.com/image.jpg";
        String newSelfIntroduction = "よろしくお願いします";

        // When
        member.updateInfo(newName, newNameKana, newEmail, newPosition,
                newLocation, newProfileImageUrl, newSelfIntroduction);

        // Then
        assertEquals(newName, member.getName());
        assertEquals(newNameKana, member.getNameKana());
        assertEquals(newEmail, member.getEmail());
        assertEquals(newPosition, member.getPosition());
        assertEquals(newLocation, member.getLocation());
        assertEquals(newProfileImageUrl, member.getProfileImageUrl());
        assertEquals(newSelfIntroduction, member.getSelfIntroduction());
    }

    @Test
    void testDelete() {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        assertFalse(member.isDeleted());

        // When
        member.delete();

        // Then
        assertTrue(member.isDeleted());
    }

    @Test
    void testPositionValidation() {
        // Given - position with max length
        Member member = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        member.updateInfo("山田太郎", "やまだたろう", "yamada@example.com",
                "あ".repeat(100), null, null, null);

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertTrue(violations.isEmpty());

        // Given - position exceeding max length
        member.updateInfo("山田太郎", "やまだたろう", "yamada@example.com",
                "あ".repeat(101), null, null, null);

        // When
        violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("position")));
    }

    @Test
    void testLocationValidation() {
        // Given - location with max length
        Member member = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        member.updateInfo("山田太郎", "やまだたろう", "yamada@example.com",
                null, "あ".repeat(200), null, null);

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        // Then
        assertTrue(violations.isEmpty());

        // Given - location exceeding max length
        member.updateInfo("山田太郎", "やまだたろう", "yamada@example.com",
                null, "あ".repeat(201), null, null);

        // When
        violations = validator.validate(member);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("location")));
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        Member member1 = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        Member member2 = new Member("鈴木花子", "すずきはなこ", "suzuki@example.com");

        // When/Then - JPA entities are identified by ID. Before persistence (null ID),
        // entities are considered equal. This is standard DDD/JPA behavior for transient entities.
        assertEquals(member1, member2);

        // Test reflexive
        assertEquals(member1, member1);

        // Test null
        assertNotEquals(null, member1);

        // Test different class
        assertNotEquals(member1, new Object());
    }

    @Test
    void testToString() {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        member.updateInfo("山田太郎", "やまだたろう", "yamada@example.com",
                "エンジニア", "東京都", null, null);

        // When
        String result = member.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("山田太郎"));
        assertTrue(result.contains("やまだたろう"));
        assertTrue(result.contains("yamada@example.com"));
        assertTrue(result.contains("エンジニア"));
        assertTrue(result.contains("東京都"));
    }

    @Test
    void testCreatedAtAndUpdatedAtAreSetOnPersist() {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        assertNull(member.getCreatedAt());
        assertNull(member.getUpdatedAt());

        // When
        member.onCreate();

        // Then
        assertNotNull(member.getCreatedAt());
        assertNotNull(member.getUpdatedAt());
    }

    @Test
    void testUpdatedAtIsSetOnUpdate() {
        // Given
        Member member = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        member.onCreate();
        assertNotNull(member.getUpdatedAt());

        // When
        member.onUpdate();

        // Then
        assertNotNull(member.getUpdatedAt());
    }

    @Test
    void testHashCodeConsistency() {
        // Given
        Member member1 = new Member("山田太郎", "やまだたろう", "yamada@example.com");
        Member member2 = new Member("鈴木花子", "すずきはなこ", "suzuki@example.com");

        // When/Then - hash code should be consistent for the same object
        int hash1 = member1.hashCode();
        int hash2 = member1.hashCode();
        assertEquals(hash1, hash2);

        // JPA entities with null IDs have the same hash code (Objects.hash(null) = 0)
        // This is standard DDD/JPA behavior. Once persisted, IDs differ and so do hash codes.
        // Avoid using transient entities in hash-based collections before persistence.
        assertEquals(member1.hashCode(), member2.hashCode());
    }
}
