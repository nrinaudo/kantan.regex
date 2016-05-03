---
layout: tutorial
title: "Extracting tuples"
section: tutorial
sort: 2
---

```tut
val input = "[1, 2] and then [3, 4] followed by [5, 6, 7]"
```

```tut:silent
val regex = "\\[(\\d+), (\\d+)(?:, (\\d+))?\\]"
```

```tut:silent
import kantan.regex.ops._
```

```tut
regex.asUnsafeRegex[(Int, Int, Option[Int])].eval(input).toList
```
