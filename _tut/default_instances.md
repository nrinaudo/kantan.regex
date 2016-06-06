---
layout: tutorial
title: "Default instances"
section: tutorial
sort_order: 7
---


## `GroupDecoder`

### Basic types

The following types have [`GroupDecoder`] instances available out of the box:

* [`BigDecimal`]
* [`BigInt`]
* [`Byte`]
* [`Char`]
* [`Boolean`]
* [`Double`]
* [`Float`]
* [`Int`]
* [`Long`]
* [`Short`]
* [`String`]
* [`UUID`]
* [`URI`]
* [`URL`]

### `java.util.Date`

There also is a default [`GroupDecoder`] instance available for [`Date`], but this one is slightly more complicated.
There are so many different ways of writing dates that there is no reasonable default behaviour - one might argue that
defaulting to ISO 8601 might make sense, but there doesn't appear to be a sane way of implementing that in Java’s crusty
date / time API.

Instead of providing a default implementation that is likely going to be incorrect for most people, kantan.regex expects
an implicit [`DateFormat`] instance in scope, and will decode using that format.

We could for example declare a formatter for something ISO 8601-like:

```scala
import kantan.regex.implicits._
import java.util.{Locale, Date}

implicit val formatter = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
```

And we're now capable of decoding matches as dates:

```scala
scala> "2000-01-00T00:00:00.000".evalRegex[Date](rx"\d\d\d\d-\d\d-\d\d").foreach(println _)
Success(Fri Dec 31 00:00:00 CET 1999)
```

Note that kantan.regex has a [joda-time](joda.html) module, a very well thought out alternative to [`Date`].

### `Either`

For any two types `A` and `B` that each have a [`GroupDecoder`], there exists a
[`GroupDecoder[Either[A, B]]`][`GroupDecoder`].


This is useful for dodgy string data where the type of a value is not well defined - it might sometimes be an int,
sometimes a boolean, for example:

```scala
scala> "[123] [true]".evalRegex[Either[Int, Boolean]](rx"\[(\d+|true|false)\]", 1).foreach(println _)
Success(Left(123))
Success(Right(true))
```

### `Option`

For any type `A` that has a [`GroupDecoder`], there exists a [`GroupDecoder[Option[A]]`][`GroupDecoder`].

This is particularly useful for optional groups. For example:

```scala
scala> "[123], []".evalRegex[Option[Int]](rx"\[(\d+)?\]", 1).foreach(println _)
Success(Some(123))
Success(None)
```


## `MatchDecoder`

### Basic types

Any type `A` such that `A` has a [`GroupDecoder`] also has a [`MatchDecoder`].

Note that this can sometimes be a bit tricky, as it involves selecting the group that's the most likely to be the
correct one. Kantan.regex will consider that, unless explicitly instructed otherwise, turning a [`GroupDecoder`] into
a [`MatchDecoder`] is done by looking at the *entire match* (that is, group 0).

That is in fact what was happening in most examples above: [`evalRegex`] expects a [`MatchDecoder`], not a
[`GroupDecoder`].


### Tuples

Tuples composed of types that each have a [`GroupDecoder`] automatically have a [`MatchDecoder`]. This is done by
assuming that the value of group 1 corresponds to the first field in the tuple, group 2 to the second, ...

```scala
scala> "[1, true] and then [3, false]".evalRegex[(Int, Boolean)](rx"\[(\d+), ([a-z]+)\]").foreach(println _)
Success((1,true))
Success((3,false))
```


### `Either`

For any two types `A` and `B` that each have a [`MatchDecoder`], there exists a
[`MatchDecoder[Either[A, B]]`][`MatchDecoder`].

This works essentially the same way as [`GroupDecoder`] for [`Either`]:

```scala
scala> "[123, true] [456, foo]".evalRegex[Either[(Int, Boolean), (Int, String)]](rx"\[(\d+), ([a-z]+)\]").foreach(println _)
Success(Left((123,true)))
Success(Right((456,foo)))
```

### `Option`

For any type `A` that has a [`MatchDecoder`], there exists a [`MatchDecoder[Option[A]]`][`MatchDecoder`].

Now, I know for a fact this works - I have tests for it. I just can't really think of an actual use case for it -
the notion of an optional match is... odd.


[`BigInt`]:http://www.scala-lang.org/api/current/index.html#scala.math.BigInt
[`BigDecimal`]:http://www.scala-lang.org/api/current/index.html#scala.math.BigDecimal
[`Byte`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Byte.html
[`Char`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Character.html
[`Boolean`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Boolean.html
[`Double`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html
[`Float`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Float.html
[`Int`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html
[`Long`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Long.html
[`Short`]:https://docs.oracle.com/javase/7/docs/api/java/lang/Short.html
[`String`]:https://docs.oracle.com/javase/7/docs/api/java/lang/String.html
[`UUID`]:https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html
[`URL`]:https://docs.oracle.com/javase/7/docs/api/java/net/URL.html
[`URI`]:https://docs.oracle.com/javase/7/docs/api/java/net/URI.html
[`Reader`]:https://docs.oracle.com/javase/7/docs/api/java/io/Reader.html
[`InputStream`]:https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html
[`File`]:https://docs.oracle.com/javase/7/docs/api/java/io/File.html
[`Path`]:https://docs.oracle.com/javase/7/docs/api/java/nio/file/Path.html
[`GroupDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`Date`]:https://docs.oracle.com/javase/7/docs/api/java/util/Date.html
[`DateFormat`]:https://docs.oracle.com/javase/7/docs/api/java/text/DateFormat.html
[`MatchDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@MatchDecoder[A]=kantan.codecs.Decoder[kantan.regex.Match,A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`Either`]:http://www.scala-lang.org/api/current/index.html#scala.util.Either
[`evalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops.StringOps@evalRegex[A](p:kantan.regex.Pattern)(implicitevidence$1:kantan.regex.MatchDecoder[A]):Iterator[kantan.regex.DecodeResult[A]]
