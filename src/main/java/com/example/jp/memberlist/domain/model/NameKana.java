package com.example.jp.memberlist.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/** 読み仮名を表す値オブジェクト */
public class NameKana {
  private static final Pattern HIRAGANA_PATTERN = Pattern.compile("^[\\u3040-\\u309F\\s]+$");

  private final String value;

  public NameKana(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("読み仮名は必須です");
    }
    if (value.length() > 100) {
      throw new IllegalArgumentException("読み仮名は100文字以内で入力してください");
    }
    if (!HIRAGANA_PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException("読み仮名はひらがなで入力してください");
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
    NameKana nameKana = (NameKana) o;
    return Objects.equals(value, nameKana.value);
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
