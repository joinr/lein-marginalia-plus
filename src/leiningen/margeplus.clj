(ns leiningen.margeplus
  (:require [leiningen.marge [:refer :all]]))

(defn marge-plus
  "Run an extended Marginalia against your project source files.
   Now produces a vertical layout, more like classic literate programming."
  [project & args]
  (eval-in-project (add-marg-dep project)
                   `(binding [marginalia.html/*resources* ""]
                      (marginalia.core/run-marginalia (list ~@args)))
                   '(do (require 'marginalia.core)
                        (require 'marginalia.patch))))
