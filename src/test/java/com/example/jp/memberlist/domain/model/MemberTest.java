package com.example.jp.memberlist.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MemberTest {

  @Test
  void testCreateMember() {
    Name name = new Name("山田太郎");
    NameKana nameKana = new NameKana("やまだたろう");
    Email email = new Email("yamada@example.com");
    Position position = new Position("エンジニア");
    Location location = new Location("東京都");

    Member member =
        new Member(
            name, nameKana, email, position, location, "https://example.com/image.jpg", "自己紹介");

    assertEquals("山田太郎", member.getName().getValue());
    assertEquals("やまだたろう", member.getNameKana().getValue());
    assertEquals("yamada@example.com", member.getEmail().getValue());
    assertEquals("エンジニア", member.getPosition().getValue());
    assertEquals("東京都", member.getLocation().getValue());
    assertFalse(member.isDeleted());
  }

  @Test
  void testUpdateMember() {
    Member member =
        new Member(
            new Name("山田太郎"),
            new NameKana("やまだたろう"),
            new Email("yamada@example.com"),
            null,
            null,
            null,
            null);

    member.update(
        new Name("田中花子"),
        new NameKana("たなかはなこ"),
        new Email("tanaka@example.com"),
        new Position("マネージャー"),
        new Location("大阪府"),
        null,
        "よろしく");

    assertEquals("田中花子", member.getName().getValue());
    assertEquals("たなかはなこ", member.getNameKana().getValue());
    assertEquals("tanaka@example.com", member.getEmail().getValue());
    assertEquals("マネージャー", member.getPosition().getValue());
    assertEquals("大阪府", member.getLocation().getValue());
    assertEquals("よろしく", member.getSelfIntroduction());
  }

  @Test
  void testDeleteMember() {
    Member member =
        new Member(
            new Name("山田太郎"),
            new NameKana("やまだたろう"),
            new Email("yamada@example.com"),
            null,
            null,
            null,
            null);

    assertFalse(member.isDeleted());
    member.delete();
    assertTrue(member.isDeleted());
  }
}
