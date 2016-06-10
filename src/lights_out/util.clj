(ns lights-out.util)

(defn rand-bool
  "Randomly return true or false."
  []
  (= (rand-int 2) 1))

(defn rand-bool-coll
  "Return a vector of size |n| filled with random booleans."
  [n]
  (into [] (map (fn [_] (rand-bool)) (range n))))

(defn amap-indexed
  "Map over each element in a 2d seq with its row and column and apply |f|."
  [f arr]
  (map-indexed
    (fn [row sub-arr]
      (map-indexed (fn [col elem] (f row col elem)) sub-arr))
    arr))