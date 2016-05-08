---
layout: tutorial
title: "Extracting case classes"
section: tutorial
sort: 3
---
We've already seen how to extract [primitive types](primitive_types.html) and [tuples](tuples.html) from regular
expression matches, but a more common requirement is to extract case classes - often preferable to tuples, since they
provide more meaningful types.

Let's take the following input and attempt to extract the bits between brackets: 

```tut:silent
val input = "[1, 2] and [3, false]"
```

This could be achieved with the following regular expression:

```tut
val regex = "\\[(\\d+), (\\d+|true|false)\\]"
```

And since we're trying to demonstrate case class extraction, let's extract data to the following case class:

```tut:silent
case class WeirdPoint(x: Int, y: Either[Int, Boolean])
```

In order to extract complex (as in, composed of more than one value) types, we need to provide instances of
[`MatchDecoder`] for them. This is done with one of [`MatchDecoder`]'s many helper methods - [`ordered`], in our case,
since match groups and case class fields are in the same order:

```tut:silent
import kantan.regex._
import kantan.regex.ops._

implicit val decoder: MatchDecoder[WeirdPoint] = MatchDecoder.ordered(WeirdPoint.apply _)
```

And that's all there is to it. Now that we have this decoder in place, we can just call [`evalRegex`] as usual:

```tut
input.evalRegex[WeirdPoint](regex).foreach(println _)
```

[`evalRegex`]:{{ site.baseUrl }}/api/index.html#kantan.regex.ops$$StringOps@evalRegex[A](expr:String,group:Int)(implicitevidence$2:kantan.regex.GroupDecoder[A]):Iterator[kantan.regex.RegexResult[A]]
[`MatchDecoder`]:{{ site.baseUrl }}/api/index.html#kantan.regex.package@MatchDecoder[A]=kantan.codecs.Decoder[kantan.regex.Match,A,kantan.regex.DecodeError,kantan.regex.codecs.type]
[`ordered`]:{{ site.baseUrl }}/api/index.html#kantan.regex.MatchDecoder$@ordered[A1,A2,O](f:(A1,A2)=>O)(implicitda1:kantan.regex.GroupDecoder[A1],implicitda2:kantan.regex.GroupDecoder[A2]):kantan.regex.MatchDecoder[O]