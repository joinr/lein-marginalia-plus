lein-marginalia-plus
===================
A little fork of marginalia, that adds support for vertical layouts, along with some extra goodies.

Usage
=====
I have no clojars account, for now you need to clone and install locally.

Clone this repository using git clone. 

Assuming you have leiningen installed, from the cloned directory evaluate "lein install"  

Add a dependency to your profile.clj for   
[lein-marginalia-plus "0.8.0-SNAPSHOT"] 

From the command line, a new lein command appears: margeplus 

lein margplus  

lein margplus /path/to/source.clj  

The old lein marg command is still there for the parallel layout. 

Since this just extends the existing lein-marginalia, all of the pre-existing 
functionality is there, so everything should be backwards compatible.

check out docs/uberdoc.html for an example of the reformatted output.

License
=======

Copyright © 2013 Joinr

Distributed under the Eclipse Public License, the same as Clojure.
