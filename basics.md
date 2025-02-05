---
layout: tutorial
title: "Basics"
section: tutorial
sort_order: 0
---
There are a few concepts to get familiar with before getting to grips with kantan.regex proper.

## Regular expression literals

First, regular expression literals: regular expressions that are validated at compile time. This is the preferred way
of creating regular expressions and will be used throughout the documentation, so it's best to get that out of the way
as soon as possible.

There are various ways of enabling the feature, the simplest and most common one being to import
[`kantan.regex.implicits._`]:

```scala
import kantan.regex.implicits._
```

This will also bring kantan.regex syntax in scope though, so if you only want the literals, you can simply import
[`kantan.regex.literals._`].

This lets you create new regular expression by prefixing string literals with [`rx`]:

```scala
rx"\d+"
// res0: java.util.regex.Pattern = \d+

rx"[ -~]"
// res1: java.util.regex.Pattern = [ -~]
```

And, as promised, this fails *at compile time* if the regular expression is not valid:

```scala
rx"[abc"
// error: Illegal regex: '[abc'
```

## Simple evaluation

Subsequent pages will get into more details, but the simplest, most idiomatic way of extracting well typed data from
strings using kantan.regex is through the [`evalRegex`] method that enriches strings (since we've imported
[`kantan.regex.implicits._`]). For example:

```scala
"123 and some text followed by 456 and then 789".evalRegex[Int](rx"\d+").foreach(println _)
// Right(123)
// Right(456)
// Right(789)
```

[`kantan.regex.implicits._`]:{{ site.baseurl }}/api/kantan/regex/implicits$.html
[`kantan.regex.literals._`]:{{ site.baseurl }}/api/kantan/regex/literals/index.html
[`rx`]:{{ site.baseurl }}/api/kantan/regex/literals/RegexLiteral.html#rx(args:Any*):kantan.regex.Pattern
[`evalRegex`]:{{ site.baseurl }}/api/kantan/regex/ops/StringOps.html#evalRegex[A](p:kantan.regex.Pattern)(implicitevidence$1:kantan.regex.MatchDecoder[A]):Iterator[kantan.regex.DecodeResult[A]]
