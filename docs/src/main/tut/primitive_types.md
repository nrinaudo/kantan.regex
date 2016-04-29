---
layout: tutorial
title: "Extracting primitive types"
section: tutorial
sort: 1
---
When working with regular expressions, it's fairly common to want to extract matched groups and turn them into a useful
type - a depressingly recurrent scenario being to extract simple integers from strings.

Let's imagine that we get the following string and are interested in the integers between brackets: 

```tut
val input = "lorem ipsum [123] dolor si amet [456] DO NOT MATCH THIS 789."
```

```tut:silent
val regex = "\\d+"
```

```tut
regex.r.findAllIn(input).map(_.toInt).toList
```

```tut:silent
import kantan.regex.ops._
```

```tut
regex.regex[Int].eval(input).toList
```


```tut:silent
val regex = "\\[(\\d+)\\]"
```

```tut
regex.r.findAllMatchIn(input).map(_.group(1).toInt).toList
```

```tut:silent
import kantan.regex._

implicit val decoder = MatchDecoder.fromGroup[Int](1)
```