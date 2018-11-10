---
layout: tutorial
title: "Enumeratum module"
section: tutorial
sort_order: 14
---
kantan.regex comes with an [enumeratum](https://github.com/lloydmeta/enumeratum) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.nrinaudo" %% "kantan.regex-enumeratum" % "0.5.0"
```

## Name-based enumerations

When working with enumerations of type `Enum`, you should import the following package:

```scala
import kantan.regex.enumeratum._
```

And that's pretty much it. You can now encode and decode your enumeration directly.

Let's first set our types up:

```scala
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

```scala
import kantan.regex.implicits._
import somePackage._
```


We can then simply write the following:

```scala
scala> "[Hello]".evalRegex[DummyEnum](rx"\[([a-zA-Z]+)]", 1).toList
res2: List[kantan.regex.DecodeResult[somePackage.DummyEnum]] = List(Right(Hello))

scala> "[GoodDay]".evalRegex[DummyEnum](rx"\[([a-zA-Z]+)]", 1).toList
res3: List[kantan.regex.DecodeResult[somePackage.DummyEnum]] = List(Left(TypeError: 'GoodDay' is not a member of enumeration [Hello, GoodBye, Hi]))
```



## Value-based enumerations

For enumerations of type `ValueEnum`, you should import the following package:

```scala
import kantan.regex.enumeratum.values._
```

And that's pretty much it. You can now encode and decode your enumeration directly.

Let's first set our types up:

```scala
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

```scala
import kantan.regex.implicits._
import somePackage._
```

We can then simply write the following:

```scala
scala> "[1]".evalRegex[Greeting](rx"\[([+-]?\d+)\]", 1).toList
res2: List[kantan.regex.DecodeResult[somePackage.Greeting]] = List(Right(Hello))

scala> "[-2]".evalRegex[Greeting](rx"\[([+-]?\d+)\]", 1).toList
res3: List[kantan.regex.DecodeResult[somePackage.Greeting]] = List(Left(TypeError: '-2' is not in values [1, 2, 3, 4]))
```
