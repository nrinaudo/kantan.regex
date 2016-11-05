---
layout: tutorial
title: "Scala cats module"
section: tutorial
sort_order: 10
---
Kantan.regex has a [cats](https://github.com/typelevel/cats) module that is, in its current incarnation, fairly bare
bones: it simply provides a few useful type class instances.

The `cats` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-cats" % "0.1.5"
```

You then need to import the corresponding package:

```tut:silent
import kantan.regex.cats._
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
[`MatchDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package$$MatchDecoder
[`RegexResult`]:{{ site.baseurl}}/api/index.html#kantan.regex.package$$RegexResult
[`DecodeResult`]:{{ site.baseurl }}/api/index.html#kantan.regex.package$$DecodeResult
[`CompileResult`]:{{ site.baseurl }}/api/index.html#kantan.regex.package$$CompileResult
[`Monoid`]:http://typelevel.org/cats/api/index.html#cats.package@Monoid[A]=cats.kernel.Monoid[A]
