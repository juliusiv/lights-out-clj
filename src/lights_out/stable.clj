(ns lights-out.core
  (:gen-class))
(use 'seesaw.core)

(native!)

; Create the frame that everything will be drawn on.
(def f (frame :title "Lights Out"))
; Define the number of lights in the board as a constant.
(def ^:const board-size 5)
(def ^:const total-tiles (* board-size board-size))

(declare update-board)
(declare draw-buttons)
(declare make-button)

; Create a blank board with random tiles turned on.
(def board
  (into [] (map (fn [e] (= (rand-int 2) 1)) (range total-tiles))))

; Create a list of buttons from the board data structure.
(defn draw-buttons [bs]
  (map-indexed #(make-button %1 (if %2 "#00A1FF" "#666666")) bs))

; Create a button.
(defn make-button [i color]
  (button :listen [:action (fn [e] (update-board i))]
          :preferred-size [40 :by 40]
          :background color))

(def button-widget (grid-panel :items (draw-buttons board) :columns 5))
(defn update-button-widget! [bs]
  (def button-widget (grid-panel :items (draw-buttons bs) :columns 5)))

; Calculate the indeices of the affected tiles.
(defn get-updateable [i bsize ttiles]
  (filter #(and (> % 0) (< % ttiles))
          [i (+ i 1) (- i 1) (+ i bsize) (- i bsize)]))

; Toggle the light in board |b| at position |i|.
(defn toggle-cell [i b]
  (def board (assoc board i (not (get board i)))))

(defn print-board []
  (doseq [i board]
    (println i)))

; Update the board when button |i| is clicked.
(defn update-board [i]
  (for [t (get-updateable i board-size total-tiles)]
    (toggle-cell t board))
  (print-board)
  (update-button-widget! board)
  (repaint! button-widget))


; Load the board into the frame.
(defn display [content]
  (config! f :content content)
  content)

; Entry point.
(defn -main
  "Show the screen"
  [& args]
  (update-button-widget! board)
  (display button-widget)
  (-> f pack! show!))
