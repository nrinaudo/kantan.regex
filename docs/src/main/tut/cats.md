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
libraryDependencies += "com.nrinaudo" %% "kantan.regex-cats" % "@VERSION@"
```

You then need to import the corresponding package:

```tut:silent
import kantan.regex.cats._
```

## Cats instances

The following instance for cats type classes are provided:

* [`Functor`] for all decoders ([`GroupDecoder`] and [`MatchDecoder`]).

[`Functor`]:http://typelevel.org/cats/api/cats/Functor.html
[`BiFunctor`]:http://typelevel.org/cats/api/cats/functor/Bifunctor.html
[`GroupDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$GroupDecoder.html
[`MatchDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$MatchDecoder.html
[`RegexResult`]:{{ site.baseurl}}/api/kantan/regex/RegexResult$.html
[`DecodeResult`]:{{ site.baseurl }}/api/kantan/regex/package$$DecodeResult$.html
[`CompileResult`]:{{ site.baseurl }}/api/kantan/regex/CompileResult$.html
