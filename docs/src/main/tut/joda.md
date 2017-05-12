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
libraryDependencies += "com.nrinaudo" %% "kantan.regex-joda-time" % "0.1.9"
```

You then need to import the corresponding package:

```tut:silent
import kantan.regex.joda.time._
```

kantan.regex has default, ISO 8601 compliant [`GroupDecoder`] instances for the following types:

* [`DateTime`]
* [`LocalDate`]
* [`LocalDateTime`]
* [`LocalTime`]

Let's imagine for example that we want to extract dates from the following string:

```tut:silent
import kantan.regex.implicits._

val input = "[1978-10-12] and [2015-09-01]"
```

This is directly supported:

```tut
input.evalRegex[org.joda.time.LocalDate](rx"\[(\d\d\d\d-\d\d-\d\d)\]", 1).foreach(println _)
```

It is, of course, possible to declare your own [`GroupDecoder`]. This is, for example, how you'd create a custom
[`GroupDecoder[LocalDate]`][`GroupDecoder`]:

```tut:silent
import kantan.regex._
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

val input = "[12-10-1978] and [01-09-2015]"

implicit val decoder: GroupDecoder[LocalDate] = localDateDecoder(DateTimeFormat.forPattern("dd-MM-yyyy"))
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
[`GroupDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$GroupDecoder.html
