(ns aoc2023.day11
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(defn galaxy? [c] (= c \#))

(defn parse-input [s]
  (map-indexed (fn [y line]
                 (map-indexed (fn [x c]
                                {:x x
                                 :y y
                                 :value (when (galaxy? c) :value)})
                              line))
               (string/split s #"\n")))

(def empty-line? (partial every? (comp nil? :value)))

(def transpose (partial apply mapv vector))

(defn expand
  [expansion matrix]
  (let [expand (fn [k matrix]
                 (second (reduce (fn [[total-expansion acc] row]
                                   (let [new-expansion (if (empty-line? row)
                                                         (+ total-expansion expansion)
                                                         total-expansion)]
                                     [new-expansion
                                      (conj acc (map (fn [c]
                                                       (update c k (fn [idx]
                                                                     (+ idx new-expansion))))
                                                     row))]))
                                 [0 []]
                                 matrix)))
        expanded-height (expand :y matrix)]
    (transpose (expand :x (transpose expanded-height)))))

(defn distance [[g1 g2]]
  (+ (Math/abs (- (:x g1) (:x g2)))
     (Math/abs (- (:y g1) (:y g2)))))

(defn calc [matrix]
  (let [v (->> (apply concat matrix)
               (filter (comp some? :value)))
        all-pairs (->> (for [x v
                             y v
                             :when (not= x y)]
                         #{x y})
                       (set)
                       (map vec))]
    (->> (map distance all-pairs)
         (apply +))))

(defn part-1 [file-path]
  (with-open [r (io/reader file-path)]
    (->> (parse-input (slurp r))
         (expand 1)
         (calc))))

(defn part-2 [file-path]
  (with-open [r (io/reader file-path)]
    (->> (parse-input (slurp r))
         (expand 999999)
         (calc))))

(defn -main [& _]
  (time (println "Part 1 =" (part-1 (io/resource "day11.input"))))
  (time (println "Part 2 =" (part-2 (io/resource "day11.input")))))
