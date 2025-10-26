# メンバー一覧取得API

## 概要

登録されたメンバーの全一覧を取得するAPIです。画面側でフィルター機能を提供します。

## 対象テーブル

- **メインテーブル**: `members` テーブル
- **取得条件**: `is_deleted = FALSE`（論理削除されていないレコードのみ）
- **参照**: [データベース設計書](../../db/database-design.md)

## 処理フロー

1. `members` テーブルから有効なメンバー情報を全件取得
2. レスポンス形式に整形して返却

## エンドポイント

`GET /api/members`

## リクエストパラメータ

なし（全件取得）

## レスポンス

### 成功時（200 OK）

#### レスポンス項目
| 項目名（JSON） | 項目名（日本語） | 型 | 説明 |
|---------------|----------------|----|----|
| `status` | ステータス | String | 処理結果（"success" or "error"） |
| `message` | メッセージ | String | 処理結果の説明文 |
| `data` | データ | Array[Object] | メンバー情報の配列 |
| `data[].id` | メンバーID | Number | メンバーの一意識別子 |
| `data[].name` | 名前 | String | メンバーの氏名 |
| `data[].nameKana` | 読み仮名 | String | 名前の読み仮名（ひらがな） |
| `data[].email` | メールアドレス | String | 連絡先メールアドレス |
| `data[].position` | 役職 | String | 所属での役職・職種 |
| `data[].location` | 所在地 | String | 勤務地・居住地 |
| `data[].profileImageUrl` | プロフィール写真URL | String | プロフィール画像のURL |
| `data[].selfIntroduction` | 自己紹介 | String | 自己紹介文 |
| `data[].createdAt` | 作成日時 | String (ISO 8601) | メンバー登録日時 |
| `data[].updatedAt` | 更新日時 | String (ISO 8601) | 最終更新日時 |

#### レスポンスボディ（JSON例）
```json
{
  "status": "success",
  "message": "メンバー一覧の取得が完了しました",
  "data": [
    {
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
  ]
}
```

### エラー時（500 Internal Server Error）

#### エラーレスポンス項目
| 項目名（JSON） | 項目名（日本語） | 型 | 説明 |
|---------------|----------------|----|----|
| `status` | ステータス | String | 処理結果（"error"） |
| `message` | メッセージ | String | エラーの説明文 |

#### レスポンスボディ（JSON例）
```json
{
  "status": "error",
  "message": "サーバーエラーが発生しました"
}
```
