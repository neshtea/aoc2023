(ns aoc2023.day6)

(defn variants
  [[race-millis current-record]]
  (->> (map inc (range (dec race-millis)))
       (map (fn [press-duration]
              (* press-duration (- race-millis press-duration))))
       (filter (partial < current-record))
       (count)))

(->> [[7 9] [15 40] [30 200]]
     (map variants)
     (apply * 1))

(defn part1 []
  (->> [[62 644] [73 1023] [75 1240] [65 1023]]
       (map variants)
       (apply * 1)))

(defn part2 []
  (variants [62737565 644102312401034]))

(defn -main [& _]
  (time (println "Part 1 =" (part1)))
  (time (println
         "Part 2 =" (part2))))

(-main)
