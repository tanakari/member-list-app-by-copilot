-- メンバーテーブルの作成
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    name_kana VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    position VARCHAR(100),
    location VARCHAR(200),
    profile_image_url TEXT,
    self_introduction TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- メールアドレスの一意性制約（削除されていないレコードのみ）
-- H2とPostgreSQLでは構文が異なるため、条件付きインデックスは個別に作成する
CREATE UNIQUE INDEX idx_members_email_not_deleted ON members (email, is_deleted);

-- 一覧表示用インデックス
CREATE INDEX idx_members_deleted_created ON members (is_deleted, created_at DESC);

-- 名前検索用インデックス
CREATE INDEX idx_members_name_not_deleted ON members (name, is_deleted);

-- 読み仮名検索用インデックス
CREATE INDEX idx_members_name_kana_not_deleted ON members (name_kana, is_deleted);
