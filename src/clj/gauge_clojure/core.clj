(ns gauge-clojure.core
  (:require [flatland.protobuf.core :as pb])
  (:import [gauge.messages.Api$GetProjectRootRequest]))


(def GetProjectRootRequest (pb/protodef gauge.messages.Api$GetProjectRootRequest))

(comment
  (assoc (pb/protobuf GetProjectRootRequest )
         :a 2))
