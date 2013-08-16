(defproject lein-marginalia-plus "0.8.0-SNAPSHOT"
  :description "A set of extensions to marginalia and lein-marginalia to allow an optional vertical layout with
                a much more prose-friendly display.  A new command is added, lein-marg-plus, which allows a marginalia
                layout to be specified with a new command-line option, -l --layout, as either \"vertical\"
                or \"parallel\" ."
  :url "None"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [marginalia "0.8.0-SNAPSHOT"]
                 [lein-marginalia "0.8.0-SNAPSHOT"]])
