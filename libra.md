---
layout: tutorial
title: "Libra module"
section: tutorial
sort_order: 15
---
kantan.regex comes with a [libra](https://github.com/to-ithaca/libra) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-libra" % "0.4.0"
```

You then need to import the corresponding package:

```scala
import kantan.regex.libra._
```

And that's pretty much it. You can now decode refined types directly.

Let's first set our types up:

```scala
import libra._
import libra.si._
import kantan.regex.implicits._

type Duration = QuantityOf[Int, Time, Second]
```

We can then simply write the following:

```scala
scala> "[123]".evalRegex[Duration](rx"\[([+-]?\d+)\]", 1).toList
res1: List[kantan.regex.DecodeResult[Duration]] = List(Right(Quantity(123)))
```
