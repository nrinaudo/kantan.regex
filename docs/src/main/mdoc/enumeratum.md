---
layout: tutorial
title: "Enumeratum module"
section: tutorial
sort_order: 14
---
kantan.regex comes with an [enumeratum](https://github.com/lloydmeta/enumeratum) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-enumeratum" % "@VERSION@"
```

## Name-based enumerations

When working with enumerations of type `Enum`, you should import the following package:

```scala mdoc:silent
import kantan.regex.enumeratum._
```

And that's pretty much it. You can now encode and decode your enumeration directly.

Let's first set our types up:

```scala mdoc:silent
import enumeratum._

// We need to put this all in a faked out package object due to the way
// documentation is built.
object somePackage {
  sealed trait DummyEnum extends EnumEntry

  object DummyEnum extends Enum[DummyEnum] {

    val values = findValues

    case object Hello   extends DummyEnum
    case object GoodBye extends DummyEnum
    case object Hi      extends DummyEnum

  }
}
```

And a few further imports, to bring our enumeration and the kantan.csv syntax in scope:

```scala mdoc:silent
import kantan.regex.implicits._
import somePackage._
```


We can then simply write the following:

```scala mdoc
"[Hello]".evalRegex[DummyEnum](rx"\[([a-zA-Z]+)]", 1).toList

"[GoodDay]".evalRegex[DummyEnum](rx"\[([a-zA-Z]+)]", 1).toList
```



## Value-based enumerations

For enumerations of type `ValueEnum`, you should import the following package:

```scala mdoc:silent:reset
import kantan.regex.enumeratum.values._
```

And that's pretty much it. You can now encode and decode your enumeration directly.

Let's first set our types up:

```scala mdoc:silent
import enumeratum.values._

// We need to put this all in a faked out package object due to the way
// documentation is built.
object somePackage {

  sealed abstract class Greeting(val value: Int) extends IntEnumEntry

  object Greeting extends IntEnum[Greeting] {

    val values = findValues

    case object Hello   extends Greeting(1)
    case object GoodBye extends Greeting(2)
    case object Hi      extends Greeting(3)
    case object Bye     extends Greeting(4)

  }

}
```

And a few further imports, to bring our enumeration and the kantan.csv syntax in scope:

```scala mdoc:silent
import kantan.regex.implicits._
import somePackage._
```

We can then simply write the following:

```scala mdoc
"[1]".evalRegex[Greeting](rx"\[([+-]?\d+)\]", 1).toList

"[-2]".evalRegex[Greeting](rx"\[([+-]?\d+)\]", 1).toList
```
