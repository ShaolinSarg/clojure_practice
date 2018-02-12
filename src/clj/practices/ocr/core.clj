(ns practices.ocr.core)

(def valid-numbers {
                    '(" _ " "| |" "|_|" "   ") 0
                    '("   " "  |" "  |" "   ") 1
                    '(" _ " " _|" "|_ " "   ") 2
                    '(" _ " " _|" " _|" "   ") 3
                    '("   " "|_|" "  |" "   ") 4
                    '(" _ " "|_ " " _|" "   ") 5
                    '(" _ " "|_ " "|_|" "   ") 6
                    '(" _ " "  |" "  |" "   ") 7
                    '(" _ " "|_|" "|_|" "   ") 8
                    '(" _ " "|_|" " _|" "   ") 9})

(defn get-char
  "get the 3 row values that make one character"
  [index raw-data]
  (map  #(apply str (nth (partition 3 3 %) index))
        raw-data))

(defn parse-input
  "convert a 4 line ocr image to a number"
  [ocr-image]
  (get valid-numbers ocr-image))

(defn ocr
  "read a single account number from the input"
  [raw-data]
  (apply str (for [index (range 9)] (parse-input (get-char index raw-data)))))

(defn process-file
  "take a list of different account data and return account numbers"
  [file-lines]
  (->> (partition 4 4 file-lines)
       (map #(ocr %))))

(defn checksum
  "calculated the checksum of an account number"
  [account-number]
  ;account number:  3  4  5  8  8  2  8  6  5
  ;position names:  d9 d8 d7 d6 d5 d4 d3 d2 d1
  ;(d1+2*d2+3*d3+...+9*d9) mod 11 = 0
  (let [weights (take 9 (iterate inc 1))
        account-numbers (map #(Integer/parseInt (str %))
                             (reverse account-number))
        weighted (map * account-numbers weights)
        combined (apply + weighted)]

    (mod combined 11)))
