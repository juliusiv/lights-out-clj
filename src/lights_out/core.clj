(ns lights-out.core
  (:gen-class)
  (:use [seesaw.core :as seesaw])
  (:use [lights-out.util :as util]))

(native!)

; Create the frame that everything will be drawn on.
(def game-frame (frame :title "Lights Out"))
; Define the number of lights in the board as a constant.
(def ^:const board-size 5)
(def ^:const total-tiles (* board-size board-size))

(declare update-board)
(declare make-light-list)
(declare make-light)

; Create a blank board with random tiles turned on.
(def board-data
  (to-array-2d
    (map (fn [_] (util/rand-bool-coll board-size)) (range board-size))))

(defn make-light-list
  "Create a list of buttons from the board data structure."
  [vals]
  (apply concat
    (util/amap-indexed
      #(make-light %1 %2 (if %3 "#00A1FF" "#666666"))
      vals)))

(defn make-light
  "Create a button."
  [row col color]
  (seesaw/button :listen [:action (fn [_] (update-board row col))]
                 :preferred-size [40 :by 40]
                 :background color))

(defn new-game-window
  "Create a grid of lights based on the values in |board-vals|.
  Note: |board-vals| is a 2d array and must be flattened before it can be used
  with a seesaw function."
  [board-vals]
  (seesaw/grid-panel :items (make-light-list board-vals)
                     :columns 5))

(defn get-updateable
  "Calculate the indices of the affected tiles when the light at |row|, |col| is
  clicked."
  [row col bsize]
  (filter (fn [v] (every? #(<= 0 % (- bsize 1)) v))
          [[row col]
           [(- row 1) col]
           [(+ row 1) col]
           [row (- col 1)]
           [row (+ col 1)]]))

(defn toggle-cell
  "Toggle the light in board |b| at position |i|."
  [row col]
  (let [opp (not (aget board-data row col))]
    (aset board-data row col opp)))

(defn update-board
  "Update the board when button |i| is clicked."
  [row col]
  (doseq [coord (get-updateable row col board-size)]
    (apply toggle-cell coord))
  (seesaw/config! game-frame :content (new-game-window board-data)))

(defn display
  "Load the board into the frame."
  [content]
  (seesaw/config! game-frame :content content)
  content)

; Entry point.
(defn -main
  "Show the screen"
  [& args]
  (display (new-game-window board-data))
  (-> game-frame pack! show!))
