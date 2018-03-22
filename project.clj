(defproject practices "0.1.0-SNAPSHOT"
  :description "My collection of katas written in clojure "

  :url "https://github.com/ShaolinSarg/clojure_practice"

  :licence {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :dependencies [[org.clojure/clojure "1.9.0"]]

  :target "/target"

  :source-paths #{"src/clj"}

  :profiles {:uberjar {:aot :all}})
