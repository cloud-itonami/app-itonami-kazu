(ns kotoba.lang.text
  "bb 用の最小 shim（parity 実行のみ。kbb ランタイムは kotoba.lang.text を内蔵）。
  clojure.string に委譲する。"
  (:refer-clojure :exclude [replace])
  (:require [clojure.string :as cs]))

(defn trim [s] (cs/trim s))
(defn blank? [s] (cs/blank? s))
(defn includes? [s sub] (cs/includes? s sub))
(defn starts-with? [s pre] (cs/starts-with? s pre))
(defn index-of [s sub] (cs/index-of s sub))
(defn upper [s] (cs/upper-case s))
(defn replace [s re-or-sub sub] (cs/replace s re-or-sub sub))
(defn split [s re] (cs/split s re))
(defn join [sep coll] (cs/join sep coll))
