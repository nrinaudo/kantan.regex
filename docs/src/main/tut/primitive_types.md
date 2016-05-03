---
layout: tutorial
title: "Extracting primitive types"
section: tutorial
sort: 1
---
When working with regular expressions, it's fairly common to want to extract matches and turn them into useful types -
a depressingly recurrent scenario being to extract simple integers from strings.

Let's imagine that we get the following string and are interested in the integers between brackets: 

```tut
val input = "lorem ipsum [123] dolor si amet [456] DO NOT MATCH THIS 789."
```

## First attempt

The first, naive approach would to simply match digits and turn matches into ints. This can be achieved with a trivial
regular expression:

```tut:silent
val digits = "\\d+"
```

We'll need to turn this into a [`Regex`] value, which is done through the [`asUnsafeRegex`] method that enriches strings:

```tut:silent
import kantan.regex._
import kantan.regex.ops._

val regex = digits.asUnsafeRegex[Int]
```

Note the type parameter to [`asUnsafeRegex`]: this tells kantan.regex how to interpret each match.

Another point worth mentioning is that [`asUnsafeRegex`] is unsafe: if your regular expression is ill-formed, an exception will
be thrown. Should you need a safe version, use [`asRegex`].
 
We can then simply evaluate that expression on our input string:
 
```tut
val results = regex.eval(input)

results.foreach(println _)
```

Two things are important to note here. The first one is that `results` is an [`Iterator`]: regular expressions are
evaluated lazily. The other interesting bit is that, even though we requested results of type [`Int`], we got
[`Success[Int]`][`DecodeResult`] values instead. This makes regular expression evaluation safe: errors are wrapped in
a failure value rather than turned into exceptions.

## Improved solution using groups

Looking at the results however, we see that we didn't really achieve what we set out to do: `789` should not have been
matched, but was. In order to solve this, we need to change our regular expression:

```tut:silent
// Only look for digits between brackets.
val bracketedDigits = "\\[(\\d+)\\]"
```

The problem here is that when this matches of this expression are not valid ints - they are surrounded by brackets. This
is where groups come in handy: we've declared our regular expression in such a way that for each match, the first group
will be the matched digits without the brackets. All that's left to do is to compile the regular expression in such
a way that it attempts to interpret the first group rather than the entire match:

```tut
bracketedDigits.asUnsafeRegex[Int](1).eval(input).foreach(println _)
```

## Adding support to new types

In order to add support for types that aren't supported by default, you need only put an implicit value of
[`GroupDecoder`] in scope. Let’s do so, for example, for Joda [`DateTime`]:

```tut:silent
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

implicit val jodaDateTime: GroupDecoder[DateTime] = {
  val format = ISODateTimeFormat.date()
  GroupDecoder[String].mapResult(s ⇒ DecodeResult(format.parseDateTime(s)))
}
```

Here's an example of a string with a valid ISO date:
 
```tut:silent
val input = "Nothing of note happened on 2009-01-06"
```

And we can now decode this easily:

```tut
"\\d\\d\\d\\d-\\d\\d-\\d\\d".asUnsafeRegex[DateTime].eval(input).foreach(println _)
```


[`Regex`]:{{ site.baseUrl }}/api/#kantan.regex.Regex
[`asUnsafeRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$CompilableOps@asUnsafeRegex[A](implicitevidence$4:kantan.regex.MatchDecoder[A],implicitcs:kantan.regex.Compiler[S]):kantan.regex.Regex[kantan.regex.DecodeResult[A]]
[`asRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$CompilableOps@asRegex[A](implicitevidence$2:kantan.regex.MatchDecoder[A],implicitcs:kantan.regex.Compiler[S]):kantan.regex.CompileResult[kantan.regex.Regex[kantan.regex.DecodeResult[A]]]
[`Iterator`]:http://www.scala-lang.org/api/current/index.html#scala.collection.Iterator
[`Int`]:http://www.scala-lang.org/api/current/index.html#scala.Int
[`DecodeResult`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@DecodeResult[A]=kantan.codecs.Result[kantan.regex.DecodeError,A]
[`MatchDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@MatchDecoder[A]=kantan.codecs.Decoder[kantan.regex.Match,A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`DateTime`]:http://www.joda.org/joda-time/apidocs/org/joda/time/DateTime.html
[`GroupDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]