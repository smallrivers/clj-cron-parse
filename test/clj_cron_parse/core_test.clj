(ns clj-cron-parse.core-test
  (:require [clj-cron-parse.core :refer :all]
            [midje.sweet :refer :all]
            [clj-time.core :as t]))

(defchecker date [& date-args]
            (checker [actual]
                     (let [d (apply t/date-time date-args)]
                       (= d actual))))

(def now (t/date-time 2015 01 01 12 00 00 000))
(def nye (t/date-time 2014 12 31 12 00 00 000))

(facts "should find next date for cron expression"
       (next-date now "1 * * * * *") => (date 2015 01 01 12 00 01 000)
       (next-date now "* 1 * * * *") => (date 2015 01 01 12 01 00 000)
       (next-date now "* * 13 * * *") => (date 2015 01 01 13 00 00 000)
       (next-date now "* * * 10 * *") => (date 2015 01 10 12 00 00 000)
       (next-date now "* * * * 2 *") => (date 2015 02 01 12 00 00 000)
       (next-date now "11 12 13 14 10 *") => (date 2015 10 14 13 12 11 000)
       (next-date now "* * * * * 0") => (date 2015 01 04 12 00 00 000)
       (next-date now "* * * * * 3") => (date 2015 01 07 12 00 00 000)
       (next-date nye "* * 10 * * *") => (date 2015 01 01 10 00 00 000)
       (next-date now "1,2 * * * * *") => (date 2015 01 01 12 00 01 000)
       (next-date now "* 1,2 * * * *") => (date 2015 01 01 12 01 00 000)
       (next-date now "* * 1,2 * * *") => (date 2015 01 02 01 00 00 000)
       (next-date now "* * * 1,2 * *") => (date 2015 01 02 12 00 00 000)
       (next-date now "* * * * 2,3 *") => (date 2015 02 01 12 00 00 000)
       (next-date now "* * * * * 1,2") => (date 2015 01 05 12 00 00 000)
       )

;; close to new year, ranges, L, W, #

(facts "should return nil for an invalid cron expression"
       (next-date now "x * * * * *") => nil
       (next-date now "* x * * * *") => nil
       (next-date now "* * x * * *") => nil
       (next-date now "* * * x * *") => nil
       (next-date now "* * * * x *") => nil
       (next-date now "* * * * * x") => nil
       (next-date now "61 * * * * *") => nil
       (next-date now "* 61 * * * *") => nil
       (next-date now "* * 25 * * *") => nil
       (next-date now "* * * 32 * *") => nil
       (next-date now "* * * * 13 *") => nil
       (next-date now "* * * * * * *") => nil
       (next-date now "1,62 * * * * *") => nil
       (next-date now "* 1,62 * * * *") => nil
       (next-date now "* * 1,25 * * *") => nil
       (next-date now "* * * 1,32 * *") => nil
       (next-date now "* * * * 2,13 *") => nil
       (next-date now "* * * * * 1,8") => nil
       (next-date now "s s") => nil
       (next-date now "") => nil
       )