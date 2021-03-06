---
layout: tutorial
title: "Refined module"
section: tutorial
sort_order: 13
---
kantan.regex comes with a [refined](https://github.com/fthomas/refined) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-refined" % "@VERSION@"
```

You then need to import the corresponding package:

```scala mdoc:silent
import kantan.regex.refined._
```

And that's pretty much it. You can now decode refined types directly.

Let's first set our types up:

```scala mdoc:silent
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import kantan.regex.implicits._

type PositiveInt = Int Refined Positive
```

We can then simply write the following:

```scala mdoc
"[123]".evalRegex[PositiveInt](rx"\[([+-]?\d+)\]", 1).toList
```

And, for an error case:

```scala mdoc
"[-123]".evalRegex[PositiveInt](rx"\[([+-]?\d+)\]", 1).toList
```
