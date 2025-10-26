# メンバー登録API

## 概要

新しいメンバーをシステムに登録するAPIです。入力バリデーション、重複チェックを行います。

## 対象テーブル

- **メインテーブル**: `members` テーブル
- **参照**: [データベース設計書](../../db/database-design.md)

## 処理フロー

1. リクエストボディのバリデーション
2. メールアドレスの重複チェック（`members.email`）
3. `members` テーブルに新規レコード挿入
4. 登録完了レスポンスを返却

## エンドポイント

`POST /api/members`

## リクエスト

### リクエスト項目
| 項目名（JSON） | 項目名（日本語） | 型 | 必須 | 説明 |
|---------------|----------------|----|----|------|
| `name` | 名前 | String | ✓ | メンバーの氏名 |
| `nameKana` | 読み仮名 | String | ✓ | 名前の読み仮名（ひらがな） |
| `email` | メールアドレス | String | ✓ | 連絡先メールアドレス |
| `position` | 役職 | String | - | 所属での役職・職種 |
| `location` | 所在地 | String | - | 勤務地・居住地 |
| `profileImageUrl` | プロフィール写真URL | String | - | プロフィール画像のURL |
| `selfIntroduction` | 自己紹介 | String | - | 自己紹介文 |

### リクエストボディ（JSON例）
```json
{
  "name": "山田太郎",
  "nameKana": "やまだたろう",
  "email": "yamada@example.com",
  "position": "エンジニア",
  "location": "東京都",
  "profileImageUrl": "https://...",
  "selfIntroduction": "フルスタックエンジニアです"
}
```

## バリデーション

| 項目 | ルール | DB制約との対応 |
|------|--------|-------------|
| `name` | 必須、100文字以内 | `members.name` (VARCHAR(100) NOT NULL) |
| `nameKana` | 必須、ひらがなのみ、100文字以内 | `members.name_kana` (VARCHAR(100) NOT NULL) |
| `email` | 必須、メール形式、重複不可 | `members.email` (VARCHAR(255) NOT NULL, UNIQUE) |
| `position` | 任意、100文字以内 | `members.position` (VARCHAR(100)) |
| `location` | 任意、200文字以内 | `members.location` (VARCHAR(200)) |
| `profileImageUrl` | 任意、URL形式 | `members.profile_image_url` (TEXT) |
| `selfIntroduction` | 任意、1000文字以内 | `members.self_introduction` (TEXT) |

## レスポンス

### 成功時（201 Created）

#### レスポンス項目
| 項目名（JSON） | 項目名（日本語） | 型 | 説明 |
|---------------|----------------|----|----|
| `status` | ステータス | String | 処理結果（"success" or "error"） |
| `message` | メッセージ | String | 処理結果の説明文 |
| `data` | データ | Object | 登録されたメンバー情報 |
| `data.id` | メンバーID | Number | 自動採番されるメンバーの一意識別子 |
| `data.name` | 名前 | String | メンバーの氏名 |
| `data.nameKana` | 読み仮名 | String | 名前の読み仮名（ひらがな） |
| `data.email` | メールアドレス | String | 連絡先メールアドレス |
| `data.position` | 役職 | String | 所属での役職・職種 |
| `data.location` | 所在地 | String | 勤務地・居住地 |
| `data.profileImageUrl` | プロフィール写真URL | String | プロフィール画像のURL |
| `data.selfIntroduction` | 自己紹介 | String | 自己紹介文 |
| `data.createdAt` | 作成日時 | String (ISO 8601) | メンバー登録日時 |
| `data.updatedAt` | 更新日時 | String (ISO 8601) | 最終更新日時 |

#### レスポンスボディ（JSON例）
```json
{
  "status": "success",
  "message": "登録が完了しました",
  "data": {
    "id": 1,
    "name": "山田太郎",
    "nameKana": "やまだたろう",
    "email": "yamada@example.com",
    "position": "エンジニア",
    "location": "東京都",
    "profileImageUrl": "https://...",
    "selfIntroduction": "フルスタックエンジニアです",
    "createdAt": "2025-01-01T00:00:00Z",
    "updatedAt": "2025-01-01T00:00:00Z"
  }
}
```

### エラー時（400 Bad Request / 500 Internal Server Error）

#### エラーレスポンス項目
| 項目名（JSON） | 項目名（日本語） | 型 | 説明 |
|---------------|----------------|----|----|
| `status` | ステータス | String | 処理結果（"error"） |
| `message` | メッセージ | String | エラーの概要説明 |
| `errors` | エラー詳細 | Array[String] | 具体的なエラー内容の配列 |

#### レスポンスボディ（JSON例）
```json
{
  "status": "error",
  "message": "バリデーションエラーです",
  "errors": ["メールアドレスが既に登録されています"]
}
```
