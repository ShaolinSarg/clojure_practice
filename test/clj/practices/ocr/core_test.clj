(ns practices.ocr.core-test
  (:require [practices.ocr.core :as t]
            [clojure.test :refer [deftest testing is are]]))

(def zero  '(" _ " "| |" "|_|" "   "))
(def one   '("   " "  |" "  |" "   "))
(def two   '(" _ " " _|" "|_ " "   "))
(def three '(" _ " " _|" " _|" "   "))
(def four  '("   " "|_|" "  |" "   "))
(def five  '(" _ " "|_ " " _|" "   "))
(def six   '(" _ " "|_ " "|_|" "   "))
(def seven '(" _ " "  |" "  |" "   "))
(def eight '(" _ " "|_|" "|_|" "   "))
(def nine  '(" _ " "|_|" " _|" "   "))

(def account-one '("    _  _     _  _  _  _  _ "
                   "  | _| _||_||_ |_   ||_||_|"
                   "  ||_  _|  | _||_|  ||_| _|"
                   "                           "))
(def account-two '(" _  _  _  _  _  _  _     _ "
                   "| ||_||_| _||_ |_   |  | _|"
                   "|_||_| _| _| _||_|  |  ||_ "
                   "                           "))
(def account-error '(" _  _        _  _  _  _  _ "
                     "|_||_   |  || ||      _||_ "
                     "|_||_|  |  ||_||_|  | _||_|"
                     "                           "))

(def two-accounts
  (concat account-one account-two))

(deftest file-process-tests
  (testing "reading multiple lines"
    (is (= '("123456789" "089356712") (t/process-file two-accounts)))))

(deftest read-results
  (testing "output the result of the read"
    (is (= '("457508000" "664371495 ERR" "86110??36 ILL")
           (t/read-results '("457508000" "664371495" "86110??36"))))))

(deftest ocr-tests
  (testing "ocr account one"
    (is (= "123456789" (t/ocr account-one)))
    (is (= "089356712" (t/ocr account-two)))
    (is (= "86110??36" (t/ocr account-error))))

  (testing "get a character"
    (are [expected actual] (= expected actual)
      one (t/get-char 0 account-one)
      two (t/get-char 1 account-one)
      zero (t/get-char 0 account-two)
      one (t/get-char 7 account-two)))

  (testing "parse-input"
    (are [expected actual] (= expected actual)
      0 (t/parse-input zero)
      1 (t/parse-input one)
      2 (t/parse-input two)
      3 (t/parse-input three)
      4 (t/parse-input four)
      5 (t/parse-input five)
      6 (t/parse-input six)
      7 (t/parse-input seven)
      8 (t/parse-input eight)
      9 (t/parse-input nine)))

  (testing "calculate-checksum"
    (is (= 0 (t/checksum "345882865")))
    (is (= 0 (t/checksum "457508000")))
    (is (= 2 (t/checksum "664371495")))))
