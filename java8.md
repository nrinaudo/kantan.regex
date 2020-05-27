---
layout: tutorial
title: "Java 8 dates and times"
section: tutorial
sort_order: 10
---
Java 8 comes with a better thought out dates and times API. Unfortunately, it cannot be supported as part of the core
kantan.regex API - we still support Java 7. There is, however, a dedicated optional module that you can include by
adding the following line to your `build.sbt` file:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-java8" % "0.5.2"
```

You then need to import the corresponding package:

```scala
import kantan.regex.java8._
```

And this will bring [`GroupDecoder`] instances in scope for the following types:

* [`Instant`]
* [`LocalDateTime`]
* [`ZonedDateTime`]
* [`OffsetDateTime`]
* [`LocalDate`]
* [`LocalTime`]

These will use the default Java 8 formats. For example:

```scala
import kantan.regex.implicits._
import java.time._

val input = "[1978-10-12] and [2015-01-09]"
```

We can decode the bracketed dates without providing an explicit decoder:

```scala
input.evalRegex[LocalDate](rx"\[(\d\d\d\d-\d\d-\d\d)\]", 1).foreach(println _)
// Right(1978-10-12)
// Right(2015-01-09)
```

It's also possible to provide your own format. For example, for [`LocalDateTime`]:

```scala
import java.time._
import java.time.format.DateTimeFormatter
import kantan.regex._
import kantan.regex.implicits._
import kantan.regex.java8._

implicit val decoder: GroupDecoder[LocalDate] = localDateDecoder(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

val input = "[10/12/1978] and [09/01/2015]"
```

And we can now simply write:

```scala
input.evalRegex[LocalDate](rx"\[(\d\d/\d\d/\d\d\d\d)\]", 1).foreach(println _)
// Right(1978-12-10)
// Right(2015-01-09)
```

Note that while you can pass a [`DateTimeFormatter`] directly, the preferred way of dealing with pattern strings is to
use the literal syntax provided by kantan.regex:

```scala
localDateDecoder(fmt"dd-MM-yyyy")
```

The advantage is that this is checked at compile time - invalid pattern strings will cause a compilation error:

```scala
localDateDecoder(fmt"FOOBAR")
// error: Illegal format: 'FOOBAR'
// localDateDecoder(fmt"FOOBAR")
//                  ^^^^^^^^^^^
```

[`GroupDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$GroupDecoder.html
[`Instant`]:https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html
[`LocalDateTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
[`OffsetDateTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/OffsetDateTime.html
[`ZonedDateTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html
[`LocalDate`]:https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
[`LocalTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html
[`DateTimeFormatter`]:https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
