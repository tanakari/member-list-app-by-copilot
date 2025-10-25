package com.example.jp.memberlist.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.example.jp.memberlist.domain.model.*;
import com.example.jp.memberlist.domain.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class MemberRepositoryImplTest {

  @Autowired private MemberRepository memberRepository;

  @Test
  void testSaveAndFindById() {
    Member member =
        new Member(
            new Name("山田太郎"),
            new NameKana("やまだたろう"),
            new Email("yamada@example.com"),
            new Position("エンジニア"),
            new Location("東京都"),
            "https://example.com/image.jpg",
            "自己紹介");

    Member saved = memberRepository.save(member);

    assertNotNull(saved.getId());
    assertEquals("山田太郎", saved.getName().getValue());

    Optional<Member> found = memberRepository.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals("yamada@example.com", found.get().getEmail().getValue());
  }

  @Test
  void testFindByEmail() {
    Member member =
        new Member(
            new Name("田中花子"),
            new NameKana("たなかはなこ"),
            new Email("tanaka@example.com"),
            null,
            null,
            null,
            null);

    memberRepository.save(member);

    Optional<Member> found = memberRepository.findByEmail(new Email("tanaka@example.com"));
    assertTrue(found.isPresent());
    assertEquals("田中花子", found.get().getName().getValue());
  }

  @Test
  void testFindAll() {
    Member member1 =
        new Member(
            new Name("山田太郎"),
            new NameKana("やまだたろう"),
            new Email("yamada1@example.com"),
            null,
            null,
            null,
            null);

    Member member2 =
        new Member(
            new Name("田中花子"),
            new NameKana("たなかはなこ"),
            new Email("tanaka1@example.com"),
            null,
            null,
            null,
            null);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> members = memberRepository.findAll();
    assertTrue(members.size() >= 2);
  }

  @Test
  void testExistsByEmail() {
    Member member =
        new Member(
            new Name("山田太郎"),
            new NameKana("やまだたろう"),
            new Email("yamada2@example.com"),
            null,
            null,
            null,
            null);

    memberRepository.save(member);

    assertTrue(memberRepository.existsByEmail(new Email("yamada2@example.com")));
    assertFalse(memberRepository.existsByEmail(new Email("notexist@example.com")));
  }

  @Test
  void testLogicalDelete() {
    Member member =
        new Member(
            new Name("山田太郎"),
            new NameKana("やまだたろう"),
            new Email("yamada3@example.com"),
            null,
            null,
            null,
            null);

    Member saved = memberRepository.save(member);
    Long id = saved.getId();

    memberRepository.deleteById(id);

    Optional<Member> found = memberRepository.findByEmail(new Email("yamada3@example.com"));
    assertFalse(found.isPresent());

    Optional<Member> foundById = memberRepository.findById(id);
    assertTrue(foundById.isPresent());
    assertTrue(foundById.get().isDeleted());
  }
}
