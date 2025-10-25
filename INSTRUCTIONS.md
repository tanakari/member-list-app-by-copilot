# 指示書

本プロジェクトは **AI駆動開発** を想定しています。  
Copilot や AI エージェント／開発者が迷わず作業できるよう、**入口（索引）**として本ファイルを配置します。

## 🚀 初めての方へ（クイックスタート）

**AIエージェントにすぐ依頼したい場合**は、以下のテンプレートをコピーして使ってください：

### 最も簡単な依頼方法
```
[機能名]の[レイヤー]を実装するIssueを作成してください。

仕様: specs/[ファイルパス]
```

**例:**
```
メンバー一覧のAPIを実装するIssueを作成してください。

仕様: specs/api/members/list.md
```

### よく使う依頼テンプレート
```bash
# 開発開始時
「メンバー一覧機能の開発を始めます。必要なIssue一覧を作成してください」

# エラー修正時  
「以下のエラーを修正してください：[エラーメッセージ]」

# テスト作成時
「MemberServiceクラスのテストを書いてください」
```

👉 **詳しい依頼方法**: [`docs/prompts-examples.md`](./docs/prompts-examples.md)  
👉 **AIとの作業ルール**: [`docs/ai/agent.md`](./docs/ai/agent.md)

---

## 開発ルール・手順

- AIエージェント作業契約: [`docs/ai/agent.md`](./docs/ai/agent.md)
- 開発フロー: [`docs/development-flow.md`](./docs/development-flow.md)
- 命名・コーディング規約: [`docs/conventions.md`](./docs/conventions.md)
- アーキテクチャ指針: [`docs/architecture.md`](./docs/architecture.md)
- 注意事項/ポリシー: [`docs/policies.md`](./docs/policies.md)
- AIエージェントへの依頼例: [`docs/prompts-examples.md`](./docs/prompts-examples.md)

## 仕様書

- 仕様概要・一覧: [`docs/SPEC.md`](./docs/SPEC.md)
- 仕様書ディレクトリ: [`docs/specs/`](./docs/specs/)