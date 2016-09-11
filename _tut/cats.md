---
layout: tutorial
title: "Scala cats module"
section: tutorial
sort_order: 10
---
Kantan.regex has a [cats](https://github.com/typelevel/cats) module that is, in its current incarnation, fairly bare
bones: it provides decoders for [`Xor`] as well as a few useful type class instances.

The `cats` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-cats" % "0.1.4"
```

You then need to import the corresponding package:

```scala
import kantan.regex.cats._
```


## `Xor` decoder

The `cats` module provides a [`GroupDecoder`] instance for [`Xor`]: for any type `A` and `B` that each have a
[`GroupDecoder`] instance, there exists a [`GroupDecoder`] instance for `A Xor B`.

First, a few imports:

```scala
import cats.data.Xor
import kantan.regex.implicits._
```

We can then simply write the following:

```scala
scala> "[123] [true]".evalRegex[Int Xor Boolean](rx"\[(\d+|true|false)\]", 1).foreach(println _)
Success(Left(123))
Success(Right(true))
```

This also applies to [`MatchDecoder`] instances:

```scala
scala> "(1, true) and then (2, foo)".evalRegex[(Int, Boolean) Xor (Int, String)](rx"\((\d+), ([a-z]+)\)").foreach(println _)
Success(Left((1,true)))
Success(Right((2,foo)))
```

## Cats instances

The following instance for cats type classes are provided:

* [`Functor`] for all decoders ([`GroupDecoder`] and [`MatchDecoder`]).
* [`Order`] for all result types ([`DecodeResult`], [`RegexResult`] and [`CompileResult`]).
* [`Show`] for all result types.
* [`Monoid`] for all result types.
* [`Traverse`] for all result types.
* [`Monad`] for all result types.
* [`BiFunctor`] for all result types.

[`Functor`]:http://typelevel.org/cats/api/#cats.Functor
[`BiFunctor`]:http://typelevel.org/cats/api/#cats.functor.Bifunctor
[`Order`]:http://typelevel.org/cats/api/index.html#cats.package@Order[A]=cats.kernel.Order[A]
[`Show`]:http://typelevel.org/cats/api/index.html#cats.Show
[`Traverse`]:http://typelevel.org/cats/api/index.html#cats.Traverse
[`Monad`]:http://typelevel.org/cats/api/index.html#cats.Monad
[`GroupDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`MatchDecoder`]:{{ site.baseurl}}/api/index.html#kantan.regex.package@MatchDecoder[A]=kantan.codecs.Decoder[kantan.regex.Match,A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`RegexResult`]:{{ site.baseurl}}/api/index.html#kantan.regex.package@RegexResult[A]=kantan.codecs.Result[kantan.regex.RegexError,A]
[`DecodeResult`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@DecodeResult[A]=kantan.codecs.Result[kantan.regex.DecodeError,A]
[`CompileResult`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@CompileResult[A]=kantan.codecs.Result[kantan.regex.CompileError,A]
[`Xor`]:http://typelevel.org/cats/api/#cats.data.Xor
[`Monoid`]:http://typelevel.org/cats/api/index.html#cats.package@Monoid[A]=cats.kernel.Monoid[A]
