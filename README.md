# boot-cljs-test/testem-runner

```clj
[boot-cljs-test/testem-runner "0.1.0"]
```

Boot task that automatically generates a ClojureScript test runner
to work in Testem.

> Testem is a test runner that makes Javascript unit testing fun.

[Testem](https://github.com/airportyh/testem) provides:

 - a great user interface to run tests in many browsers and see the
 result
 - convenient setup for continuous integration

## Requirement

You need Node version 0.10+ or iojs installed on your system.

Installing testem is straightforward:

```
npm install testem -g
```

## Usage

Add `boot-cljs-test/testem-runner` to your `build.boot` dependencies
and `require` the namespace:

```clj
(set-env! :dependencies '[[boot-cljs-test/testem-runner "X.Y.Z" :scope "test"]])
(require '[boot-cljs-test/testem-runner :refer [setup-testem]])
```

You can see the options available on the command line:

```bash
$ boot setup-testem -h
```

or in the REPL:

```bash
boot.user=> (doc setup-testem)
```

## Setup

### For test-driven development

in your `build.boot`:

```clj
(deftask build []
  (comp (setup-testem :namespaces '[foo.core-test bar.util-test]) ;; put it before `cljs` task
        (cljs :source-map true
              :optimizations :none)))
```

start Boot in a terminal:

```
boot watch speak build
```

in another terminal, start Testem runner:

```
cd target
testem
```

### For continuous integration


```bash
boot build # just compile, not watch
cd target
testem ci # start Testem in CI mode
```

Enjoy!

## Advanced Testem config

other config for Testem will go into `testem.json`:

```clj
(setup-testem :namespaces '[app.test]
              :testem-config {:launch_in_ci ["Firefox" "Chromium"]})
```

More about Testem config:

https://github.com/airportyh/testem/blob/master/docs/config_file.md

## License

Copyright © 2015 Hoang Minh Thang

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
