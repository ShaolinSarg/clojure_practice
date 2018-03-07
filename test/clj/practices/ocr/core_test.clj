(ns practices.ocr.core-test
  (:require [practices.ocr.core :as t]
            [clojure.test :refer [deftest testing is are]]
            [clojure.set :refer [subset?]]))

(def account-one '("    _  _     _  _  _  _  _ "
                   "  | _| _||_||_ |_   ||_||_|"
                   "  ||_  _|  | _||_|  ||_| _|"
                   "                           "))
(def account-one-error '("    _  _     _  _  _  _  _ "
                         "  | _| _||_||_ |_   ||_||_|"
                         "  ||_  _|  | _ |_|  ||_| _|"
                         "                           "))
(def account-one-errors '("    _  _     _  _  _  _    "
                          "  | _| _||_||_ |_   ||_||_|"
                          "  ||_  _|  | _ |_|  ||_| _|"
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
    (is (= [{:raw account-one
             :initial-read "123456789"
             :status :OK}
            {:raw account-two
             :initial-read "089356712"
             :status :ERR}]
           (t/process-file two-accounts)))))

(deftest read-result
  (testing "return the right status for the given number"
    (are [expected actual] (= expected actual)
      :ERR (t/read-result "664371495")
      :ILL (t/read-result "86110??36")
      :OK (t/read-result "457508000"))))

(deftest read-results
  (testing "output the result of the read"
    (is (= '("457508000 OK" "664371495 ERR" "86110??36 ILL")
           (t/read-results '("457508000" "664371495" "86110??36"))))))

(deftest ocr-tests
  (testing "ocr initial-read"
    (are [expected actual] (= expected actual)
      "123456789" (:initial-read (t/ocr account-one))
      "089356712" (:initial-read (t/ocr account-two))
      "86110??36" (:initial-read (t/ocr account-error))))
  (testing "raw-input"
    (is (= account-one (:raw (t/ocr account-one)))))
  (testing "status"
    (is (= :OK (:status (t/ocr account-one))))))

(deftest get-char-tests
  (testing "get a character"
    (are [expected actual] (= expected actual)
      t/one (t/get-char 0 account-one)
      t/two (t/get-char 1 account-one)
      t/zero (t/get-char 0 account-two)
      t/one (t/get-char 7 account-two))))

(deftest parse-input-tests
  (testing "successfully parsing a single string" 
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
      9 (t/parse-input t/nine))))

(deftest valid-checksum-tests
  (testing "valid checksums"
    (is (true? (t/valid-checksum? "345882865")))
    (is (true? (t/valid-checksum? "457508000"))))
  (testing "invalid checksum"
    (is (false? (t/valid-checksum? "664371495")))))

(deftest pixel-variants-tests
  (testing "return other values"
    (are [expected actual] (= expected actual)
      #{\space \|} (t/pixel-variants \_)
      #{\_ \|}     (t/pixel-variants \space)
      #{\space \_} (t/pixel-variants \|))))

(deftest char-variants
  (testing "return characters with one varied pixel"
    (is (empty? (t/char-variants "")))
    (is (subset? #{" " "_"} (t/char-variants "|")))
    (is (subset? #{"_" "|"} (t/char-variants " ")))
    (is (subset? #{"_ " "| " " _" " |"} (t/char-variants "  ")))))

(deftest alternative-numbers-tests
  (testing "identify correct other number variants"
    (are [expected actual] (= expected actual)
      #{7} (t/alternative-numbers "     |  |")
      #{1} (t/alternative-numbers " _   |  |")
      #{8} (t/alternative-numbers " _ | ||_|")
      #{7} (t/alternative-numbers " _   |   ")
      #{1} (t/alternative-numbers "        |"))
    (is (subset? #{0 6 9} (t/alternative-numbers " _ |_||_|")))))

(deftest fix-misread
  (testing "single missing read"
    (is (= ["123456789"] (t/fix-error account-one-error)))
    (is (= ["123456789"] (t/fix-error account-one-errors)))))
