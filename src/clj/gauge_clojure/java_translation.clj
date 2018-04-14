(ns gauge-clojure.java-translation
  (:require [clojure.string :as string]
            [clojure.reflect :as reflect]
            [clojure.walk :as walk]
            [clojure.java.data :refer [from-java]])
  (:import [gauge.messages.Messages$Message$MessageType]))


(defn camelize [input-string]
  (let [words (string/split input-string #"[\s_-]+")]
    (string/join "" (cons (string/lower-case (first words))
                          (map string/capitalize (rest words))))))

(defn keyword-to-class [k]
  (symbol (apply str (map string/capitalize
                          (string/split (name k) #"-")))))

(defn accessor [prefix k]
  (symbol (str prefix (keyword-to-class k))))

(defn getter
  [k]
  (accessor ".get" k))

(defn setter
  [k]
  (accessor ".set" k))


(defn to-java-message-type [k]
  ;; hmm bad interop here, is there another way?
  (eval (symbol (str "gauge.messages.Messages$Message$MessageType"
                    "/"
                    (keyword-to-class k)))))



(defn message-builder [k]
  ;; hmm bad interop here, is there another way?
  (eval `(~(symbol (str "gauge.messages.Messages$"
                       (keyword-to-class k)
                       "/newBuilder")))))

(defn walk-from-java [instance]
  (let [top (from-java instance)
        f (fn [v]
            ;; hmm crude filter here, is there a better way to filter java objects?
            (if (string/starts-with? (str (type v))
                                         "class gauge.messages")
              (from-java v)
              v))]
    (walk/prewalk f top)))

(defn to-java-message
  ([message-class m]
   (to-java-message "gauge.messages.Messages$" message-class m))
  ([prefix message-class m]
   ;; lots happening here, lots of compromises
   (eval `(->
           (~(symbol (str prefix (keyword-to-class message-class) "/newBuilder")))
           ~@(map (fn [[k v]] (list (setter k) v)) m)
           (.build)
           ))))

(defn proto-message [message-type message-class message-id & {:as attrs}]
  (let [msg-type (to-java-message-type message-type)
        jmsg (to-java-message message-class attrs)
        wrapper (-> (message-builder :message)
                    (.setExecuteStepRequest jmsg)
                    (.setMessageType msg-type)
                    (.setMessageId message-id)
                    (.build))]
    wrapper))

(defmethod from-java gauge.messages.Messages$ExecuteStepRequest [instance]
  {:actual-step-text (.getActualStepText instance)
   :parsed-step-text (.getParsedStepText instance)
   #_...
   })

(defmethod from-java gauge.messages.Messages$Message [instance]
  {:message-id (.getMessageId instance)
   :message-type (.getMessageType instance)
   :execute-step-request (.getExecuteStepRequest instance)})
