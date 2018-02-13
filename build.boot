(set-env!
 :source-paths #{"src/clj"}

 :dependencies '[[org.clojure/clojure "1.9.0"]
                 [adzerk/boot-test "1.2.0"]
                 [org.clojure/tools.nrepl "0.2.13"]])

(require '[adzerk.boot-test :refer [test]])

(deftask add-source-paths
  "Add paths to the :source-paths environment variable"
  [t dirs PATH #{str} ":source-paths"]
  (merge-env! :source-paths dirs)
  identity)

(deftask tdd
  "Launch a customisable TDD environment"
  []
  (comp
   (add-source-paths :dirs #{"test/clj"})
   (watch)
   (test)
   (target :dir #{"target"})))
