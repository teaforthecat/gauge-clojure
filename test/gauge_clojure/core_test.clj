(ns gauge-clojure.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.java.data :refer [from-java to-java]]
            [aleph.tcp :as tcp]
            [manifold.stream :as ms]
            [byte-streams :as bs]
            [gauge-clojure.core :as core]
            [gauge-clojure.handlers :as handlers]
            [gauge-clojure.java-translation :as jt])
  (:import [com.google.protobuf.CodedOutputStream]
           [java.io InputStream ByteArrayOutputStream ByteArrayInputStream]
           [java.nio ByteBuffer]

           [gauge.messages
            Messages$ExecuteStepRequest
            Messages$Message
            Messages$Message$MessageType
            #_....moreclassesomg]
           ))

(defmulti handle-message :message-type)
(defmethod handle-message :execute-step
  [{:keys [message-id message-type execute-step-request]}]
  :wat)


(defn parse-message [bz]
  (gauge.messages.Messages$Message/parseFrom bz))


(deftest streaming
  (testing "ExecuteStepRequest"
    (let [socket (ms/stream)
          msg (jt/proto-message :execute-step
                                :execute-step-request
                                123
                                :actual-step-text "abc")
          _ (ms/put! socket (.toByteArray msg))
          result (-> @(ms/take! socket)
                     parse-message
                     (handlers/handle-message))]
      ;; testing that the message made it through the ring and nothing blew up
      (is (= (.getMessageId msg) (.getMessageId result)))

      #_(is (= ["abc"] (-> @(ms/take! socket)
                       parse-message
                       (handlers/handle-message)
                       ;; skipping toByteArray here
                       ;; this is the other side of the looking glass, what gauge sees

                       ;; parse-message
                       (.getExecutionStatusResponse)
                       (.getExecutionResult)
                       (.getMessageList)
                       )))
      ;; also perhaps this:
      #_(is (= "abc" (get-in (parse-message @(ms/take! socket))
                           [:execute-step-request :actual-step-text])))
      )))
