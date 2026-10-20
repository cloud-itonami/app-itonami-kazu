# app-sheets（A1 記法）— itonami（dapp）上の分散型設計

状態: 第 1 スライス実装（2026-10-20）。正本はこの文書。
追跡: Linear JUN-128（親 JUN-116）。

## 位置づけ

app-sheets は **Google Sheets v4 と Microsoft Graph workbook の両方に互換な
スプレッドシート API**（sheets.etzhayyim.com、canonical は kotoba datomic
`sheets-v1`）。workspace-compat の第 3 垂直。本スライスは **A1 記法の
判定**（`a1.cljk`）を判定コア化。

## このスライス: A1 記法判定コア

| 層 | 実体 | 状態 |
|---|---|---|
| 判定コア guest | `aiueos/sheets_a1.cljk` — col->idx / idx->col（base-26 互逆: A=0, Z=25, AA=26, AB=27, AAA=702）/ range-class（parse の枝: 単一セル / 閉じた範囲 / 開いた範囲 / シート名のみ / シート全体） | **実装済み・実測** |
| 記述子 | `aiueos/sheets-a1.edn` | **実装済み** |
| parity | `lg/test/sheets/a1_parity_test.cljk` — **実物の a1.cljk を bb で実行**し col 変換・parse 結果と一致 | **実装済み・実測** |

実測: `amu test aiueos/sheets_a1.cljk` → **18/18**（jvm-kir / js / wasm）。
parity → **15 assertions / 0 failures**（bb 実行）。

## 実測で判明した2つの正本の事実（docs 記録・repo 側へ提出）

1. **正本 a1.cljk は bb/JVM 専用**: `(int ch)` / `(char ...)` の文字演算が
   **kbb（sci）では壊れる**（`col->idx "Z"` → 0、`idx->col 0` → `"\u0000"`）。
   bb では正しい（25 / "A"）。guest は 3 ターゲットすべてで bb と一致。
   parity は bb で実行し、shims（`lg/test-shims/`: kotoba.lang.text の
   clojure.string 委譲 + .cljc コピー）で classpath を満たす。
2. **"Sheet1" のようなセル形のシート名は単一セルに誤 parse される** —
   docstring は「"Sheet1" → シート全体」と言うが、`cell-re`
   （`^([A-Za-z]+)([0-9]+)$`）が "Sheet1" にマッチしてセル扱いになる
   （"Data_1" のような非セル形は正しく全体）。guest の range-class は
   **コードの枝構造**（start-is-cell フラグ）に追従。

## 未実装境界

- parse-range の文字列処理（sheet 名の `'` 除去・trim）は host 仕事
  （guest はスカラーの判定のみ。packed 2 桁/文字の列表現で対応）。
- format-range（逆方向の文字列化）。mapping / handlers の tx 射影。
- スキン変換（values.batchGet 等の reshape）は CF Worker 側。
- mon（DNA）化、aiueos session 載せ（fleet 共通）。
