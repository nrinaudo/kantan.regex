---
layout: tutorial
title: "Scalaz module"
section: tutorial
sort_order: 11
---
Kantan.regex has a [scalaz](https://github.com/scalaz/scalaz) module that is, in its current incarnation, fairly bare
bones: it provides decoders for [`Maybe`] and [`\/`] as well as a few useful type class instances.

The `scalaz` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-scalaz" % "0.1.5"
```

You then need to import the corresponding package:

```tut:silent
import kantan.regex.scalaz._
```

## `\/` decoder

The `scalaz` module provides a [`GroupDecoder`] instance for [`\/`]: for any type `A` and `B` that each have a
[`GroupDecoder`] instance, there exists a [`GroupDecoder`] instance for `A \/ B`.

First, a few imports:

```tut:silent
import scalaz._
import kantan.regex.implicits._
```

We can then simply write the following:

```tut
"[123] [true]".evalRegex[Int \/ Boolean](rx"\[(\d+|true|false)\]", 1).foreach(println _)
```

This also applies to [`MatchDecoder`] instances:

```tut
"(1, true) and then (2, foo)".evalRegex[(Int, Boolean) \/ (Int, String)](rx"\((\d+), ([a-z]+)\)").foreach(println _)
```

## `Maybe` decoder

The `scalaz` module provides a [`GroupDecoder`] instance for [`Maybe`]: for any type `A` that has a [`GroupDecoder`]
instance, there exists a [`GroupDecoder`] instance for `Maybe[A]`.

```tut
"[123], []".evalRegex[Maybe[Int]](rx"\[(\d+)?\]", 1).foreach(println _)
```

The same is true for [`MatchDecoder`], although I can't really think of an example for this odd concept.

## Scalaz instances

The following instance for cats type classes are provided:

* [`Functor`] for all decoders ([`GroupDecoder`] and [`MatchDecoder`]).
* [`Order`] for all result types ([`DecodeResult`], [`RegexResult`] and [`CompileResult`]).
* [`Monoid`] for all result types.
* [`Show`] for all result types.
* [`Traverse`] for all result types.
* [`Monad`] for all result types.
* [`BiFunctor`] for all result types.

[`Functor`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Functor
[`BiFunctor`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Bifunctor
[`Order`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Order
[`Show`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Show
[`Traverse`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Show
[`Monad`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Monad
[`Monoid`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Monoid
[`\/`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.$bslash$div
[`Maybe`]:https://oss.sonatype.org/service/local/repositories/releases/archive/org/scalaz/scalaz_2.11/7.2.3/scalaz_2.11-7.2.3-javadoc.jar/!/index.html#scalaz.Maybe
[`GroupDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`GroupDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`MatchDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package$$MatchDecoder
[`RegexResult`]:{{ site.baseurl}}/api/index.html#kantan.regex.package$$RegexResult
[`DecodeResult`]:{{ site.baseurl }}/api/index.html#kantan.regex.package$$DecodeResult
[`CompileResult`]:{{ site.baseurl }}/api/index.html#kantan.regex.package$$CompileResult
