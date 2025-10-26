# メンバー管理システム

## 概要

このアプリケーションは、メンバー情報を管理するシンプルなWebアプリケーションです。
アプリケーションはAI駆動で開発しており、**具体的な実装例とAI開発プロセス全体を学習できるサンプルプロジェクト**です。

## 🎯 このサンプルの目的

- ✅ AI駆動開発における実際のワークフローを体験
- ✅ 具体的なIssue作成方法の実践
- ✅ DDD（ドメイン駆動設計）に基づく段階的開発の学習
- ✅ AIが作成したコードのレビュー手法の習得

## 🤖 AIエージェントとの開発

**開発は AI駆動で行います。** 詳細な開発ルールは [`INSTRUCTIONS.md`](./INSTRUCTIONS.md) を参照してください。

### 📋 基本的な依頼方法

```
[機能名]の[レイヤー]を実装するIssueを作成してください。

仕様: specs/[ファイルパス]
```

**例:** `メンバー一覧のAPIを実装してください。仕様: specs/api/members/list.md`

### 🔗 AI開発関連ドキュメント
- **開発指示書**: [`INSTRUCTIONS.md`](./INSTRUCTIONS.md) - AIエージェント向け詳細ルール
- **依頼例文集**: [`docs/prompts-examples.md`](./docs/prompts-examples.md) - 具体的な依頼方法
- **AI作業契約**: [`docs/ai/agent.md`](./docs/ai/agent.md) - AIとの協働ルール

### ⚠️ 重要ルール
- **PR作成時**: `.github/pull_request_template.md` の**全項目記載必須**
- **開発前**: 必ず [`INSTRUCTIONS.md`](./INSTRUCTIONS.md) を確認

## 🎓 学習用サンプルとして

このメンバー管理システムは、以下の学習ポイントを含んでいます：

### 📚 提供される具体的な仕様
- **API仕様書**: `specs/api/members/` - RESTful APIの詳細設計
- **UI仕様書**: `specs/ui/` - 画面設計とユーザーインタラクション
- **データベース設計**: Entity設計とリレーションシップ

### 🔄 AI開発ワークフロー体験
1. **Issue作成** → 具体的な仕様に基づく適切な指示
2. **AI実装** → Copilotによる自動コード生成を観察
3. **コードレビュー** → AI生成コードの品質評価手法を習得
4. **継続改善** → フィードバックによるAI精度向上

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
├── .github/              # GitHub設定
│   ├── ISSUE_TEMPLATE/  # Issueテンプレート
│   └── pull_request_template.md  # PRテンプレート
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
├── INSTRUCTIONS.md       # 開発手順書の目次（AI向け指示書）
├── SPEC.md              # 仕様書の目次
├── SETUP.md             # セットアップガイド
└── README.md            # このファイル
```
