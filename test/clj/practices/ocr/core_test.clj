(ns practices.ocr.core-test
  (:require [practices.ocr.core :as t]
            [clojure.test :refer [deftest testing is are]]))

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
      t/one (t/get-char 0 account-one)
      t/two (t/get-char 1 account-one)
      t/zero (t/get-char 0 account-two)
      t/one (t/get-char 7 account-two)))

  (testing "parse-input" 
    (are [expected actual] (= expected actual)
      0 (t/parse-input t/zero)
      1 (t/parse-input t/one)
      2 (t/parse-input t/two)
      3 (t/parse-input t/three)
      4 (t/parse-input t/four)
      5 (t/parse-input t/five)
      6 (t/parse-input t/six)
      7 (t/parse-input t/seven)
      8 (t/parse-input t/eight)
      9 (t/parse-input t/nine)))

  (testing "validate-checksum"
    (is (= true (t/valid-checksum? "345882865")))
    (is (= true (t/valid-checksum? "457508000")))
    (is (= false (t/valid-checksum? "664371495"))))

  (testing "pixel-variants"
    (are [expected actual] (= expected actual)
      '(\space \|) (t/pixel-variants \_)
      '(\_ \|)     (t/pixel-variants \space)
      '(\space \_) (t/pixel-variants \|)))

  (testing "char-variants"
    (are [expected actual] (= expected actual)
        '() (t/char-variants "")
        '(" " "_") (t/char-variants "|")
        '("_" "|") (t/char-variants " ")
        '("_ " "| " " _" " |") (t/char-variants "  ")))

  (testing "alternative-numbers"
    (are [expected actual] (= expected actual)
      '(7) (t/alternative-numbers "     |  |")
      '(1) (t/alternative-numbers " _   |  |")
      '(8) (t/alternative-numbers " _ | ||_|")
      '(0 6 9) (t/alternative-numbers " _ |_||_|")
      '(7) (t/alternative-numbers " _   |   ")
      '(1) (t/alternative-numbers "        |"))))
