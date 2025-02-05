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
libraryDependencies += "com.nrinaudo" %% "kantan.regex-cats" % "0.6.0"
```

You then need to import the corresponding package:

```scala
import kantan.regex.cats._
```

## Cats instances

The following instance for cats type classes are provided:

* [`MonadError`] and [`SemigroupK`] for [`GroupDecoder`].
* [`Functor`] for [`Regex`].
* [`Show`] and [`Eq`] for all error types ([`RegexError`] and all its descendants).

[`MonadError`]:https://typelevel.org/cats/api/cats/MonadError.html
[`Functor`]:https://typelevel.org/cats/api/cats/Functor.html
[`SemigroupK`]:https://typelevel.org/cats/api/cats/SemigroupK.html
[`Show`]:https://typelevel.org/cats/api/cats/Show.html
[`Eq`]:https://typelevel.org/cats/api/cats/kernel/Eq.html
[`GroupDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$GroupDecoder.html
[`MatchDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$MatchDecoder.html
[`RegexError`]:{{ site.baseurl}}/api/kantan/regex/RegexError.html
[`Regex`]:{{ site.baseurl}}/api/kantan/regex/Regex.html
