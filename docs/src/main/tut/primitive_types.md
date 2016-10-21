---
layout: tutorial
title: "Extracting primitive types"
section: tutorial
sort_order: 1
---
When working with regular expressions, it's fairly common to want to extract matches and turn them into useful types -
a depressingly recurrent scenario being extracting simple integers from strings.

Let's imagine that we get the following string and are interested in the integers between brackets:

```tut:silent
val input = "lorem ipsum [123] dolor si amet [456] DO NOT MATCH THIS 789."
```

## First attempt

The first, naive approach would to simply match digits and turn matches into ints. This can be achieved with a trivial
regular expression:

```tut:silent
import kantan.regex.implicits._

val digits = rx"\d+"
```

Note the way the regular expression was declared: the `rx` bit lets the compiler know that the following string literal
is a regular expression and should be validated. Had our expression been invalid, it would have been detected at
compile time.

And we can now simply call the [`evalRegex`] method that enriches strings:

```tut
val results = input.evalRegex[Int](digits)

results.foreach(println _)
```

There are a few important things happening here. First, note the type parameter to [`evalRegex`]: this tells
kantan.regex how to interpret each match. In our case, we want ints.

Second, `results` is an [`Iterator`]: regular expressions are evaluated lazily.

Lastly, even though we requested results of type [`Int`], we got [`Success[Int]`][`DecodeResult`] values instead. This
makes regular expression evaluation safe: errors are wrapped in a failure value rather than turned into exceptions.
Should you not really care about safety, you can use [`unsafeEvalRegex`] and get raw ints.


## Improved solution using groups

Looking at the results however, we see that we didn't really achieve what we set out to do: `789` should not have been
matched, but was. In order to solve this, we need to change our regular expression to something more precise, such as:

```tut
val regex = rx"\[(\d+)\]"
```

The problem here is that matches of this expression are not valid ints - they are surrounded by brackets. This
is where groups come in handy: we've declared our regular expression in such a way that for each match, the first group
will be the matched digits without the brackets. There's a version of [`evalRegex`] that takes the index of the group
from which to extract data:

```tut
input.evalRegex[Int](regex, 1).foreach(println _)
```

## Adding support to new types

In order to add support for types that aren't supported by default, you need only put an implicit value of
[`GroupDecoder`] in scope. Let’s do so, for example, for Joda [`DateTime`]:

```tut:silent
import kantan.regex._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

implicit val jodaDateTime: GroupDecoder[DateTime] = {
  val format = ISODateTimeFormat.date()

  // Summon an existing GroupDecoder[String] instance and modifies its behaviour,
  // rather than build a new decoder from scratch.
  GroupDecoder[String].mapResult(s ⇒ DecodeResult(format.parseDateTime(s)))
}
```

Here's an example of a string with a valid ISO date:

```tut:silent
val input = "Nothing of note happened on 2009-01-06"
```

And we can now decode this easily:

```tut
input.evalRegex[DateTime](rx"\d\d\d\d-\d\d-\d\d").foreach(println _)
```


[`Regex`]:{{ site.baseurl }}/api/#kantan.regex.Regex
[`evalRegex`]:{{ site.baseurl }}/api/index.html#kantan.regex.ops.StringOps@evalRegex[A](p:kantan.regex.Pattern)(implicitevidence$1:kantan.regex.MatchDecoder[A]):Iterator[kantan.regex.DecodeResult[A]]
[`Iterator`]:http://www.scala-lang.org/api/current/index.html#scala.collection.Iterator
[`Int`]:http://www.scala-lang.org/api/current/index.html#scala.Int
[`DecodeResult`]:{{ site.baseurl }}/api/index.html#kantan.regex.package$$DecodeResult
[`MatchDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package$$MatchDecoder
[`DateTime`]:http://www.joda.org/joda-time/apidocs/org/joda/time/DateTime.html
[`GroupDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`unsafeEvalRegex`]:{{ site.baseurl }}/api/index.html#kantan.regex.ops.StringOps@unsafeEvalRegex[A](p:kantan.regex.Pattern)(implicitevidence$3:kantan.regex.MatchDecoder[A]):Iterator[A]
