(ns chance-time.dev
    (:require
     [chance-time.core]
     [figwheel.client :as fw]))

(fw/start {
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :on-jsload (fn []
               (chance-time.core/init))})
