(ns practices.ocr.core)

;; {:raw-account
;;  :status ""
;;  :alternatives []}

(def zero  " _ | ||_|")
(def one   "     |  |")
(def two   " _  _||_ ")
(def three " _  _| _|")
(def four  "   |_|  |")
(def five  " _ |_  _|")
(def six   " _ |_ |_|")
(def seven " _   |  |")
(def eight " _ |_||_|")
(def nine  " _ |_| _|")

(def valid-numbers {zero 0
                    one 1
                    two 2
                    three 3
                    four 4
                    five 5
                    six 6
                    seven 7
                    eight 8
                    nine 9})

;; convert 4 row to single string
;; convert string to number
;; check for alternates


(defn get-char
  "get the 3 row values that make one character"
  [index raw-data]
  (apply str (take 3 (map #(apply str (nth (partition 3 3 %) index))
                          raw-data))))

(defn parse-input
  "convert a 4 line ocr image to a number"
  [ocr-image]
  (get valid-numbers ocr-image "?"))

(defn ocr
  "read a single account number from the input"
  [raw-data]
  (apply str (for [index (range 9)]
               (parse-input (get-char index raw-data)))))

(defn process-file
  "take a list of different account data and return account numbers"
  [file-lines]
  (->> (partition 4 4 file-lines)
       (map #(ocr %))))

(defn valid-checksum?
  "validate the checksum of an account number"
  [account-number]
  ;account number:  3  4  5  8  8  2  8  6  5
  ;position names:  d9 d8 d7 d6 d5 d4 d3 d2 d1
  ;(d1+2*d2+3*d3+...+9*d9) mod 11 = 0
  (let [weights (take 9 (iterate dec 9))
        account-numbers (map #(Integer/parseInt (str %))
                             account-number)
        weighted (map * account-numbers weights)
        combined (apply + weighted)]

    (= 0 (mod combined 11))))

(defn read-results
  "indicates the result of parsing the account representation"
  [accounts]
  (map (fn [v]
         (cond
           (< (count (remove #(= \? %) v)) 9) (str v " ILL")
           (false? (valid-checksum? v)) (str v " ERR")
           :else (str v))) accounts))

(defn pixel-variants
  "returns the alternative values of the given value"
  [c]
  (cond
    (= \_ c) '(\space \|)
    (= \space  c) '(\_ \|)
    (= \|  c) '(\space \_)))

(defn char-variants
  [current]
  (let [orig (vec current)]
    (if (empty? orig)
      '()
      (for [i (range 0 (count orig))
            v (pixel-variants (nth orig i))]
        (apply str (assoc orig i v))))))


(defn alternative-numbers
  "given a representaton returns numbers that have one more or less char"
  [candidate]
  (let [combinations (char-variants candidate)]
    (remove #(= "?" %)
            (map #(parse-input %)
                 combinations))))
