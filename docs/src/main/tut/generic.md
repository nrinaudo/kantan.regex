---
layout: tutorial
title: "Generic Module"
section: tutorial
sort: 8
---
While kantan.regex goes out of its way to provide [default instance](default_instances.html) for as many types as it can,
some are made problematic by my desire to avoid runtime reflection. Fortunately, [shapeless](http://shapeless.io)
provides _compile time_ reflection, which makes it possible for the `generic` module to automatically derive instances
for more common types and patterns.

The `generic` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-generic" % "0.1.2"
```

If you're using Scala 2.10.x, you should also add the macro paradise plugin to your build:

```scala
libraryDependencies += compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
```

Let's first declare the imports we'll need in the rest of this tutorial:

```tut:silent
import kantan.regex.implicits._ // Provides syntax and literal values.
import kantan.regex.generic._   // Provides automatic instance derivation.
```

The rest of this post will be a simple list of supported types.

## `GroupDecoder`

### Case class of arity 1

```tut:silent
case class Wrapper[A](a: A)
```

```tut
"123 and then 456".evalRegex[Wrapper[Int]](rx"\d+").foreach(println _)
```

### Sum types

```tut:silent
sealed abstract class Or[+A, +B]
case class Left[A](value: A) extends Or[A, Nothing]
case class Right[B](value: B) extends Or[Nothing, B]
```

```tut
"(123) and then (true)".evalRegex[Int Or Boolean](rx"\((\d+|true|false)\)", 1).foreach(println _)
```


## `MatchDecoder`

### Case classes

```tut:silent
case class CustomTuple2[A, B](a: A, b: B)
```

```tut
"(1, false) and then (3, true)".evalRegex[CustomTuple2[Int, Boolean]](rx"\((\d+), (true|false)\)").foreach(println _)
```

### Sum types

```tut
"(1, false) and then (3, foobar)".evalRegex[CustomTuple2[Int, Boolean] Or CustomTuple2[Int, String]](rx"\((\d+), ([a-z]+)\)").foreach(println _)
```