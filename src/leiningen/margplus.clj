(ns leiningen.margplus
  (:require [leiningen [marg :refer :all]]))

;;ripped since it was private.
(defn- add-marg-dep [project]
  ;; Leiningen 2 is a bit smarter about only conjing it in if it
  ;; doesn't already exist and warning the user.
  (if-let [conj-dependency (resolve 'leiningen.core.project/conj-dependency)]
    (conj-dependency project dep)
    (update-in project [:dependencies] conj dep)))


(defn margplus
  "Run an extended Marginalia against your project source files.
   Now produces a vertical layout, more like classic literate programming."
  [project & args]
  (eval-in-project (add-marg-dep project)
                   `(binding [marginalia.html/*resources* ""]
                      (marginalia.core/run-marginalia (list ~@args)))
                   '(do (require 'marginalia.core)
                        (require 'marginalia.patch))))
