 (ns gauge-clojure.handlers
   (:require [gauge-clojure.java-translation :as jt]
             [clojure.java.data :refer [to-java from-java]])
  (:import [gauge.messages
            Messages$ExecuteStepRequest
            Messages$Message
            Messages$Message$MessageType
            Spec$ProtoExecutionResult$ErrorType
            #_....moreclassesomg
            ]))


;; or perhaps
;; (defmulti handle-message :message-type)
(defmulti handle-message #(.getMessageType %))

(defmethod handle-message Messages$Message$MessageType/ExecuteStep
  [message]
  (let [msg (.getExecuteStepRequest message)
        actual-step-text (.getActualStepText msg)
        parsed-step-text (.getParsedStepText msg)
        scenario-failing (.getScenarioFailing msg)
        jparameters (to-java (.getParameters msg)) ;; Spec$Parameter .getName .getValue .getParameterType
        response-builder (partial jt/to-java-message "gauge.messages.Spec$" :proto-execution-result)]

    ;;do stuff...
    (let [{:keys [failed messages error-message]} (identity {:failed false
                                                             :messages ["hi"]
                                                             :error-message "no error"})]
      ;; return response message
      (-> message ;;keeping messageId
          (.toBuilder)
          (.setMessageType Messages$Message$MessageType/ExecutionStatusResponse)
          (.setExecutionStatusResponse
           (-> (gauge.messages.Messages$ExecutionStatusResponse/newBuilder)
               (.setExecutionResult
                (-> (gauge.messages.Spec$ProtoExecutionResult/newBuilder)
                    (.setFailed failed)
                    (.setErrorMessage error-message)
                    (.addMessage messages)
                    (.build)))))
          ))))
