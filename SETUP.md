# メンバー管理システム セットアップガイド

## 🎯 このサンプルについて

このセットアップガイドは、**メンバー管理システムのサンプルプロジェクト**を実際に動作させるための手順です。
AI駆動開発の学習用として設計されており、実際のアプリケーション開発プロセス全体を体験できます。

## 前提条件

### 必須ツール
- **Java 21以上** - [OpenJDK](https://openjdk.org/) または [Oracle JDK](https://www.oracle.com/java/)
- **Maven 3.8以上** - [Apache Maven](https://maven.apache.org/)
- **Git** - [Git](https://git-scm.com/)
- **IDE** - IntelliJ IDEA, VS Code, Eclipse等

### 推奨ツール
- **Docker** - 統合テスト用データベース
- **PostgreSQL** - 本番用データベース

## セットアップ手順

### 1. サンプルプロジェクトのフォーク・クローン
```bash
# GitHubでリポジトリをフォーク後
git clone https://github.com/[YOUR_USERNAME]/copilot-coding-agent-guide-templates.git
cd copilot-coding-agent-guide-templates/sample-files
```

### 2. 依存関係インストール
```bash
mvn clean install
```

### 3. データベース設定

#### 開発環境（H2）
デフォルトでH2データベースを使用します。特別な設定は不要です。

#### 本番環境（PostgreSQL）
```bash
# PostgreSQL起動（Docker使用の場合）
docker run -d \
  --name postgres-dev \
  -e POSTGRES_DB=memberdb \
  -e POSTGRES_USER=member \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  postgres:15

# 接続確認
psql -h localhost -U member -d memberdb
```

### 4. アプリケーション起動
```bash
# 開発環境で起動
mvn spring-boot:run

# または
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 5. 動作確認
- アプリケーション: http://localhost:8080
- ヘルスチェック: http://localhost:8080/actuator/health
- H2コンソール: http://localhost:8080/h2-console

## 開発環境設定

### IDE設定

#### IntelliJ IDEA
1. **Google Java Format プラグインインストール（推奨）**
   - File → Settings → Plugins → Google Java Format
2. **コードスタイル設定**
   - File → Settings → Editor → Code Style → Java
   - Scheme: Google Style

#### VS Code
1. **拡張機能インストール**
   ```
   - Extension Pack for Java
   - Google Java Format for VS Code（推奨）
   - Spring Boot Extension Pack
   ```

2. **設定（settings.json）**
   ```json
   {
     "java.format.settings.url": "https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml",
     "java.format.settings.profile": "GoogleStyle"
   }
   ```

## 品質保証設定

### コードフォーマット
```bash
# Google Java Format適用（IDE拡張機能推奨）
# 保存時自動フォーマット推奨
```

## プロファイル設定

### 開発環境
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### テスト環境
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

### 本番環境
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## トラブルシューティング

### よくある問題

#### 1. Java バージョンエラー
```bash
# Javaバージョン確認
java -version
javac -version

# JAVA_HOME設定確認
echo $JAVA_HOME
```

#### 2. Maven依存関係エラー
```bash
# ローカルリポジトリクリア
mvn dependency:purge-local-repository

# 強制再ダウンロード
mvn clean install -U
```

#### 3. データベース接続エラー
```bash
# PostgreSQL接続確認
pg_isready -h localhost -p 5432

# Docker コンテナ確認
docker ps | grep postgres
```

#### 4. ポート衝突エラー
```bash
# ポート使用状況確認
lsof -i :8080
netstat -tulpn | grep :8080

# 別ポートで起動
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=8081"
```

### ログレベル調整

#### application-dev.yml
```yaml
logging:
  level:
    com.example: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

## プロジェクト構造確認

セットアップ完了後、以下の構造になっていることを確認してください：

```
project-root/
├── src/
│   ├── main/java/com/example/
│   ├── main/resources/
│   └── test/java/com/example/
├── docs/               # ドキュメント
├── .github/           # GitHub設定
├── pom.xml            # Maven設定
├── checkstyle.xml     # コーディング規約
└── README.md          # プロジェクト概要
```

## 次のステップ

1. **[INSTRUCTIONS.md](./INSTRUCTIONS.md)** - AI指示とプロジェクトルール確認
2. **[docs/SPEC.md](./docs/SPEC.md)** - 実装すべき機能の確認
3. **[docs/development-flow.md](./docs/development-flow.md)** - 開発フロー理解
4. **GitHub Copilot設定** - コーディングエージェント有効化

セットアップに関する質問は、IssueまたはDiscussionsでお気軽にお尋ねください。
