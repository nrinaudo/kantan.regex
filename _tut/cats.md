---
layout: tutorial
title: "Scala cats module"
section: tutorial
sort_order: 11
---
Kantan.regex has a [cats](https://github.com/typelevel/cats) module that is, in its current incarnation, fairly bare
bones: it simply provides a few useful type class instances.

The `cats` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-cats" % "0.1.6"
```

You then need to import the corresponding package:

```scala
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

[`Functor`]:http://typelevel.org/cats/api/cats/Functor.html
[`BiFunctor`]:http://typelevel.org/cats/api/cats/functor/Bifunctor.html
[`Order`]:http://typelevel.org/cats/api/cats/kernel/Order.html
[`Show`]:http://typelevel.org/cats/api/cats/Show.html
[`Traverse`]:http://typelevel.org/cats/api/cats/Traverse.html
[`Monad`]:http://typelevel.org/cats/api/cats/Monad.html
[`GroupDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$GroupDecoder.html
[`MatchDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$MatchDecoder.html
[`RegexResult`]:{{ site.baseurl}}/api/kantan/regex/RegexResult$.html
[`DecodeResult`]:{{ site.baseurl }}/api/kantan/regex/package$$DecodeResult$.html
[`CompileResult`]:{{ site.baseurl }}/api/kantan/regex/CompileResult$.html
[`Monoid`]:http://typelevel.org/cats/api/cats/kernel/Monoid.html
