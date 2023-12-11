(ns aoc2023.day4
  (:require [clojure.string :as string]
            [clojure.set :as set]
            [clojure.java.io :as io]))

(def example-input
  "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11")

(defn parse-line [line]
  (let [[card numbers] (string/split line #":")
        [winning-numbers my-numbers] (string/split numbers #"\|")
        parse-numbers (fn [s] (into #{} (map #(Integer/parseInt %) (re-seq #"\d+" s))))]
    {:card-id (Integer/parseInt (first (re-seq #"\d+" card)))
     :winning-numbers (parse-numbers winning-numbers)
     :my-numbers (parse-numbers my-numbers)}))

(defn winning-numbers-count [card]
  (count (set/intersection (:winning-numbers card) (:my-numbers card))))

(defn card-worth [card]
  (->> (winning-numbers-count card)
       (dec)
       (Math/pow 2)
       (int)))

(defn part1 [file-path]
  (with-open [r (io/reader file-path)]
    (->> (line-seq r)
         (map parse-line)
         (map card-worth)
         (apply +))))

(defn generate-copies [acc cards]
  (if (empty? cards)
    cards
    (let [head (first cards)
          tail (rest cards)
          wins (winning-numbers-count head)]

      (generate-copies (conj acc head)
                       (take wins tail)))))

(defn part2 [file-path]
  (with-open [r (io/reader file-path)]
    (let [cards (->> (line-seq r)
                     (map parse-line))]
      (take 10 cards))))

(part1 (io/resource "day4.input"))
(part2 (io/resource "day4.input"))
