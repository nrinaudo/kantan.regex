---
layout: tutorial
title: "Compiling regular expressions for reuse"
section: tutorial
sort: 6
---
In the examples we've seen so far, regular expressions were passed around as strings. This can be inefficient, as they
need to be compiled each time they're evaluated against some input.

When working with regular expressions that are used over and over, it's more efficient to compile them as instances
of [`Regex`].

## Compiling a string

First, we'll need some input to evaluate our regular expression against:

```tut:silent
val input = "[1, 2] and then [3, 4] followed by [5, 6]"
```

We'll be trying to extract the bits between brackets as `(Int, Int)` values. This is expressed by a pretty simple
regular expression, which we can compile with the [`asRegex`] method:

```tut:silent
import kantan.regex._
import kantan.regex.all._

val regex = rx"\[(\d+), (\d+)\]".asRegex[(Int, Int)].get
```

Note that we had to call [`get`] on the result: [`asRegex`] returns a [`CompileResult`] instance to protect against
ill-formed regular expressions. We could also have used [`asUnsafeRegex`] for exactly the same results. Or, ideally,
have some proper error handling in place.

Now that we have this instance of [`Regex`], we can simply pass it as parameter to [`evalRegex`]:

```tut
input.evalRegex(regex).foreach(println _)
```

## Compiling regexes expressed as other types

Most of the time, regular expressions are expressed as strings. It's however possible to have them as other types -
[`scala.util.matching.Regex`] and [`Pattern`] come to mind.

For this reason, kantan.regex abstracts over the notion of what can be compiled as regular expression through the
[`Compiler`] type class: anything that has an implicit instance of [`Compiler`] in scope can be used in [`asRegex`].

For example:

```tut:silent
// Raw string.
"""\[(\d+), (\d+)\]""".r.asRegex[(Int, Int)]

// Note the .r - we're working with a Scala regular expression
"""\[(\d+), (\d+)\]""".r.asRegex[(Int, Int)]

// Java patterns can also be turned into instances of Regex.
java.util.regex.Pattern.compile("""\[(\d+), (\d+)\]""").asRegex[(Int, Int)]

// Regular expression literal, the safest alternative since syntax errors are detected at compile time. 
rx"\[(\d+), (\d+)\]".asRegex[(Int, Int)]
```

Note that the last two examples can be improved on: the [`Regex.apply`] construction method takes an existing
[`Pattern`] (or regular expression literal, which amounts to the same thing) and returns a [`Regex`] value without
wrapping it into a [`CompileResult`], since there is no possibility for failure:

```tut:silent
Regex[(Int, Int)](rx"\[(\d+), (\d+)\]")
```


[`evalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops.StringOps@evalRegex[A](p:kantan.regex.Pattern)(implicitevidence$1:kantan.regex.MatchDecoder[A]):Iterator[kantan.regex.DecodeResult[A]]
[`asRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops.CompilerOps@asRegex[A](implicitevidence$1:kantan.regex.MatchDecoder[A],implicitcs:kantan.regex.Compiler[S]):kantan.regex.CompileResult[kantan.regex.Regex[kantan.regex.DecodeResult[A]]]
[`asUnsafeRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops.CompilerOps@asUnsafeRegex[A](implicitevidence$3:kantan.regex.MatchDecoder[A],implicitcs:kantan.regex.Compiler[S]):kantan.regex.Regex[kantan.regex.DecodeResult[A]]
[`Regex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.Regex
[`Regex.apply`]::{{ site.baseUrl }}/api/index.html#kantan.regex.Regex$@apply[A](pattern:kantan.regex.Pattern)(implicitda:kantan.regex.MatchDecoder[A]):kantan.regex.Regex[kantan.regex.DecodeResult[A]]
[`CompileResult`]:{{ site.baseUrl}}/api/index.html#kantan.regex.package@CompileResult[A]=kantan.codecs.Result[kantan.regex.CompileError,A]
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/index.html#kantan.codecs.Result@get:S
[`Compiler`]:{{ site.baseUrl }}/api/#kantan.regex.Compiler
[`scala.util.matching.Regex`]:http://www.scala-lang.org/api/current/index.html#scala.util.matching.Regex
[`Pattern`]:https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html