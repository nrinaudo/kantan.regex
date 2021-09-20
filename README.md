# kantan.regex

[![Build Status](https://travis-ci.org/nrinaudo/kantan.regex.svg?branch=master)](https://travis-ci.org/nrinaudo/kantan.regex)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.nrinaudo/kantan.regex_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.nrinaudo/kantan.regex_2.13)
[![Join the chat at https://gitter.im/nrinaudo/kantan.regex](https://img.shields.io/badge/gitter-join%20chat-52c435.svg)](https://gitter.im/nrinaudo/kantan.regex)

There are two main use-cases for regular expressions:

* predicates (does a string match a given pattern?)
* data extraction (grab whatever matches a given pattern)

Kantan.regex is solely concerned with the second use case, and is meant to make data extraction from strings as
painless as possible - you still have to write regular expressions, so still a *bit* painful, but at least the rest
is automated away and checked by the compiler.

Documentation and tutorials are available on the [companion site](https://nrinaudo.github.io/kantan.regex/), but for
those looking for a few quick examples:

```scala
import kantan.regex._
import kantan.regex.implicits._
import kantan.regex.generic._

// Returns an iterator on all parts of str that look like a positive integer
"abc 123 def".evalRegex[Int](rx"\d+")

// Returns an iterator on all parts of str that look like an (x, y) point. Points
// are represented as a Tuple2[Int, Int]
"(1, 2) and then (3, 4)".evalRegex[(Int, Int)](rx"\((\d+), (\d+)\)")

// Declares a new Point case class, lets shapeless work out how to decode for it.
case class Point(x: Int, y: Int)

// Returns an iterator on all parts of str that look like an (x, y) point. Points
// are represented as Point.
"(1, 2) and then (3, 4)".evalRegex[Point](rx"\((\d+), (\d+)\)")

// A somewhat contrived example where the z-coordinate of a point is optional:
"(1, 2) and then (3, 4, 5)".evalRegex[(Int, Int, Option[Int])](rx"\((\d+), (\d+)(?:, (\d+))?\)")
```

kantan.regex is distributed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html).
