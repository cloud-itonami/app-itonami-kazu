(ns sheets.a1-parity-test
  "aiueos/sheets_a1.cljk（判定コア）と sheets.a1 の parity。実関数
  parse-range / col->idx / idx->col が guest の判定と一致する。"
  (:require [clojure.test :refer [deftest is testing]]
            [sheets.a1 :as a1]))

(deftest col-index-matches-guest
  (testing "guest test-col / test-col-2 / test-col-3: base-26（A=0, Z=25, AA=26, AB=27, AAA=702）"
    (is (= 0 (a1/col->idx "A")))
    (is (= 25 (a1/col->idx "Z")))
    (is (= 26 (a1/col->idx "AA")))
    (is (= 27 (a1/col->idx "AB")))
    (is (= 702 (a1/col->idx "AAA"))))
  (testing "guest test-idx: 互逆"
    (is (= "A" (a1/idx->col 0)))
    (is (= "Z" (a1/idx->col 25)))
    (is (= "AA" (a1/idx->col 26)))
    (is (= "AAA" (a1/idx->col 702)))))

(deftest range-class-matches-guest
  (testing "guest test-range-class: parse の枝分かれ（0..4）"
    ;; 単一セル
    (let [r (a1/parse-range "Sheet1!A1")]
      (is (= "Sheet1" (:sheet r)))
      (is (= 0 (:r0 r) (:c0 r) (:r1 r) (:c1 r))))
    ;; 閉じた範囲（0-based inclusive）
    (is (= {:sheet "Sheet1" :r0 0 :c0 0 :r1 9 :c1 2}
           (a1/parse-range "Sheet1!A1:C10")))
    ;; sheet 無し → デフォルトシート
    (is (nil? (:sheet (a1/parse-range "A1:C10"))))
    ;; シート名のみ（セル形でない名前）→ 全体（bounds nil）
    (is (= {:sheet "Data_1" :r0 nil :c0 nil :r1 nil :c1 nil}
           (a1/parse-range "Data_1")))
    ;; 既知の正本の癖（docs に記録）: "Sheet1" のようなセル形のシート名は
    ;; 単一セルとして parse されてしまう（docstring の「シート全体」と不一致）
    (is (some? (:r0 (a1/parse-range "Sheet1"))))))
