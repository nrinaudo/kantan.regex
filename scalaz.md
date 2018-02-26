---
layout: tutorial
title: "Scalaz module"
section: tutorial
sort_order: 12
---
Kantan.regex has a [scalaz](https://github.com/scalaz/scalaz) module that is, in its current incarnation, fairly bare
bones: it provides decoders for [`Maybe`] and [`\/`] as well as a few useful type class instances.

The `scalaz` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-scalaz" % "0.4.0"
```

You then need to import the corresponding package:

```scala
import kantan.regex.scalaz._
```

## `\/` decoder

The `scalaz` module provides a [`GroupDecoder`] instance for [`\/`]: for any type `A` and `B` that each have a
[`GroupDecoder`] instance, there exists a [`GroupDecoder`] instance for `A \/ B`.

First, a few imports:

```scala
import scalaz._
import kantan.regex.implicits._
```

We can then simply write the following:

```scala
scala> "[123] [true]".evalRegex[Int \/ Boolean](rx"\[(\d+|true|false)\]", 1).foreach(println _)
Right(-\/(123))
Right(\/-(true))
```

This also applies to [`MatchDecoder`] instances:

```scala
scala> "(1, true) and then (2, foo)".evalRegex[(Int, Boolean) \/ (Int, String)](rx"\((\d+), ([a-z]+)\)").foreach(println _)
Right(-\/((1,true)))
Right(\/-((2,foo)))
```

## `Maybe` decoder

The `scalaz` module provides a [`GroupDecoder`] instance for [`Maybe`]: for any type `A` that has a [`GroupDecoder`]
instance, there exists a [`GroupDecoder`] instance for `Maybe[A]`.

```scala
scala> "[123], []".evalRegex[Maybe[Int]](rx"\[(\d+)?\]", 1).foreach(println _)
Right(Just(123))
Right(Empty())
```

The same is true for [`MatchDecoder`], although I can't really think of an example for this odd concept.

## Scalaz instances

The following instance for cats type classes are provided:

* [`MonadError`] and [`Plus`] for [`GroupDecoder`].
* [`Show`] and [`Equal`] for all error types ([`RegexError`] and all its descendants).
* [`Functor`] for [`Regex`].

[`MonadError`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/MonadError.html
[`Functor`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Functor.html
[`Plus`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Plus.html
[`Show`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Show.html
[`Equal`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Equal.html
[`\/`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/$bslash$div.html
[`Maybe`]:https://static.javadoc.io/org.scalaz/scalaz_2.12/7.2.18/scalaz/Maybe.html
[`GroupDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$GroupDecoder.html
[`MatchDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$MatchDecoder.html
[`RegexError`]:{{ site.baseurl}}/api/kantan/regex/RegexError.html
[`Regex`]:{{ site.baseurl}}/api/kantan/regex/Regex.html
