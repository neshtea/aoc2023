(ns aoc2023.day1
  (:require
   [clojure.java.io :as io]))

(def digit? (->> [0 1 2 3 4 5 6 7 8 9]
                 (map str)
                 (into #{})))

(defn first+last [xs]
  [(first xs) (last xs)])

(defn- reduce-result [res]
  (->> (first+last res)
       (apply str)
       (Integer/parseInt)))

(defn parse-line-part-1
  [line]
  (reduce-result (filter (comp digit? str) line)))

(defn digit-string [k]
  (get {"one" "1", "two" "2", "three" "3", "four" "4", "five" "5", "six" "6", "seven" "7", "eight" "8", "nine" "9"}
       k k))

(defn all-subs [^String s]
  (let [cnt (count s)]
    (for [start (range (inc cnt))
          end (range (inc cnt))
          :when (< start end)]
      (subs s start end))))

(defn parse-line-part-2 [line]
  (->> (all-subs line)
       (map digit-string)
       (parse-line-part-1)))

(defn run [line-parser file-path]
  (with-open [reader (io/reader file-path)]
    (let [lines (line-seq reader)]
      (reduce (fn [acc line] (+ acc (line-parser line))) 0 lines))))

(defn -main [& _]
  (time (do
          (println "Part 1 =" (run parse-line-part-1 (io/resource "day1.input")))
          (println "Part 2 =" (run parse-line-part-2 (io/resource "day1.input"))))))

;; (-main)
