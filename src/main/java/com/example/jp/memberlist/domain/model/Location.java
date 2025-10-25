package com.example.jp.memberlist.domain.model;

import java.util.Objects;

/** 所在地を表す値オブジェクト */
public class Location {
  private final String value;

  public Location(String value) {
    if (value != null && value.length() > 200) {
      throw new IllegalArgumentException("所在地は200文字以内で入力してください");
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
    Location location = (Location) o;
    return Objects.equals(value, location.value);
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
