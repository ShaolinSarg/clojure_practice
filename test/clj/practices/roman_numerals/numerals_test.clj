(ns practices.roman-numerals.numerals-test
  (:require [practices.roman-numerals.numerals :as sut]
            [clojure.test :refer [deftest are testing]]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.string :as str]))


(deftest roman-converter-test
  (testing "simple arabic mappings"
    (are [expected actual] (= expected actual)
      ""  (sut/convert-arabic 0)
      "I"  (sut/convert-arabic 1)
      "IV" (sut/convert-arabic 4)
      "V"  (sut/convert-arabic 5)
      "IX"  (sut/convert-arabic 9)
      "X"  (sut/convert-arabic 10)
      "XL"  (sut/convert-arabic 40)
      "L"  (sut/convert-arabic 50)
      "XC"  (sut/convert-arabic 90)
      "C"  (sut/convert-arabic 100)
      "CD"  (sut/convert-arabic 400)
      "D"  (sut/convert-arabic 500)
      "CM"  (sut/convert-arabic 900)
      "M"  (sut/convert-arabic 1000)))
  (testing "repeating numerals"
    (are [expected actual] (= expected actual)
      "II" (sut/convert-arabic 2)
      "III" (sut/convert-arabic 3)
      "XX" (sut/convert-arabic 20))))


(def arabic-gen (gen/such-that (complement zero?) gen/nat))

(defspec roman-repeating-props
  1000
  (prop/for-all [arabic arabic-gen]
                ;; The symbols 'I', 'X', 'C', and 'M' can be repeated at most 3 times in a row
                (not (str/includes? (sut/convert-arabic arabic) "IIII"))
                (not (str/includes? (sut/convert-arabic arabic) "XXXX"))
                (not (str/includes? (sut/convert-arabic arabic) "CCCC"))
                (not (str/includes? (sut/convert-arabic arabic) "MMMM"))

                ;; The symbols 'V', 'L', and 'D' can never be repeated
                (not (str/includes? (sut/convert-arabic arabic) "VV"))
                (not (str/includes? (sut/convert-arabic arabic) "LL"))
                (not (str/includes? (sut/convert-arabic arabic) "DD"))))

(defspec roman-valid-subtraction-props
  1000
  (prop/for-all [arabic arabic-gen]
                ;; The '1' symbols ('I', 'X', and 'C') can only be subtracted from the 2 next highest
                ;; values ('IV' and 'IX', 'XL' and 'XC', 'CD' and 'CM')
                (not (str/includes? (sut/convert-arabic arabic) "IL"))
                (not (str/includes? (sut/convert-arabic arabic) "IC"))
                (not (str/includes? (sut/convert-arabic arabic) "ID"))
                (not (str/includes? (sut/convert-arabic arabic) "IM"))

                (not (str/includes? (sut/convert-arabic arabic) "XD"))
                (not (str/includes? (sut/convert-arabic arabic) "XM"))))


(defspec roman-single-subtraction-props
  1000
  (prop/for-all [arabic arabic-gen]
                ;; Only one subtraction can be made per numeral
                (not (str/includes? (sut/convert-arabic arabic) "IIV"))
                (not (str/includes? (sut/convert-arabic arabic) "IIX"))

                (not (str/includes? (sut/convert-arabic arabic) "XXL"))
                (not (str/includes? (sut/convert-arabic arabic) "XXC"))

                (not (str/includes? (sut/convert-arabic arabic) "CCD"))
                (not (str/includes? (sut/convert-arabic arabic) "CCM"))))

