(set-env!
 :resource-paths #{"resources"}
 :source-paths   #{"src"}
 :dependencies   '[[org.clojure/data.json "0.2.6"]
                   [adzerk/bootlaces "0.1.10" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
 pom {:project     'boot-cljs-test/testem-runner
      :version     +version+
      :description "Boot task producing a script to test ClojureScript
      namespaces with Testem."
      :url         "https://github.com/boot-cljs-test/testem-runner"
      :scm         {:url "https://github.com/boot-cljs-test/testem-runner"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})
