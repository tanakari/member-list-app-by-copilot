# メンバー管理システム

## 概要

このアプリケーションは、メンバー情報を管理するシンプルなWebアプリケーションです。
アプリケーションはAI駆動で開発しています。

## 🤖 AIエージェントとの開発

### すぐに始められる簡単依頼

AIエージェントに依頼する際は、以下のテンプレートをコピーして使ってください：

```
[機能名]の[レイヤー]を実装するIssueを作成してください。

仕様: specs/[ファイルパス]
```

**具体例:**
- `メンバー一覧のAPIを実装するIssueを作成してください。仕様: specs/api/members/list.md`
- `メンバー一覧画面を実装してください。仕様: specs/ui/member-list.md`

### その他のよく使う依頼
- `エラーを修正してください：[エラーメッセージ]`
- `[クラス名]のテストを書いてください`  
- `何から始めればいいか教えてください`

👉 **詳しい依頼方法**: [docs/prompts-examples.md](./docs/prompts-examples.md)

## 主な機能

- メンバー情報の登録・更新・削除
- メンバー情報の一覧表示
- メンバー情報の検索機能（名前・メール）
- ページング機能

## アーキテクチャ・技術

### アーキテクチャ

- ドメイン駆動

### 技術

- Java 25
- Spring Boot 3.5.x
- Thymeleaf
- Spring Data JPA
- Maven
- PostgreSQL

詳細な技術スタックは [docs/stack.md](./docs/stack.md) を参照してください。

## セットアップ

セットアップ手順は [SETUP.md](./SETUP.md) を参照してください。

### クイックスタート

```bash
# リポジトリクローン
git clone https://github.com/tanakari/member-management.git
cd member-management

# 環境設定
cp .env.example .env
# .envファイルを編集

# 依存関係インストール & 起動
mvn clean install
mvn spring-boot:run
```

## ディレクトリ構成

```
member-management/
├── .github/              # GitHub設定（Issue/PRテンプレート）
├── docs/                 # 各種ドキュメント
├── instructions/         # 開発手順書
├── specs/                # 仕様書
│   ├── api/             # API仕様書
│   └── ui/              # 画面仕様書
├── src/                  # ソースコード
│   ├── main/
│   │   ├── java/        # Javaソースコード
│   │   └── resources/   # リソースファイル
│   └── test/            # テストコード
├── pom.xml              # Maven設定ファイル
├── INSTRUCTIONS.md       # 開発手順書の目次
├── SPEC.md              # 仕様書の目次
├── SETUP.md             # セットアップガイド
└── README.md            # このファイル
```
