(ns debux.cs.test.main
  (:require [debux.cs.core :as d :refer-macros [clog clogn dbg dbgn break]])
  (:require-macros [debux.cs.test.macros :as m] ))


(comment
  
;; simple example
(clogn (defn foo [a b & [c]]
        (if c
          (* a b c)
          (* a b 100))))

(foo 2 3)
(foo 2 3 10)


  
;;; :def-type example
(clogn (def my-function "my function doc-string"
        (fn [x] (* x x x))))

(my-function 10)


;;; :defn-type example
(clogn (defn add
        "add doc-string"
        [a b]
        (+ a b)))

(add 10 20)


(clogn (defn my-add
        "add doc-string"
        ([] 0)
        ([a] a)
        ([a b] (+ a b))
        ([a b & more] (apply + a b more))))


(my-add)
(my-add 10)
(my-add 10 20)
(my-add 10 20 30 40)

(clogn (defn calc1 [a1 a2] (+ a1 a2)))
(clogn (defn calc2 [s1 s2] (- 100 (calc1 s1 s2))))
(clogn (defn calc3 [m1 m2] (* 10 (calc2 m1 m2))))

(calc3 2 5)


  
;;; :fn-type example
(clogn (reduce (fn [acc i] (+ acc i)) 0 [10 20 30]))
(clogn (map #(* % 10) [1 2 3]))



;;; :let-type
(clogn (let [a (+ 1 2)
            [b c] [(+ a 10) (* a 2)]] 
         (- (+ a b) c)))


;;; :letfn-type
(clogn (letfn [(twice [x]
                (* x 2))
              (six-times [y]
                (* (twice y) 3))]
        (six-times 15)))


  
;;; :for-type example
(clogn (for [x [0 1 2 3 4 5]
            :let [y (* x 3)]
            :when (even? y)]
        y))

;;; :case-type example
(clogn (let [mystr "hello"]
        (case mystr
          "" 0
          "hello" (count mystr))))

(clogn (case 'a
        (x y z) "x, y, or z"
        "default"))

;;; :skip-arg-1-type example
;(clogn (with-precision 10 (/ 1M 6)))

(clog (let [a (take 5 (range))
           {:keys [b c d] :or {d 10 b 20 c 30}} {:c 50 :d 100}
           [e f g & h] ["a" "b" "c" "d" "e"]]
       [a b c d e f g h]))
; => [(0 1 2 3 4) 20 50 100 "a" "b" "c" ("d" "e")]

  

;;; :skip-arg-2-type example
(clogn (as-> 0 n
        (inc n)
        (inc n)))


  
;;; :skip-arg-2-3-type example
(def a #js [1 2 3])
(clogn (areduce a i ret 0 (+ ret (aget a i))))


;;; :skip-arg-1-3-type example

(defmulti greeting
  (fn [x] (:language x)))

(clogn (defmethod greeting :english [map]
        (str "English greeting: " (:greeting map))))

(clogn (defmethod greeting :french [map]
        (str "English greeting: " (:greeting map))))

(def english-map {:language :english :greeting "Hello!"})
(def french-map {:language :french :greeting "Bonjour!"})

(greeting english-map)
(greeting french-map)


;;; :expand-type example
(clogn (-> "a b c d" 
          .toUpperCase 
          (.replace "A" "X") 
          (.split " ") 
          first))

(clogn (.. "a b c d"
           toUpperCase
           (replace "A" "X")))


(let [x 1 y 2]
  (clogn (cond-> []
          (odd? x) (conj "x is odd")
          (zero? (rem y 3)) (conj "y is divisible by 3")
          (even? y) (conj "y is even"))))

;;; :dot-type example
(clogn (. (js/Date.) getMonth))


;;; macro registering: now working in cljs
;; (d/register-macros! :let-type `[m/my-let])
;; (d/show-macros :let-type)
;; (d/show-macros)

;; (dbgn (my-let [a 10] (+ a 20)))

) ; end of comment
