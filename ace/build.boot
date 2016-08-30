(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.5.2" :scope "test"]])

(require '[boot.task-helpers]
         '[cljsjs.boot-cljsjs.packaging :refer :all])


(def +lib-version+ "1.2.5")
(def +version+ (str +lib-version+ "-0"))

(task-options!
  push {:ensure-clean false}
  pom  {:project     'cljsjs/ace
        :version     +version+
        :description ""
        :url         "https://github.com/ajaxorg/ace"
        :license     {"BSD" "https://opensource.org/licenses/BSD"}
        :scm         {:url "https://github.com/cljsjs/packages"}})

(require '[boot.core :as c]
         '[boot.tmpdir :as tmpd]
         '[clojure.java.io :as io]
         '[clojure.string :as string])

(deftask package []
  (comp
    (download :url (format "https://github.com/ajaxorg/ace-builds/archive/v%s.zip" +lib-version+)
              :checksum "857CB14ED763E5DF75B7535C23C37C1B"
              :unzip true)
    (sift :move {#"^ace-.*/src-min/ace.js" "cljsjs/ace/development/ace.inc.js"})
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.ace")
    (pom)
    (jar)
    ))
