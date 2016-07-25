(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs        "0.5.2" :scope "test"]
                  [degree9/boot-polymer      "0.2.0-SNAPSHOT" :scope "test"]
                  [cljsjs/webcomponents-lite "0.7.21-1"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all]
         '[degree9.boot-polymer :refer :all])

(def +lib-version+ "1.6.0")
(def +version+ (str +lib-version+ "-0"))

(task-options!
 pom  {:project     'cljsjs/polymer
       :version     +version+
       :description "Polymer packaged for clojurescript."
       :url         "https://www.polymer-project.org/1.0/"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"BSD" "https://github.com/Polymer/polymer/blob/master/LICENSE.txt"}})

(deftask package []
  (comp
    (download :url (str "https://github.com/Polymer/polymer/archive/v" +lib-version+ ".zip")
              :checksum "E4B83BA6B6240B70FB3E9264E2085462"
              :unzip true)
    (vulcanize :input (str "polymer-" +lib-version+ "/polymer.html")
               :html true
               :output "vulcanized.html")
    (crisper :input "vulcanized.html" :html "crisper.html" :javascript "polymer.js")
    (sift :move {#"polymer.js" "cljsjs/development/polymer.inc.js"})
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.polymer"
               :requires ["cljsjs.webcomponents-lite"])
    (pom)
    (jar)))
