package com.example.jp.memberlist.domain.model;

import java.util.Objects;

/** 名前を表す値オブジェクト */
public class Name {
  private final String value;

  public Name(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("名前は必須です");
    }
    if (value.length() > 100) {
      throw new IllegalArgumentException("名前は100文字以内で入力してください");
    }
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Name name = (Name) o;
    return Objects.equals(value, name.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
