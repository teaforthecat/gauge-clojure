(ns gauge-clojure.handlers-test
  (:require [clojure.test :refer [deftest testing is]]
            [gauge-clojure.handlers :as h])
  (:import [gauge.messages
            Messages$Message$MessageType
            Messages$ExecuteStepRequest
            ]))

(def message-types {
                    :execute-step gauge.messages.Messages$Message$MessageType/ExecuteStep
                    })

(def message-builders {
                       :execute-step-request (gauge.messages.Messages$ExecuteStepRequest/newBuilder)
                      })

(defn message-wrapper-builder [msg-type msg-id]
  (-> (gauge.messages.Messages$Message/newBuilder)
      (.setMessageId msg-id)
      (.setMessageType (get message-types msg-type))))

(defn message-builder [msg-class]
  (let [builder (get message-builders msg-class)]
    builder))


(defn parameter-builder [attrs]
  (let [params (gauge.messages.Spec$Parameter/newBuilder)
        setter (fn [o [k v]] (-> o
                                 (.setName (name k))
                                 ;; skipping parameter type for now
                                 (.setValue v)))]
    (.build (reduce setter params attrs))))

(deftest execute-step
  (let [input-message (-> (message-wrapper-builder :execute-step 123)
                          (.setExecuteStepRequest (-> (message-builder :execute-step-request)
                                                      (.setActualStepText "hello")
                                                      (.addParameters (parameter-builder {:name "world"}))))
                          (.build))
        output-message (h/handle-message input-message)]
    (is (= 1 1))))
