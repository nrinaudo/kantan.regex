---
layout: tutorial
title: "Extracting primitive types"
section: tutorial
sort: 1
---
When working with regular expressions, it's fairly common to want to extract matches and turn them into useful types -
a depressingly recurrent scenario being to extract simple integers from strings.

Let's imagine that we get the following string and are interested in the integers between brackets: 

```scala
val input = "lorem ipsum [123] dolor si amet [456] DO NOT MATCH THIS 789."
```

## First attempt

The first, naive approach would to simply match digits and turn matches into ints. This can be achieved with a trivial
regular expression:

```scala
val digits = """\d+"""
```

In order to evaluate that, we'll first need to import the kantan.regex syntax:

```scala
import kantan.regex.ops._
```

And we can now simply call the [`evalRegex`] method that enriches strings:

```scala
scala> val results = input.evalRegex[Int](digits)
results: Iterator[kantan.regex.RegexResult[Int]] = non-empty iterator

scala> results.foreach(println _)
Success(123)
Success(456)
Success(789)
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

```scala
scala> val regex = """\[(\d+)\]"""
regex: String = \[(\d+)\]
```

The problem here is that matches of this expression are not valid ints - they are surrounded by brackets. This
is where groups come in handy: we've declared our regular expression in such a way that for each match, the first group
will be the matched digits without the brackets. There's a version of [`evalRegex`] that takes the index of the group
from which to extract data:

```scala
scala> input.evalRegex[Int](regex, 1).foreach(println _)
Success(123)
Success(456)
```

## Adding support to new types

In order to add support for types that aren't supported by default, you need only put an implicit value of
[`GroupDecoder`] in scope. Let’s do so, for example, for Joda [`DateTime`]:

```scala
import kantan.regex._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

implicit val jodaDateTime: GroupDecoder[DateTime] = {
  val format = ISODateTimeFormat.date()
  GroupDecoder[String].mapResult(s ⇒ DecodeResult(format.parseDateTime(s)))
}
```

Here's an example of a string with a valid ISO date:
 
```scala
val input = "Nothing of note happened on 2009-01-06"
```

And we can now decode this easily:

```scala
scala> input.evalRegex[DateTime]("""\d\d\d\d-\d\d-\d\d""").foreach(println _)
Success(2009-01-06T00:00:00.000+01:00)
```


[`Regex`]:{{ site.baseUrl }}/api/#kantan.regex.Regex
[`evalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$StringOps@evalRegex[A](expr:String,group:Int)(implicitevidence$2:kantan.regex.GroupDecoder[A]):Iterator[kantan.regex.RegexResult[A]]
[`Iterator`]:http://www.scala-lang.org/api/current/index.html#scala.collection.Iterator
[`Int`]:http://www.scala-lang.org/api/current/index.html#scala.Int
[`DecodeResult`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@DecodeResult[A]=kantan.codecs.Result[kantan.regex.DecodeError,A]
[`MatchDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@MatchDecoder[A]=kantan.codecs.Decoder[kantan.regex.Match,A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`DateTime`]:http://www.joda.org/joda-time/apidocs/org/joda/time/DateTime.html
[`GroupDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`unsafeEvalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$StringOps@unsafeEvalRegex[A](expr:String)(implicitevidence$3:kantan.regex.MatchDecoder[A]):Iterator[A]
