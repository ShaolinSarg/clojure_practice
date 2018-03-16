(set-env!
 :source-paths #{"src/clj"}

 :dependencies '[[org.clojure/clojure "1.9.0"]
                 [adzerk/boot-test "1.2.0"]
                 [org.clojure/tools.nrepl "0.2.13"]
                 [tolitius/boot-check "0.1.9"]
                 [org.clojure/test.check "0.9.0" :scope "test"]])

(require '[adzerk.boot-test :refer [test]]
         '[tolitius.boot-check :as check])

(deftask add-source-paths
  "Add paths to the :source-paths environment variable"
  [t dirs PATH #{str} ":source-paths"]
  (merge-env! :source-paths dirs)
  identity)

(deftask tdd
  "Launch a customisable TDD environment"
  [t dirs PATH #{str} ":source-paths"]
  (comp
   (add-source-paths :dirs #{"test/clj"})
   (watch)
   (test :namespaces '#{practices.ocr.core-test})
   (target :dir #{"target"})))

(deftask check-source []
  (set-env! :source-paths #{"src/clj" "test/clj"})
  (comp
   (check/with-yagni)
   (check/with-eastwood)
   (check/with-kibit)
   (check/with-bikeshed)))
