---
layout: tutorial
title: "Extracting case classes"
section: tutorial
sort_order: 3
---
We've already seen how to extract [primitive types](primitive_types.html) and [tuples](tuples.html) from regular
expression matches, but a more common requirement is to extract case classes - often preferable to tuples, since they
provide more meaningful types.

Let's take the following input and attempt to extract the bits between brackets:

```scala mdoc:silent
val input = "(1, 2) and (3, false)"
```

This could be achieved with the following regular expression:

```scala mdoc:silent
import kantan.regex.implicits._

val regex = rx"\((\d+), (\d+|true|false)\)"
```

And since we're trying to demonstrate case class extraction, let's extract data to the following case class:

```scala mdoc:silent
case class WeirdPoint(x: Int, y: Either[Int, Boolean])
```

In order to extract complex (as in, composed of more than one value) types, we need to provide instances of
[`MatchDecoder`] for them. We can create a new one with one of [`MatchDecoder`]'s many helper methods - [`ordered`],
in our case, since match groups and case class fields are in the same order:

```scala mdoc:silent
import kantan.regex._

implicit val decoder: MatchDecoder[WeirdPoint] = MatchDecoder.ordered(WeirdPoint.apply _)
```

And that's all there is to it. Now that we have this decoder in place, we can just call [`evalRegex`] as usual:

```scala mdoc
input.evalRegex[WeirdPoint](regex).foreach(println _)
```

It's possible to automate this process through the [shapeless](http://shapeless.io)-backed [generic](generic.html)
module. Let's first import the module and declare a new case class:

```scala mdoc:silent
import kantan.regex.generic._

case class Foo(x: Int, y: Either[Int, Boolean])
```

And without any further work, we can decode instances of `Foo`:

```scala mdoc
input.evalRegex[Foo](regex).foreach(println _)
```

[`evalRegex`]:{{ site.baseurl }}/api/kantan/regex/ops/StringOps.html#evalRegex[A](p:kantan.regex.Pattern)(implicitevidence$1:kantan.regex.MatchDecoder[A]):Iterator[kantan.regex.DecodeResult[A]]
[`MatchDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$MatchDecoder.html
[`ordered`]:{{ site.baseurl }}/api/kantan/regex/GeneratedMatchDecoders.html#ordered[A1,A2,O](f:(A1,A2)=>O)(implicitevidence$5:kantan.regex.GroupDecoder[A1],implicitevidence$6:kantan.regex.GroupDecoder[A2]):kantan.regex.MatchDecoder[O]
