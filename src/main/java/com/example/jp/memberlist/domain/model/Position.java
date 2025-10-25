package com.example.jp.memberlist.domain.model;

import java.util.Objects;

/** 役職を表す値オブジェクト */
public class Position {
  private final String value;

  public Position(String value) {
    if (value != null && value.length() > 100) {
      throw new IllegalArgumentException("役職は100文字以内で入力してください");
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
    Position position = (Position) o;
    return Objects.equals(value, position.value);
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
