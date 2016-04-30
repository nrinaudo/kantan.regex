---
layout: tutorial
title: "Extracting collections"
section: tutorial
sort: 2
---

```tut
val input = "[1, 2] and then [3, 4] followed by [5, 6, 7]"
```

```tut:silent
val regex = "\\[(\\d+), (\\d+)(?:, (\\d+))?\\]"
```

```tut
regex.r.findAllMatchIn(input).toList
```

```tut:silent
import kantan.regex.ops._
```

```tut
regex.regex[List[Int]].eval(input).toList
```
