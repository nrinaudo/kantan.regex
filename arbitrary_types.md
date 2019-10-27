---
layout: tutorial
title: "Extracting arbitrary types"
section: tutorial
sort_order: 4
---

Sometimes, you're trying to extract content from strings into something that is neither a
[primitive type](primitive_types.html), a [tuple](tuples.html) or a [case class](case_classes.html). Dealing with
arbitrary types is essentially the same thing as with case classes, you just need to write your own instantiation
function.

Let's imagine for example that we want to extract the bits between brackets as points with an optional z-coordinate:

```scala
val input = "(1, 2) and then (3, 4) followed by (5, 6, 7)"
```

This could be achieved with the following regular expression:

```scala
import kantan.regex.implicits._

val regex = rx"\((\d+), (\d+)(?:, (\d+))?\)"
```

Here's the class we want to extract matches into:

```scala
class Point(val x: Int, val y: Int, val z: Option[Int]) {
  override def toString = s"Point($x, $y, $z)"
}
```

Just like we did for [case classes](case_classes.html), we need to create a new [`MatchDecoder`] instance for `Point`.
While we could write one from scratch, it's usually easier (and easier to get right) to use one of [`MatchDecoder`]'s
helper functions - in our case, [`decoder`]:

```scala
import kantan.regex._

implicit val decoder: MatchDecoder[Point] = MatchDecoder.decoder(1, 2, 3) { (x: Int, y: Int, z: Option[Int]) =>
  new Point(x, y, z)
}
```

And that's it, we're done: we can now [`evalRegex`] as usual, with the right type parameter:

```scala
input.evalRegex[Point](regex).foreach(println _)
// Right(Point(1, 2, None))
// Right(Point(3, 4, None))
// Right(Point(5, 6, Some(7)))
```

Two things worth noting about how created that decoder instance:

* group indexes start at 1, not at 0 (which represents the entire match).
* since, in our case, group indexes are contiguous, we could have used [`ordered`] instead of [`decoder`].

[`evalRegex`]:{{ site.baseurl }}/api/kantan/regex/ops/StringOps.html#evalRegex[A](p:kantan.regex.Pattern)(implicitevidence$1:kantan.regex.MatchDecoder[A]):Iterator[kantan.regex.DecodeResult[A]]
[`MatchDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$MatchDecoder.html
[`decoder`]:{{ site.baseurl }}/api/kantan/regex/GeneratedMatchDecoders.html#decoder[A1,A2,O](i1:Int,i2:Int)(f:(A1,A2)=>O)(implicitevidence$3:kantan.regex.GroupDecoder[A1],implicitevidence$4:kantan.regex.GroupDecoder[A2]):kantan.regex.MatchDecoder[O]
[`ordered`]:{{ site.baseurl }}//api/kantan/regex/GeneratedMatchDecoders.html#ordered[A1,A2,O](f:(A1,A2)=>O)(implicitevidence$5:kantan.regex.GroupDecoder[A1],implicitevidence$6:kantan.regex.GroupDecoder[A2]):kantan.regex.MatchDecoder[O]
