---
layout: tutorial
title: "Basics"
section: tutorial
sort: 0
---
There are a few concepts to get familiar with before getting to grips with kantan.regex proper.

## Regular expression literals

First, regular expression literals: regular expressions that are validated at compile time. This is the preferred way
of creating regular expressions and will be used throughout the documentation, so it's best to get that out of the way
as soon as possible.

There are various ways of enabling the feature, the simplest and most common one being to import [`kantan.regex.all._`]:

```tut:silent
import kantan.regex.implicits._
```

This will also bring kantan.regex syntax in scope though, so if you only want the literals, you can simply import
[`kantan.regex.literals._`].

This lets you create new regular expression by prefixing string literals with [`rx`]:
 
```tut
rx"\d+"

rx"[ -~]"
```

And, as promised, this fails *at compile time* if the regular expression is not valid:
 
```tut:fail
rx"[abc"
```

## Simple evaluation

Subsequent pages will get into more details, but the simplest, most idiomatic way of extracting well typed data from
strings using kantan.regex is through the [`evalRegex`] method that enriches strings (since we've imported
[`kantan.regex.all._`]). For example:

```tut
"123 and some text followed by 456 and then 789".evalRegex[Int](rx"\d+").foreach(println _)
```

[`kantan.regex.all._`]:{{ site.baseUrl }}/api/#kantan.regex.all$
[`kantan.regex.literals._`]:{{ site.baseUrl }}/api/#kantan.regex.literals.package
[`rx`]:{{ site.baseUrl }}/api/index.html#kantan.regex.literals.RegexLiteral@rx(args:Any*):kantan.regex.Pattern
[`evalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops.StringOps@evalRegex[A](p:kantan.regex.Pattern)(implicitevidence$1:kantan.regex.MatchDecoder[A]):Iterator[kantan.regex.DecodeResult[A]]