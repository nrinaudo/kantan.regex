---
layout: tutorial
title: "Extracting tuples"
section: tutorial
sort_order: 2
---
We've seen in a [previous tutorial](primitive_types.html) how to extract simple types from matches in a regular
expression. Sometimes, however, matches contain more than one interesting value, each in a separate group.

For example, consider the following string:

```scala
val input = "(1, 2) and then (3, 4) followed by (5, 6, 7)"
```

We might want to extract all the parts that look like a point from it - this could be achieved with a simple regular
expression, something like:

```scala
import kantan.regex.implicits._

val tuple2Regex = rx"\((\d+), (\d+)\)"
```

Note how the "interesting" parts are each in their own group, this is critical to kantan.regex behaving properly.


We can then proceed to extract our points as `(Int, Int)` exactly like we did before for simple types, through
[`evalRegex`]:

```scala
input.evalRegex[(Int, Int)](tuple2Regex).foreach(println _)
// Right((1,2))
// Right((3,4))
```

Note that this will map each group in a match to the corresponding field in a tuple. If your groups and tuple
values are not in the same order, you need a bit [more legwork](case_classes.html).

This is not entirely satisfactory, though: if you take another look at our `input` string, you'll see that we have a
third point, this one with a `z` coordinate.

The following regex can be used to match the first two coordinates with an optional third:

```scala
val tuple3Regex = rx"\((\d+), (\d+)(?:, (\d+))?\)"
```

One way of interpreting matches from this regex would be as `(Int, Int, Option[Int])`: triples with two ints and an
optional third. This is achieved exactly as you'd expect:

```scala
input.evalRegex[(Int, Int, Option[Int])](tuple3Regex).foreach(println _)
// Right((1,2,None))
// Right((3,4,None))
// Right((5,6,Some(7)))
```

Another way would be as either an `(Int, Int, Int)` or an `(Int, Int)`, which is also as simple as specifying the
right type parameter to [`evalRegex`]:

```scala
input.evalRegex[Either[(Int, Int, Int), (Int, Int)]](tuple3Regex).foreach(println _)
// Right(Right((1,2)))
// Right(Right((3,4)))
// Right(Left((5,6,7)))
```

Note, however, that there's a small catch when decoding to [`Either`]: the most discriminatory type should always go
on the left. The reason for this is that [`Either`] will first attempt to decode as the left type, and stop there if
successful.  If we'd swapped the type parameter in our previous example, we'd not have gotten quite what we wanted:

```scala
input.evalRegex[Either[(Int, Int), (Int, Int, Int)]](tuple3Regex).foreach(println _)
// Right(Left((1,2)))
// Right(Left((3,4)))
// Right(Left((5,6)))
```

[`evalRegex`]:{{ site.baseurl }}/api/kantan/regex/ops/StringOps.html#evalRegex[A](p:kantan.regex.Pattern)(implicitevidence$1:kantan.regex.MatchDecoder[A]):Iterator[kantan.regex.DecodeResult[A]]
[`Either`]:http://www.scala-lang.org/api/current/scala/util/Either.html
