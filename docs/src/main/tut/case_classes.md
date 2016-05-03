---
layout: tutorial
title: "Extracting case classes"
section: tutorial
sort: 3
---

```tut
val input = "[1, 2] and [3, false]"
```

```tut:silent
val regex = "\\[(\\d+), (\\d+|true|false)\\]"
```

```tut:silent
import kantan.regex._
import kantan.regex.ops._

case class Point(x: Int, y: Either[Int, Boolean])
```

```tut:silent
implicit val decoder: MatchDecoder[Point] = MatchDecoder.ordered(Point.apply _)
```

```tut
regex.asUnsafeRegex[Point].eval(input).toList
```