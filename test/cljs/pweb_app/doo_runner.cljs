(ns pweb-app.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [pweb-app.core-test]))

(doo-tests 'pweb-app.core-test)

