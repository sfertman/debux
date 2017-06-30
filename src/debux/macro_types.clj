(ns debux.macro-types
  (:require [clojure.set :as set]))


(defn- var->symbol [v]
  (let [m    (meta v)
        ns   (str (ns-name (:ns m)))
        name (str (:name m))]
    (symbol ns name) ))

(defn ns-symbol [sym]
  (if-let [v (resolve sym)]
    (var->symbol v)
    sym))


;;; macro management
(def macro-types*
  (atom {:def-type `#{def defonce}
         :defn-type `#{defn defn-}
         :fn-type `#{fn fn*}

         :let-type
         `#{let binding dotimes if-let if-some when when-first when-let
            when-some with-in-str with-local-vars with-open with-out-str
            with-redefs}
         :letfn-type `#{letfn}
         
         :for-type `#{for doseq}
         :case-type `#{case}

         :skip-arg-1-type `#{set! with-precision}
         :skip-arg-2-type `#{as->}
         :skip-arg-1-2-type `#{}
         :skip-arg-1-3-type `#{defmethod}
         :skip-arg-2-3-type `#{amap areduce}
         :skip-form-itself-type
         `#{catch comment declare definline definterface defmacro defmulti
            defprotocol defrecord defstruct deftype extend-protocol
            extend-type finally gen-class gen-interface import loop memfn new
            ns proxy proxy-super quote refer-clojure reify sync var throw}

         :expand-type
         `#{clojure.core/.. -> ->> doto cond-> cond->> condp import some-> some->>}
         :dot-type `#{.} }))

(defn register-macros! [macro-type symbols]
  (swap! macro-types* update macro-type
                             #(set/union % (set symbols)) ))
(defn show-macros
  ([] @macro-types*)
  ([macro-type] (get @macro-types* macro-type)))

