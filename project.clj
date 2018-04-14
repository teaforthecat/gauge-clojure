(defproject gauge-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojars.brunchboy/protobuf "0.8.3"]
                 [org.clojure/java.data "0.1.1"]
                 [aleph "0.4.4"]
                 ]

  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]

  :proto-path "gauge-proto"
  ;; this I'm sure will have to be changed for you, I appologize
  ;; downloaded from https://github.com/google/protobuf/releases/download/v3.5.1/protoc-3.5.1-win32.zip
  ;; unzipped to  C:/Program Files/protoc-3.5.1
  :protoc "C:/Program Files/protoc-3.5.1/bin/protoc"
  ;; now we can run lein protobuf and see class files in target/protosrc/gauge/messages
  ;; then mv target/protosrc/gauge/messages to src/java/gauge/messages
  ;; these are our pojos that we can import

  :profiles {:dev
             {
              :dependencies [[com.google.protobuf/protobuf-java "3.5.1"]]
              :plugins [[lein-protobuf "0.5.0"]]}})
