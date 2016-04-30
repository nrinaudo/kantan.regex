---
layout: tutorial
title: "Extracting arbitrary types"
section: tutorial
sort: 6
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

class Point(val x: Int, val y: Int, val z: Option[Int]) {
  override def toString = s"Point($x, $y, $z)"
}
```

```tut:silent
implicit val decoder: MatchDecoder[Point] = MatchDecoder.decoder(1, 2, 3) { (x: Int, y: Int, z: Option[Int]) ⇒
  new Point(x, y, z)
}
```

```tut
regex.regex[Point].eval(input).toList
```
