---
layout: tutorial
title: "Extracting case classes"
section: tutorial
sort: 4
---

```tut
val input = "[1, 2] and then [3, 4] followed by [5, 6, 7]"
```

```tut:silent
val regex = "\\[(\\d+), (\\d+)(?:, (\\d+))?\\]"
```

```tut:silent
import kantan.regex._
import kantan.regex.ops._

case class Point(x: Int, y: Int, z: Option[Int])
```

```tut:silent
implicit val decoder: MatchDecoder[Point] = MatchDecoder.decoder(1, 2, 3)(Point.apply)
```

```tut
regex.regex[Point].eval(input).toList
```