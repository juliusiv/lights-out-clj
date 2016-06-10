(ns lights-out.core
  (:gen-class)
  (:use [seesaw.core :as seesaw])
  (:use [lights-out.util :as util]))

; (native!)

; Create the frame that everything will be drawn on.
(def f (frame :title "Lights Out"))
; Define the number of lights in the board as a constant.
(def ^:const board-size 5)
(def ^:const total-tiles (* board-size board-size))

(declare update-board)
(declare make-light-list)
(declare make-light)

; Create a blank board with random tiles turned on.
(def board-data
  (into [] (map (fn [_] (util/rand-bool)) (range total-tiles))))

(defn make-light-list
  "Create a list of buttons from the board data structure."
  [vals]
  (map-indexed #(make-light %1 (if %2 "#00A1FF" "#666666")) vals))

(defn make-light
  "Create a button."
  [i color]
  (seesaw/button :listen [:action (fn [_] (update-board i))]
                 :preferred-size [40 :by 40]
                 :background color))

(defn new-game-window
  [board-vals]
  (seesaw/grid-panel :items (make-light-list board-vals) :columns 5))

(defn get-updateable
  "Calculate the indices of the affected tiles."
  [i bsize ttiles]
  (println (str i " " bsize " " ttiles))
  (concat (filter #(and (>= % 0) (< % ttiles))
                  [i (+ i bsize) (- i bsize)])
          (filter #(and (>= % 0) (< % ttiles) (= (quot i bsize) (quot % bsize)))
                  [(+ i 1) (- i 1)])))
  ; (filter #(and (>= % 0) (< % ttiles))
  ;         [i (+ i 1) (- i 1) (+ i bsize) (- i bsize)]))

(defn toggle-cell
  "Toggle the light in board |b| at position |i|."
  [i b]
  (let [opp (not (get b i))]
    (def board-data (assoc board-data i opp))))

(defn update-board
  "Update the board when button |i| is clicked."
  [i]
  (doseq [t (get-updateable i board-size total-tiles)]
    (toggle-cell t board-data))
  (seesaw/config! f :content (new-game-window board-data)))

(defn display
  "Load the board into the frame."
  [content]
  (seesaw/config! f :content content)
  content)

; Entry point.
(defn -main
  "Show the screen"
  [& args]
  (display (new-game-window board-data))
  (-> f pack! show!))
