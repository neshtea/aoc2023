(ns aoc2023.day2
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]))

(defn parse-game
  [line]
  (let [[game rounds] (string/split line #":")]
    {:game-id (-> (string/split game #"\ ") (second) (Integer/parseInt))
     :rounds (->> (string/split rounds #";")
                  (map (fn [round]
                         (map (fn [result]
                                (let [[num color] (string/split (string/trim result) #"\ ")]
                                  (assoc {:red 0, :green 0, :blue 0} (keyword color) (Integer/parseInt num))))
                              (-> (string/trim round)
                                  (string/split #","))))))}))

;; part 1
(defn round-stats [round]
  (apply (partial merge-with +) round))

(defn round-possible?
  [max-red max-green max-blue round]
  (let [{:keys [red green blue]} (round-stats round)]
    (and (<= red max-red)
         (<= green max-green)
         (<= blue max-blue))))

(defn game-possible? [max-red max-green max-blue game]
  (every? (partial round-possible? max-red max-green max-blue) (:rounds game)))

(defn sum-of-possible-game-ids [max-red max-green max-blue games]
  (let [xform (comp (filter (partial game-possible? max-red max-green max-blue))
                    (map :game-id))]
    (transduce xform + 0 games)))

;; part 2
(def max-merge (partial merge-with max))

(defn rounds-cubes-min [rounds]
  (->> (map (partial apply max-merge) rounds)
       (apply max-merge)))

(defn game-cubes-min [game]
  (rounds-cubes-min (:rounds game)))

(defn game-power [game]
  (apply * (vals (game-cubes-min game))))

(defn power-sum [games]
  (apply + (map game-power games)))

(defn run [f file-path]
  (with-open [reader (io/reader file-path)]
    (->> (line-seq reader)
         (map parse-game)
         (f))))

(defn -main [& _]
  (time (do (println "Part 1 =" (run (partial sum-of-possible-game-ids 12 13 14) (io/resource "day2.input")))
            (println "Part 2 =" (run power-sum (io/resource "day2.input"))))))
