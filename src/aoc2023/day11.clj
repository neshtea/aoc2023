(ns aoc2023.day11
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(def example "...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....")

(defn galaxy? [c] (= c \#))

(defn parse-input [s]
  (map (fn [line]
         (map (fn [c]
                (when (galaxy? c) :ga))
              line))
       (string/split s #"\n")))

(def empty-line? (partial every? (comp nil?)))

(def transpose (partial apply mapv vector))

(defn expand
  [expansion-multiplier matrix]
  (let [expand (fn [matrix]
                 (reduce (fn [acc row]
                           (if (empty-line? row)
                             (concat acc (repeat expansion-multiplier row))
                             (conj acc row)))
                         []
                         matrix))
        expanded-height (expand matrix)]
    (transpose (expand (transpose expanded-height)))))

(defn add-indices
  [matrix]
  (map-indexed (fn [j row]
                 (map-indexed (fn [i col]
                                {:x i :y j :value col})
                              row))

               matrix))

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
         (expand 2)
         (add-indices)
         (calc))))

(part-1 (io/resource "day11.input"))

(defn part-2 [file-path]
  (with-open [r (io/reader file-path)]
    (->> (parse-input (slurp r))
         (expand 1000000)
         (add-indices)
         (calc))))

(defn -main [& _]
  (time (println "Part 1 =" (part-1 (io/resource "day11.input"))))
  (time (println "Part 2 =" (part-2 (io/resource "day11.input")))))
