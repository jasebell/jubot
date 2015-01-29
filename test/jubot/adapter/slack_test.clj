(ns jubot.adapter.slack-test
  (:require
    [jubot.adapter.slack :refer :all]
    [conjure.core        :refer [stubbing]]
    [midje.sweet         :refer :all]
    [clojure.data.json   :as json]))

(def ^:private botname "test")
(def ^:pricate this (->SlackAdapter botname))
(defn- handler [_ text] (str "[" text "]"))
(defn- text [& s] {:text (apply str s)})

(def ^:private test-process-input (partial process-input this))

(facts "process-input should work fine."
  (fact "ignore nil input"
    (test-process-input handler {:text nil}) => "")


  (fact "ignore message from slackbot"
    (test-process-input handler {:text (str botname " foo")
                        :user_name "slackbot"}) => "")

  (fact "ignore message which is not addressed to bot"
    (test-process-input handler {:text "foo"}) => "")

  (fact "handler function returns nil"
    (test-process-input
      (constantly nil)
      {:text (str botname " foo")}) => "")

  (fact "handler function returns string"
    (test-process-input
      handler
      {:text (str botname " foo")}) => (json/write-str {:text "[foo]"}))
  )

;; TODO: output-process