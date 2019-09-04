---
layout: scala mdocorial
title: "Libra module"
section: scala mdocorial
sort_order: 15
---
kantan.regex comes with a [libra](https://github.com/to-ithaca/libra) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-libra" % "@VERSION@"
```

You then need to import the corresponding package:

```scala mdoc:silent
import kantan.regex.libra._
```

And that's pretty much it. You can now decode refined types directly.

Let's first set our types up:

```scala mdoc:silent
import libra._
import kantan.regex.implicits._

type Duration = QuantityOf[Int, Time, Second]
```

We can then simply write the following:

```scala mdoc
"[123]".evalRegex[Duration](rx"\[([+-]?\d+)\]", 1).toList
```
