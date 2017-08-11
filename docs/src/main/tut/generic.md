---
layout: tutorial
title: "Generic Module"
section: tutorial
sort_order: 8
---
While kantan.regex goes out of its way to provide [default instance](default_instances.html) for as many types as it can,
some are made problematic by my desire to avoid runtime reflection. Fortunately, [shapeless](http://shapeless.io)
provides _compile time_ reflection, which makes it possible for the `generic` module to automatically derive instances
for more common types and patterns.

The `generic` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-generic" % "0.1.6"
```

Let's first declare the imports we'll need in the rest of this tutorial:

```tut:silent
import kantan.regex.implicits._ // Provides syntax and literal values.
import kantan.regex.generic._   // Provides automatic instance derivation.
```

The rest of this post will be a simple list of supported types.

## `GroupDecoder`

### Case class of arity 1

Any class of arity 1 such that the type of its sole field has a [`GroupDecoder`] has a [`GroupDecoder`] itself.

Let's take a simple `Wrapper` class as an example:

```tut:silent
final case class Wrapper[A](a: A)
```

Without any further work, we can decode instances of `Wrapper`:

```tut
"123 and then 456".evalRegex[Wrapper[Int]](rx"\d+").foreach(println _)
```


### Sum types

Any sum type such that each of its alternatives has a [`GroupDecoder`] get a [`GroupDecoder`] for free.

For example, the following `Or` type:

```tut:silent
sealed abstract class Or[+A, +B]
final case class Left[A](value: A) extends Or[A, Nothing]
final case class Right[B](value: B) extends Or[Nothing, B]
```

If both `A` and `B` have a [`GroupDecoder`], then both `Left` and `Right`, being unary case classes, also do. If both
`Left` and `Right` have a [`GroupDecoder`], all of `Or[A, B]`'s alternatives do and `Or[A, B]` also does:

```tut
"(123) and then (true)".evalRegex[Int Or Boolean](rx"\((\d+|true|false)\)", 1).foreach(println _)
```


## `MatchDecoder`

### Case classes

Any case class of arity one or more such that all of its fields have a [`MatchDecoder`] get a [`MatchDecoder`] for free.

For example, a silly [`Tuple2`] implementation:

```tut:silent
final case class CustomTuple2[A, B](a: A, b: B)
```

If both `A` and `B` have [`MatchDecoder`] instances, so does `CustomTuple2[A, B]`:

```tut
"(1, false) and then (3, true)".evalRegex[CustomTuple2[Int, Boolean]](rx"\((\d+), (true|false)\)").foreach(println _)
```

### Sum types

Finally, any sum type such that all its alternatives have a [`MatchDecoder`] gets a [`MatchDecoder`] instance for free:

```tut
"(1, false) and then (3, foobar)".evalRegex[CustomTuple2[Int, Boolean] Or CustomTuple2[Int, String]](rx"\((\d+), ([a-z]+)\)").foreach(println _)
```

[`GroupDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$GroupDecoder.html
[`MatchDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$MatchDecoder.html
[`Tuple2`]:http://www.scala-lang.org/api/current/index.html?search=Tuple2
