---
layout: tutorial
title: "Refined module"
section: tutorial
sort_order: 13
---
kantan.regex comes with a [refined](https://github.com/fthomas/refined) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-refined" % "0.5.2"
```

You then need to import the corresponding package:

```scala
import kantan.regex.refined._
```

And that's pretty much it. You can now decode refined types directly.

Let's first set our types up:

```scala
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import kantan.regex.implicits._

type PositiveInt = Int Refined Positive
```

We can then simply write the following:

```scala
"[123]".evalRegex[PositiveInt](rx"\[([+-]?\d+)\]", 1).toList
// res0: List[kantan.regex.package.DecodeResult[PositiveInt]] = List(
//   Right(123)
// )
```

And, for an error case:

```scala
"[-123]".evalRegex[PositiveInt](rx"\[([+-]?\d+)\]", 1).toList
// res1: List[kantan.regex.package.DecodeResult[PositiveInt]] = List(
//   Left(TypeError("Not acceptable: 'Predicate failed: (-123 > 0).'"))
// )
```
