(defproject gauge-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 ]

  :profiles {:dev
             {
              ;; :dependencies [[org.flatland/protobuf "0.8.2"]]
              :plugins [[lein-protobuf "0.5.0"]]}})
