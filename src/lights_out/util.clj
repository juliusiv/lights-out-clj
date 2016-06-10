(ns lights-out.util)

(defn rand-bool
  "Randomly return true or false."
  []
  (= (rand-int 2) 1))

(defn print-each
  "Print each item in a seq."
  [vals]
  (doseq [i vals]
    (println i))
  (println "\n"))