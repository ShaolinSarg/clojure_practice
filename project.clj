(defproject practices "0.1.0-SNAPSHOT"
  :description "My collection of katas written in Clojure "

  :url "https://github.com/ShaolinSarg/clojure_practice"

  :licence {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/test.check "0.10.0-alpha4"]
                 [expound "0.7.2"]
                 [metrics-clojure "2.10.0"]]

  :target "/target"

  :source-paths #{"src/clj"}

  :profiles {:test {:source-paths #{"test/clj"}}
             :uberjar {:aot :all}})
