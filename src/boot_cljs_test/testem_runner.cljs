(ns boot-cljs-test.testem-runner
  (:require [clojure.string :as str :refer [upper-case]]
            [cljs.test :as test :refer [report]
             :refer-macros [deftest testing is are run-all-tests]]))

(defn fail|error->result [m]
  (let [message (str (upper-case (name (:type m))) ": "
                     (:message m) "\n"
                     (test/testing-vars-str m) "\n"
                     "  expected: " (:expected m) "\n"
                     "  actual:   " (:actual m))]
    #js {:passed 0
         :failed 1
         :total  1
         :name   (test/testing-contexts-str)
         :items  #js [#js {:passed false
                           :message message}]}))

(defn with-testem
  [test-fn]
  (->> (fn [socket]
         (.emit socket "tests-start")

         (defmethod report [::test/default :pass]
           [m]
           (test/inc-report-counter! :pass)
           (let [result
                 #js {:passed 1
                      :failed 0
                      :name   (test/testing-contexts-str)
                      :total  1}]
             (.emit socket "test-result" result)))

         (defmethod report [::test/default :fail]
           [m]
           (test/inc-report-counter! :fail)
           (.emit socket "test-result" (fail|error->result m)))

         (defmethod report [::test/default :error]
           [m]
           (test/inc-report-counter! :error)
           (.emit socket "test-result" (fail|error->result m)))

         (defmethod report [::test/default :summary]
           [_]
           (.emit socket "all-test-results"))

         (defmethod report [::test/default :begin-test-ns] [m])

         (test-fn))
    (.useCustomAdapter js/Testem)))

(defn run-all-tests-with-testem []
  (with-testem #(run-all-tests)))
