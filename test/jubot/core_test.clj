(ns jubot.core-test
  (:require
    [jubot.system :as sys]
    [jubot.core   :refer :all]
    [conjure.core :refer [stubbing]]
    [clojure.test :refer :all]))

(def ^:private f (jubot :handler "handler" :entries "entries"))

(deftest test-jubot
  (stubbing [sys/start nil]
    (testing "default parameters"
      (let [{:keys [adapter brain scheduler]} (do (f) sys/system)
            {:keys [name handler]} adapter
            {:keys [entries]} scheduler]
        (are [x y] (= x y)
             jubot.adapter.slack.SlackAdapter (type adapter)
             jubot.brain.memory.MemoryBrain (type brain)
             jubot.scheduler.Scheduler (type scheduler)
             DEFAULT_BOTNAME name
             "handler" handler
             "entries" entries)))

    (testing "specify adapter and botname"
      (let [{:keys [adapter brain scheduler]} (do (f "-n" "foo" "-a" "repl")
                                                sys/system)]
        (are [x y] (= x y)
             jubot.adapter.repl.ReplAdapter (type adapter)
             "foo" (:name adapter))))))
