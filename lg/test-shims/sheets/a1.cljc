(ns sheets.a1
  "A1-notation parsing for spreadsheet ranges (clj port of lg_sheets/a1.py).

  Supports the subset both Google Sheets and Microsoft Graph workbook clients use:
    - \"Sheet1!A1:C10\"  -> {:sheet :r0 :c0 :r1 :c1}  (0-based inclusive)
    - \"A1:C10\"         -> {:sheet nil ...}           (default/first sheet)
    - \"Sheet1\"         -> {:sheet ... :r0 nil ...}   (whole sheet)

  An A1Range is a plain map {:sheet :r0 :c0 :r1 :c1} (NamedTuple in Python)."
  (:require [kotoba.lang.text :as str]))

(def ^:private cell-re #"^([A-Za-z]+)([0-9]+)$")

(defn a1-range
  "Construct an A1Range map (mirrors the Python NamedTuple positional shape)."
  [sheet r0 c0 r1 c1]
  {:sheet sheet :r0 r0 :c0 c0 :r1 r1 :c1 c1})

(defn col->idx [col]
  (let [n (reduce (fn [n ch] (+ (* n 26) (- (int ch) (int \A)) 1))
                  0 (str/upper col))]
    (dec n)))

(defn idx->col [idx]
  (loop [idx (inc idx) s ""]
    (if (pos? idx)
      (let [q (quot (dec idx) 26)
            r (mod (dec idx) 26)]
        (recur q (str (char (+ (int \A) r)) s)))
      s)))

(defn parse-range [range-str]
  (let [rng0 (str/trim (or range-str ""))
        bang? (str/includes? rng0 "!")
        [sheet rng1] (if bang?
                       (let [idx (str/index-of rng0 "!")]
                         [(-> (subs rng0 0 idx) str/trim (str/replace #"'" ""))
                          (subs rng0 (inc idx))])
                       [nil rng0])
        rng (str/trim rng1)]
    (if (str/blank? rng)
      (a1-range sheet nil nil nil nil)
      (let [parts (str/split rng #":")
            start (re-matches cell-re (nth parts 0))]
        (if-not start
          ;; bare sheet name with no cell range
          (a1-range (or sheet (str/replace (str/trim (or range-str "")) #"'" ""))
                    nil nil nil nil)
          (let [c0 (col->idx (nth start 1))
                r0 (dec (Long/parseLong (nth start 2)))]
            (if (= 1 (count parts))
              (a1-range sheet r0 c0 r0 c0)
              (let [end (re-matches cell-re (nth parts 1))]
                (if-not end
                  (a1-range sheet r0 c0 nil nil)
                  (let [c1 (col->idx (nth end 1))
                        r1 (dec (Long/parseLong (nth end 2)))]
                    (a1-range sheet r0 c0 r1 c1)))))))))))

(defn format-range [sheet r0 c0 r1 c1]
  (str sheet "!" (idx->col c0) (inc r0) ":" (idx->col c1) (inc r1)))
