;;
;; To build and develop:
;;
;; lein do clean, cljsbuild once dev
;;
;; Cider:
;; - open a cljs file
;; - M-c M-S-j  (jack-in with cljs repl), choose the figwheel repl type
;;
;; Open the app on localhost:3449, the repl will show up
;;

(defproject ohno "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [reagent "0.8.1" :excludes ["cljsjs/react-dom" "cljsjs/react-dom-server" "cljsjs/react" ]]
                 ;; pull in the latest version of react
                 [cljsjs/react "16.8.3-0"]
                 [cljsjs/react-dom "16.8.3-0"]
                 [cljsjs/react-dom-server "16.8.3-0"]
                 [re-frame "0.10.6"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev
   {:source-paths ["dev" "test/cljs" "test/clj"]

    :dependencies [[binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.3.7"]
                   [day8.re-frame/tracing "0.5.1"]
                   [figwheel-sidecar "0.5.18"]
                   [cider/piggieback "0.4.0"]]

    :plugins      [[lein-figwheel "0.5.16"]
                   [lein-doo "0.1.8"]]}
   
   :prod {:dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "ohno.core/mount-root"}
     :compiler     {:main                 ohno.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           day8.re-frame-10x.preload]
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true
                                           "day8.re_frame.tracing.trace_enabled_QMARK_" true}
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            ohno.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:main          ohno.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :optimizations :none}}
    ]}
  )
