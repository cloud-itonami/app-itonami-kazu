# itonami Kazu（数）App migration（旧 etzhayyim-project-sheets）

このディレクトリは `legacy-runtime` 実装を残したまま、App 版を段階移行するための配置先です。

## 対象 App services

- `sheets-actor-erhyqofm`
- `sheets-w8tquucg`

## App 実装方針

- 各 service は `projects/*/wasm/*-component` として順次実装。
- 既存 App runtime は互換運用のため維持。
- HTTP/cron/job エンドポイントから優先して移植。

## 追加済みコンポーネント

- `wasm/sheets-mcp-component`
  - `https://[nanoid].etzhayyim.com/api/mcp` と `/[nanoid]/api/mcp` 互換ルートの両方を処理。
  - Clerk JWT 認証 + registry 解決 + backend bridge 転送を提供。

## 移行ステータス

- `sheets-actor-erhyqofm`: implemented (`sheets-mcp-component`)
- `sheets-w8tquucg`: implemented (`sheets-mcp-component`)
