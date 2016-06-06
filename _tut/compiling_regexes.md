---
layout: tutorial
title: "Compiling regular expressions for reuse"
section: tutorial
sort_order: 6
---
In the examples we've seen so far, regular expressions were passed around as [`Pattern`]s. This can be inefficient, as
kantan.regex needs to bake in the decoding code in patterns each time they are evaluated against some input.

When working with regular expressions that are used over and over, it's more efficient to compile them as instances
of [`Regex`].

## Compiling `Pattern`s

If you already have an instance of [`Pattern`], turning it into an instance of [`Regex`] is trivial, you need only
call [`Regex.apply`]:

```scala
import kantan.regex._
import kantan.regex.implicits._

val regex = Regex[(Int, Int)](rx"\((\d+), (\d+)\)")
```

Note the type parameter, which works exactly as in [`evalRegex`].

You can now pass the compiled regex to [`evalRegex`] directly, as many time as you need:

```scala
scala> "(1, 2) and then (3, 4) followed by (5, 6)".evalRegex(regex).foreach(println _)
Success((1,2))
Success((3,4))
Success((5,6))
```

Note how you don't need to specify a type parameter to [`evalRegex`] anymore, as the target type has already been
compiled into your [`Regex`] instance.


## Compiling `String`s

Strings are a bit more complicated, in that there is no guarantee that any given string is a valid regular expression.
If what you're trying to compile is a string literal, you should probably consider turning it into a [`Pattern`], which
is validated at compile time.

If you really must work with a string - for a dynamically generated regular expression, say - you can compile it safely
through [`asRegex`]:

```scala
val regex = """\((\d+), (\d+)\)""".asRegex[(Int, Int)].get
```

Note that we had to call [`get`] on the result: [`asRegex`] returns a [`CompileResult`] instance to protect against
ill-formed regular expressions.

Now that we have this instance of [`Regex`], we can simply pass it as parameter to [`evalRegex`]:

```scala
scala> "(1, 2) and then (3, 4) followed by (5, 6)".evalRegex(regex).foreach(println _)
Success((1,2))
Success((3,4))
Success((5,6))
```

## Under the hood

Any type that has an implicit instance of [`Compiler`] in scope can be compiled the way strings are. There are also
instances of [`Compiler`] for [`Pattern`] and [`scala.util.matching.Regex`], although they're not often useful - if you
have a [`Pattern`], just pass that to [`Regex.apply`], and if you have a [`scala.util.matching.Regex`], you can just
call [`pattern`] to get the underlying [`Pattern`].




[`evalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops.StringOps@evalRegex[A](p:kantan.regex.Pattern)(implicitevidence$1:kantan.regex.MatchDecoder[A]):Iterator[kantan.regex.DecodeResult[A]]
[`asRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops.CompilerOps@asRegex[A](implicitevidence$1:kantan.regex.MatchDecoder[A],implicitcs:kantan.regex.Compiler[S]):kantan.regex.CompileResult[kantan.regex.Regex[kantan.regex.DecodeResult[A]]]
[`asUnsafeRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops.CompilerOps@asUnsafeRegex[A](implicitevidence$3:kantan.regex.MatchDecoder[A],implicitcs:kantan.regex.Compiler[S]):kantan.regex.Regex[kantan.regex.DecodeResult[A]]
[`Regex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.Regex
[`Regex.apply`]:{{ site.baseUrl }}/api/index.html#kantan.regex.Regex$@apply[A](pattern:kantan.regex.Pattern)(implicitda:kantan.regex.MatchDecoder[A]):kantan.regex.Regex[kantan.regex.DecodeResult[A]]
[`CompileResult`]:{{ site.baseUrl}}/api/index.html#kantan.regex.package@CompileResult[A]=kantan.codecs.Result[kantan.regex.CompileError,A]
[`get`]:https://nrinaudo.github.io/kantan.codecs/api/index.html#kantan.codecs.Result@get:S
[`Compiler`]:{{ site.baseUrl }}/api/#kantan.regex.Compiler
[`scala.util.matching.Regex`]:http://www.scala-lang.org/api/current/index.html#scala.util.matching.Regex
[`Pattern`]:https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
[`pattern`]:http://www.scala-lang.org/api/current/index.html#scala.util.matching.Regex@pattern:java.util.regex.Pattern
