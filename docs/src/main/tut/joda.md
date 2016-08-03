---
layout: tutorial
title: "Joda time module"
section: tutorial
sort_order: 9
---
[Joda-Time](http://www.joda.org/joda-time/) is a very well thought out date and time library for Java that happens to
be very popular in Scala - at the very least, it's quite a bit better than the stdlib [`Date`]. kantan.regex provides
support for it through a dedicated module.

The `joda-time` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-joda-time" % "0.1.3"
```

You then need to import the corresponding package:

```tut:silent
import kantan.regex.joda.time._
```

There are so many different ways of serialising dates that kantan.regex doesn't have a default implementation - whatever
the choice, it would end up more often wrong than right. What you can do, however, is declare an implicit
[`DateTimeFormat`]. This will get you a [`GroupDecoder`] instance for the following types:

* [`DateTime`]
* [`LocalDate`]
* [`LocalDateTime`]
* [`LocalTime`]

Let's imagine for example that we want to extract dates from the following string:

```tut:silent
import kantan.regex.implicits._

val input = "[12-10-1978] and [09-01-2015]"
```

We'd first need to declare the appropriate [`DateTimeFormat`]:

```tut:silent
import org.joda.time.format._

implicit val format = DateTimeFormat.forPattern("DD-MM-yyyy")
```

And we're done, as far as decoding is concerned. We only need to get a regular expression together and evaluate it:

```tut
input.evalRegex[org.joda.time.LocalDate](rx"\[(\d\d-\d\d-\d\d\d\d)\]", 1).foreach(println _)
```



[`Date`]:https://docs.oracle.com/javase/7/docs/api/java/util/Date.html
[`DateTime`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTime.html
[`LocalDate`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/LocalDate.html
[`LocalDateTime`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/LocalDateTime.html
[`LocalTime`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/LocalTime.html
[`DateTimeFormat`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html
[`GroupDecoder`]:{{ site.baseurl }}/api/index.html#kantan.regex.package@GroupDecoder[A]=kantan.codecs.Decoder[Option[String],A,kantan.regex.DecodeError,kantan.regex.codecs.type]
