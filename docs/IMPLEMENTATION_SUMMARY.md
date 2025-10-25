# ドメイン層とインフラストラクチャ層実装サマリー

## 実装概要

DDD（ドメイン駆動設計）のレイヤーアーキテクチャに基づいて、ドメイン層とインフラストラクチャ層を実装しました。

## 実装したコンポーネント

### ドメイン層 (domain)

#### 値オブジェクト (domain/model)
- **Email.java**: メールアドレス
  - バリデーション: 必須、255文字以内、メール形式
- **Name.java**: 名前
  - バリデーション: 必須、100文字以内
- **NameKana.java**: 読み仮名
  - バリデーション: 必須、ひらがなのみ、100文字以内
- **Position.java**: 役職
  - バリデーション: 任意、100文字以内
- **Location.java**: 所在地
  - バリデーション: 任意、200文字以内

#### エンティティ (domain/model)
- **Member.java**: メンバーエンティティ
  - ビジネスロジック: 新規作成、更新、論理削除
  - 不変条件の保証

#### リポジトリインターフェース (domain/repository)
- **MemberRepository.java**: メンバーリポジトリ
  - save: メンバーの保存
  - findById: IDでメンバーを検索
  - findByEmail: メールアドレスでメンバーを検索
  - findAll: すべてのメンバーを取得（削除されていないもののみ）
  - deleteById: メンバーの論理削除
  - existsByEmail: メールアドレスの存在確認

#### ドメインサービス (domain/service)
- **MemberDomainService.java**: メンバードメインサービス
  - メールアドレス重複チェック

### インフラストラクチャ層 (infrastructure)

#### 永続化 (infrastructure/persistence)
- **MemberJpaEntity.java**: JPA用エンティティ
  - データベーステーブルとのマッピング
  - @PrePersist, @PreUpdateでタイムスタンプ管理
- **MemberJpaRepository.java**: Spring Data JPA リポジトリ
  - 論理削除を考慮したクエリメソッド
- **MemberRepositoryImpl.java**: リポジトリ実装
  - ドメインモデルとJPAエンティティの変換
  - ドメイン層とインフラ層のブリッジ

### データベースマイグレーション

#### マイグレーションスクリプト
- **V1__create_members_table.sql**: membersテーブル作成
  - テーブル定義
  - インデックス定義（パフォーマンス最適化）
  - H2とPostgreSQLの両方に対応

### テスト

#### ドメイン層テスト
- **MemberTest.java**: ドメインエンティティのテスト
  - メンバー作成テスト
  - メンバー更新テスト
  - 論理削除テスト

#### インフラストラクチャ層テスト
- **MemberRepositoryImplTest.java**: リポジトリ実装のテスト
  - 保存・検索テスト
  - メールアドレス検索テスト
  - 全件取得テスト
  - 存在確認テスト
  - 論理削除テスト

## アーキテクチャの特徴

### DDD準拠
- ドメイン層とインフラ層の明確な分離
- ドメインモデルは永続化技術に依存しない
- 値オブジェクトによる型安全性の確保
- ビジネスルールのドメイン層への集約

### クリーンアーキテクチャ原則
- 依存関係の逆転: インフラ層がドメイン層に依存
- 境界での変換: JPAエンティティとドメインモデルの分離
- テスト容易性: ドメインロジックの単体テスト可能

### データベース設計
- 論理削除による履歴保持
- パフォーマンス最適化（インデックス）
- メールアドレスの一意性保証

## 動作確認

### テスト実行
```bash
mvn test -Dspring.profiles.active=dev
```

全10テスト成功:
- コントローラーテスト: 2件
- ドメインモデルテスト: 3件
- リポジトリテスト: 5件

### 完了条件の確認

✅ データベース接続が正常に動作する
- Flywayによる自動マイグレーション成功
- H2データベースへの接続成功
- テーブル作成成功

✅ 基本的なCRUD操作が可能
- Create: memberRepository.save()
- Read: memberRepository.findById(), findByEmail(), findAll()
- Update: member.update() + save()
- Delete: memberRepository.deleteById()（論理削除）

✅ テーブルが適切に作成される
- membersテーブルが正しいスキーマで作成
- インデックスが適切に設定
- 制約が正しく適用

✅ DDDのレイヤーアーキテクチャに準拠している
- ドメイン層: エンティティ、値オブジェクト、リポジトリインターフェース、ドメインサービス
- インフラストラクチャ層: JPAエンティティ、リポジトリ実装
- 依存関係の方向が正しい（インフラ→ドメイン）

## 技術スタック

- Java 17
- Spring Boot 3.5.0
- Spring Data JPA
- Hibernate 6.6.15
- Flyway 11.7.2
- PostgreSQL（本番環境）
- H2（開発・テスト環境）
- JUnit 5

## ドキュメント

- [データベースセットアップガイド](DATABASE_SETUP.md)
- [データベース設計書](specs/db/database-design.md)
- [アーキテクチャ方針](architecture.md)

## 次のステップ

ドメイン層とインフラストラクチャ層の実装が完了したので、次は以下を実装できます：

1. **アプリケーション層**: ユースケース実装
2. **プレゼンテーション層**: REST APIコントローラー
3. **UI層**: Thymeleafテンプレート

これらの実装により、完全なメンバー管理アプリケーションが完成します。
