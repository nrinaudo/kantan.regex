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

```scala
val input = "[1, 2] and then [3, 4] followed by [5, 6]"
```

We'll be trying to extract the bits between brackets as `(Int, Int)` values. This is expressed by a pretty simple
regular expression, which we can compile with the [`asRegex`] method:

```scala
import kantan.regex.ops._

val regex = "\\[(\\d+), (\\d+)\\]".asRegex[(Int, Int)].get
```

Note that we had to call [`get`] on the result: [`asRegex`] returns a [`CompileResult`] instance to protect against
ill-formed regular expressions. We could also have used [`asUnsafeRegex`] for exactly the same results. Or, ideally,
have some proper error handling in place.

Now that we have this instance of [`Regex`], we can simply pass it as parameter to [`evalRegex`]:

```scala
scala> input.evalRegex(regex).foreach(println _)
Success((1,2))
Success((3,4))
Success((5,6))
```

## Compiling regexes expressed as other types

Most of the time, regular expressions are expressed as strings. It's however possible to have them as other types -
[`scala.util.matching.Regex`] and [`java.util.regex.Pattern`] come to mind.

For this reason, kantan.regex abstracts over the notion of what can be compiled as regular expression through the
[`Compiler`] type class: anything that has an implicit instance of [`Compiler`] in scope can be used in [`asRegex`].

For example:

```scala
// Note the .r - we're working with a Scala regular expression
"\\[(\\d+), (\\d+)\\]".r.asRegex[(Int, Int)]

// Java patterns can also be turned into instances of Regex.
Pattern.compile("\\[(\\d+), (\\d+)\\]".asRegex[(Int, Int)]
```


[`evalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$StringOps@evalRegex[A](expr:String,group:Int)(implicitevidence$2:kantan.regex.GroupDecoder[A]):Iterator[kantan.regex.RegexResult[A]]
[`asRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$CompilableOps@asRegex[A](group:Int)(implicitevidence$6:kantan.regex.GroupDecoder[A],implicitcs:kantan.regex.Compiler[S]):kantan.regex.CompileResult[kantan.regex.Regex[kantan.regex.DecodeResult[A]]]
[`asUnsafeRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$CompilableOps@asUnsafeRegex[A](implicitevidence$7:kantan.regex.MatchDecoder[A],implicitcs:kantan.regex.Compiler[S]):kantan.regex.Regex[kantan.regex.DecodeResult[A]]
[`Regex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.Regex
[`CompileResult`]:{{ site.baseUrl}}/api/index.html#kantan.regex.package@CompileResult[A]=kantan.codecs.Result[kantan.regex.CompileError,A]
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/index.html#kantan.codecs.Result@get:S
[`Compiler`]:{{ site.baseUrl }}/api/#kantan.regex.Compiler
[`scala.util.matching.Regex`]:http://www.scala-lang.org/api/current/index.html#scala.util.matching.Regex
[`java.util.regex.Pattern`]:https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
