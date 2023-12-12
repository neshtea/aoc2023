(ns aoc2023.day9
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]))

(defn curry [f] (fn [[a b]] (f a b)))

(defn diff-at-step [xs]
  (map (comp (curry -) vector) xs (rest xs)))

(defn diffs [history]
  (->> (iterate diff-at-step history)
       (take-while not-empty)
       (reverse)))

(defn make-extrapolate [make-pad]
  (fn [diffs]
    (letfn [(worker [acc pad xs]
              (cond
                (empty? xs) acc

                (empty? (rest xs))
                (conj acc (cons pad (first xs)))

                :else
                (let [[x y & ys] xs]
                  (recur (conj acc (cons pad x))
                         (make-pad pad y)
                         (cons y ys)))))]
      (worker [] 0 diffs))))

(defn extrapolation [make-pad line]
  (->> (string/split line #"\ ")
       (map #(Integer/parseInt %))
       (reverse)
       (diffs)
       ((make-extrapolate make-pad))
       (map first)
       (last)))

(defn run [make-pad file-path]
  (with-open [r (io/reader file-path)]
    (reduce (fn [acc line]
              (+ acc (extrapolation make-pad line)))
            0
            (line-seq r))))

(def part-1 (partial run (fn [pad ys] (+ pad (first ys)))))
(def part-2 (partial run (fn [pad ys] (- (last ys) pad))))

(defn -main [& _]
  (time (println "Part 1 =" (part-1 (io/resource "day9.input"))))
  (time (println "Part 2 =" (part-2 (io/resource "day9.input")))))
