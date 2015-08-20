(ns boot-cljs-test.testem
  (:require [clojure.java.io :as io]
            [boot.core :as core :refer [deftask]]
            [boot.util :as util :refer [sh]]
            [boot.file :as file]
            [clojure.data.json :as json]))

(defn mk-parents [file]
  (-> file .getParent io/file .mkdirs))

(deftask setup-testem
  "Automatically produces:

  - a resource file `testem.json` for testem config.
  - a testem_runner.cljs.edn file to instruct `boot-cljs` to build the file targeting testem.

  Should be called before `boot-cljs` task."
  [n namespaces    NS   #{sym} "Namespaces whose tests will be run."
   c testem-config OPTS edn    "testem configuration."]
  (comp (core/with-pre-wrap [fileset]
          (let [test-dir (core/temp-dir!)
                data     (-> testem-config
                           (merge {:framework "custom"
                                   :src_files ["testem_runner.js"]
                                   :ignore_missing_launchers true}))
                output   (io/file test-dir "testem.json")
                content  (json/write-str data)]
            (file/empty-dir! test-dir)
            (doto output
              (mk-parents)
              (spit content))
            (-> fileset
              (core/add-resource test-dir)
              (core/commit!))))
        (core/with-pre-wrap [fileset]
          (let [test-dir (core/temp-dir!)
                data     {:require  (conj (vec namespaces)
                                          'boot-cljs-test.testem-runner)
                          :init-fns
                          '[boot-cljs-test.testem-runner/run-all-tests-with-testem]
                          :compiler-options {}}
                output   (io/file test-dir "testem_runner.cljs.edn")
                content  (pr-str data)]
            (file/empty-dir! test-dir)
            (doto output
              (mk-parents)
              (spit content))
            (-> fileset
              (core/add-source test-dir)
              (core/commit!))))))
