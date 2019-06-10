(ns practices.roman-numerals.numerals)

(def numerals [["M" 1000]
               ["CM" 900]
               ["D" 500]
               ["CD" 400]
               ["C" 100]
               ["XC" 90]
               ["L" 50]
               ["XL" 40]
               ["X" 10]
               ["IX" 9]
               ["V" 5]
               ["IV" 4]
               ["I" 1]])

(defn convert-arabic [arabic]
  (loop [out ""
         rem arabic
         nums numerals]

    (if (seq nums)
      (let [[n v] (first nums)]
        (if (>= rem v)
          (recur (str out n) (- rem v) nums)
          (recur out rem (rest nums)))) 
      out)))

(convert-arabic 10)

;;1732 would be denoted MDCCXXXII
