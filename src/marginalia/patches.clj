;;Patches the existing functions in marginalia.core to use out
;;extended versions that understand the alternate layout.
(require '[marginalia.core])

(in-ns 'marginalia.core)
(require '[marginalia [extensions :as ext]])

;;re-route documentation funcs to use our extensions.
(defn multidoc!
  [output-dir files-to-analyze props]
  (let [parsed-files (map path-to-doc files-to-analyze)
        index (ext/index-html props parsed-files)
        pages (map #(filename-contents props output-dir parsed-files %) parsed-files)]
    (doseq [f (conj pages {:name (io/file output-dir "toc.html")
                           :contents index})]
           (spit (:name f) (:contents f)))))

(defn uberdoc!
  "Generates an uberdoc html file from 3 pieces of information:

   2. The path to spit the result (`output-file-name`)
   1. Results from processing source files (`path-to-doc`)
   3. Project metadata as a map, containing at a minimum the following:
     - :name
     - :version
  "
  [output-file-name files-to-analyze props]
  (let [source (ext/uberdoc-html
                props
                (map path-to-doc files-to-analyze))]
    (spit output-file-name source)))

;;Redefine run-marginalia to use our new layout option. Default is
;;vertical layout.  This almost a carbon copy of the original, with
;;the parsing of a layout arg, and the addition of the layout as a key
;;to the project metadata.
(defn run-marginalia
  "Default generation: given a collection of filepaths in a project, find the .clj
   files at these paths and, if Clojure source files are found:

   1. Print out a message to std out letting a user know which files are to be processed;
   1. Create the docs directory inside the project folder if it doesn't already exist;
   1. Call the uberdoc! function to generate the output file at its default location,
     using the found source files and a project file expected to be in its default location.

   If no source files are found, complain with a usage message."
  [args & [project]]
  (let [[{:keys [dir file name version desc deps css js multi layout]} files help]
        (cli args
             ["-d" "--dir" "Directory into which the documentation will be written" :default "./docs"]
             ["-f" "--file" "File into which the documentation will be written" :default "uberdoc.html"]
             ["-n" "--name" "Project name - if not given will be taken from project.clj"]
             ["-v" "--version" "Project version - if not given will be taken from project.clj"]
             ["-D" "--desc" "Project description - if not given will be taken from project.clj"]
             ["-a" "--deps" "Project dependencies in the form <group1>:<artifact1>:<version1>;<group2>...
                 If not given will be taken from project.clj"]
             ["-c" "--css" "Additional css resources <resource1>;<resource2>;...
                 If not given will be taken from project.clj."]
             ["-j" "--js" "Additional javascript resources <resource1>;<resource2>;...
                 If not given will be taken from project.clj"]
             ["-m" "--multi" "Generate each namespace documentation as a separate file" :flag true]
             ["-l"  "--layout" "Specify a layout for the marginalia.  vertical -default- or parallel" :default "vertical"]) 
        sources (distinct (format-sources (seq files)))]
    (if-not sources
      (do
        (println "Wrong number of arguments passed to Marginalia.")
        (println help))
      (binding [*docs* dir]
        (let [project-clj (or project
                              (when (.exists (io/file "project.clj"))
                                (parse-project-file)))
              choose #(or %1 %2)
              marg-opts (merge-with choose
                                    {:css (when css (.split css ";"))
                                     :javascript (when js (.split js ";"))}
                                    (:marginalia project-clj))
              opts (merge-with choose
                               {:name name
                                :version version
                                :description desc
                                :dependencies (split-deps deps)
                                :multi multi
                                :marginalia marg-opts
                                :layout (keyword layout)} ;added a layout arg                                        
                               project-clj)]
          (println "Generating Marginalia documentation for the following source files:")
          (doseq [s sources]
            (println "  " s))
          (println)
          (ensure-directory! *docs*)
          (if multi
            (multidoc! *docs* sources opts)
            (uberdoc!  (str *docs* "/" file) sources opts))
          (println "Done generating your documentation in" *docs*)
          (println ""))))))

