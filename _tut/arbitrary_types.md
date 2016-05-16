---
layout: tutorial
title: "Extracting arbitrary types"
section: tutorial
sort: 4
---
Sometimes, you're trying to extract content from strings into something that is neither a
[primitive type](primitive_types.html), a [tuple](tuples.html) or a [case class](case_classes.html). Dealing with
arbitrary types is essentially the same thing as with case classes, you just need to write your own instantiation
function.

Let's imagine for example that we want to extract the bits between brackets as points with an optional z-coordinate:

```scala
val input = "[1, 2] and then [3, 4] followed by [5, 6, 7]"
```

This could be achieved with the following regular expression: 

```scala
scala> val regex = """\[(\d+), (\d+)(?:, (\d+))?\]"""
regex: String = \[(\d+), (\d+)(?:, (\d+))?\]
```

Here's the class we want to extract matches into:

```scala
import kantan.regex._
import kantan.regex.ops._

class Point(val x: Int, val y: Int, val z: Option[Int]) {
  override def toString = s"Point($x, $y, $z)"
}
```

Just like we did for [case classes](case_classes.html), we need to create a new [`MatchDecoder`] instance for `Point`.
While we could write one from scratch, it's usually easier (and easier to get right) to use one of [`MatchDecoder`]'s
helper functions - in our case, [`ordered`]:

```scala
implicit val decoder: MatchDecoder[Point] = MatchDecoder.ordered { (x: Int, y: Int, z: Option[Int]) ⇒
  new Point(x, y, z)
}
```

And that's it, we're done: we can now [`evalRegex`] as usual, with the right type parameter:

```scala
scala> input.evalRegex[Point](regex).foreach(println _)
Success(Point(1, 2, None))
Success(Point(3, 4, None))
Success(Point(5, 6, Some(7)))
```

[`evalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$StringOps@evalRegex[A](expr:String,group:Int)(implicitevidence$2:kantan.regex.GroupDecoder[A]):Iterator[kantan.regex.RegexResult[A]]
[`MatchDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@MatchDecoder[A]=kantan.codecs.Decoder[kantan.regex.Match,A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`ordered`]:{{ site.baseUrl }}/api/index.html#kantan.regex.MatchDecoder$@ordered[A1,A2,A3,O](f:(A1,A2,A3)=>O)(implicitda1:kantan.regex.GroupDecoder[A1],implicitda2:kantan.regex.GroupDecoder[A2],implicitda3:kantan.regex.GroupDecoder[A3]):kantan.regex.MatchDecoder[O]
