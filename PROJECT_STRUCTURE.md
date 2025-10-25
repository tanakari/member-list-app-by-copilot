# プロジェクト構造

AI駆動開発プロジェクトの標準ディレクトリ構造です。

```
project-root/
├── README.md                    # プロジェクト概要
├── INSTRUCTIONS.md              # AI指示書
├── PROJECT_STRUCTURE.md         # このファイル（ルート）
├── SETUP.md                     # セットアップ手順
├── .editorconfig               # エディタ設定
├── docs/                       # ドキュメント
│   ├── SPEC.md                 # 仕様書ハブ
│   ├── specs/                  # 仕様書実体
│   │   ├── api/                # API仕様
│   │   │   └── members/        # リソース別
│   │   │       ├── create.md   # 作成API
│   │   │       └── list.md     # 一覧API
│   │   ├── ui/                 # UI仕様
│   │   │   ├── member-list.md  # 一覧画面
│   │   │   ├── member-map.md   # マップ画面
│   │   │   └── member-registration.md # 登録画面
│   │   └── db/                 # DB仕様
│   │       └── schema.md       # データベース設計
│   ├── ai/                     # AI関連ドキュメント
│   │   └── agent.md            # AI作業契約
│   ├── development-flow.md     # 開発プロセス
│   ├── conventions.md          # コーディング規約
│   ├── architecture.md         # アーキテクチャ方針
│   ├── policies.md             # 開発ポリシー
│   ├── prompts-examples.md     # AI依頼例
│   └── stack.md                # 技術スタック詳細
├── .github/                    # GitHub設定
│   ├── ISSUE_TEMPLATE/         # Issueテンプレート
│   │   ├── feature_request.yml # 機能要望
│   │   └── bug_report.yml      # バグ報告
│   └── pull_request_template.md # PRテンプレート
└── src/                        # ソースコード（実装時作成）
```

## ファイル配置ルール

### プロジェクトルート
- `README.md` - プロジェクト概要
- `INSTRUCTIONS.md` - AI指示書（エントリーポイント・索引）
- `PROJECT_STRUCTURE.md` - プロジェクト構造リファレンス（このファイル）
- `SETUP.md` - セットアップ手順
- `.editorconfig` - エディタ設定統一

### ドキュメント（docs/）
- `docs/development-flow.md` - Issue→PR→マージのワークフロー
- `docs/conventions.md` - 命名規則・フォーマット
- `docs/architecture.md` - システム設計・パターン
- `docs/policies.md` - セキュリティ・品質管理
- `docs/prompts-examples.md` - AI指示の例文集
- `docs/stack.md` - 技術選定理由

### 仕様書（docs/内で統合管理）
- `docs/SPEC.md` - 仕様書ハブ（各仕様へのリンク集）
- `docs/specs/` - 仕様書実体（プロジェクト固有に書き換え）
  - `docs/specs/api/` - RESTful API仕様書
  - `docs/specs/ui/` - 画面・UI仕様書
  - `docs/specs/db/` - データベース設計書

### AI関連（docs/ai/で管理）
- `docs/ai/agent.md` - AIエージェントとの作業契約書
- `INSTRUCTIONS.md` - AI指示書（エントリーポイント、agent.mdを参照）

### GitHub設定
- `.github/ISSUE_TEMPLATE/` - Issue作成テンプレート
- `.github/pull_request_template.md` - PR作成テンプレート

### 実装
- `src/main/java/` - Javaソースコード
- `src/test/java/` - テストコード
- `src/main/resources/` - 設定ファイル・テンプレート

## 命名規則
- ファイル名: `kebab-case.md`
- ディレクトリ名: `lowercase`
- API仕様: `create.md`, `list.md`, `update.md`, `delete.md`

## docs/specs/ ディレクトリについて
⚠️ `docs/specs/` 内のファイルはテンプレートです。プロジェクト固有の仕様に書き換えてください。
`docs/SPEC.md` が各仕様書へのハブ（リンク集）として機能します。

## 実装パッケージ構成 (Java)
```
src/main/java/com/example/project/
├── Application.java            # メインクラス
├── controller/                 # REST API
├── service/                    # ビジネスロジック
├── repository/                 # データアクセス
└── domain/                     # ドメインモデル
```