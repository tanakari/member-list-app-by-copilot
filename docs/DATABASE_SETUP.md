# データベースセットアップガイド

## 概要

このアプリケーションはFlywayを使用してデータベースマイグレーションを管理しています。

## 開発環境（H2）

開発環境ではH2インメモリデータベースを使用します。アプリケーション起動時に自動的にテーブルが作成されます。

```bash
# devプロファイルでアプリケーションを起動
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

H2コンソールは以下のURLでアクセスできます：
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:devdb
- ユーザー名: sa
- パスワード: (空欄)

## 本番環境（PostgreSQL）

### 前提条件
- PostgreSQL 15以上がインストールされていること
- データベースとユーザーが作成されていること

### PostgreSQLのセットアップ

1. データベースとユーザーの作成：

```sql
CREATE DATABASE memberlist;
CREATE USER memberlist_user WITH PASSWORD 'memberlist_password';
GRANT ALL PRIVILEGES ON DATABASE memberlist TO memberlist_user;
```

2. アプリケーション設定：

`application.yml`に以下の設定があることを確認してください：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/memberlist
    username: memberlist_user
    password: memberlist_password
  flyway:
    enabled: true
    baseline-on-migrate: true
```

3. アプリケーションを起動：

```bash
mvn spring-boot:run
```

Flywayが自動的にマイグレーションを実行し、テーブルが作成されます。

## マイグレーションファイル

マイグレーションファイルは`src/main/resources/db/migration/`にあります：

- `V1__create_members_table.sql`: membersテーブルとインデックスの作成

## テーブル構造

### membersテーブル

| カラム名 | データ型 | 制約 | 説明 |
|---------|---------|------|------|
| id | BIGINT | PRIMARY KEY | メンバーID（自動採番） |
| name | VARCHAR(100) | NOT NULL | 名前 |
| name_kana | VARCHAR(100) | NOT NULL | 読み仮名（ひらがな） |
| email | VARCHAR(255) | NOT NULL, UNIQUE | メールアドレス |
| position | VARCHAR(100) | - | 役職 |
| location | VARCHAR(200) | - | 所在地 |
| profile_image_url | TEXT | - | プロフィール写真URL |
| self_introduction | TEXT | - | 自己紹介 |
| created_at | TIMESTAMP | NOT NULL | 登録日時 |
| updated_at | TIMESTAMP | NOT NULL | 更新日時 |
| is_deleted | BOOLEAN | NOT NULL | 削除フラグ |

## マイグレーションの確認

```bash
# マイグレーション状態の確認
mvn flyway:info

# マイグレーションの実行（手動）
mvn flyway:migrate

# マイグレーションのロールバック（注意：本番環境では慎重に）
mvn flyway:clean
```

## トラブルシューティング

### マイグレーションが失敗する場合

1. データベース接続情報を確認
2. PostgreSQLが起動していることを確認
3. ユーザーに適切な権限があることを確認

### マイグレーションをやり直したい場合（開発環境のみ）

```bash
mvn flyway:clean
mvn flyway:migrate
```

**注意**: `flyway:clean`は全てのテーブルを削除します。本番環境では絶対に実行しないでください。
