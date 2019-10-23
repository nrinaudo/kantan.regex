---
layout: index
---

[![Build Status](https://travis-ci.org/nrinaudo/kantan.regex.svg?branch=master)](https://travis-ci.org/nrinaudo/kantan.regex)
[![codecov](https://codecov.io/gh/nrinaudo/kantan.regex/branch/master/graph/badge.svg)](https://codecov.io/gh/nrinaudo/kantan.regex)
[![Latest version](https://index.scala-lang.org/nrinaudo/kantan.regex/kantan.regex/latest.svg)](https://index.scala-lang.org/nrinaudo/kantan.regex)
[![Join the chat at https://gitter.im/nrinaudo/kantan.regex](https://img.shields.io/badge/gitter-join%20chat-52c435.svg)](https://gitter.im/nrinaudo/kantan.regex)

kantan.regex is a library for extracting useful types from regular expression matches written in the
[Scala programming language](http://www.scala-lang.org).

## Getting started

kantan.regex is currently available for Scala 2.11 and 2.12.

The current version is `0.5.1`, which can be added to your project with one or more of the following line(s)
in your SBT build file:

```scala
// Core library, included automatically if any other module is imported.
libraryDependencies += "com.nrinaudo" %% "kantan.regex" % "0.5.1"

// Java 8 date and time instances.
libraryDependencies += "com.nrinaudo" %% "kantan.regex-java8" % "0.5.1"

// Provides generic instance derivation through shapeless.
libraryDependencies += "com.nrinaudo" %% "kantan.regex-generic" % "0.5.1"

// Provides scalaz type class instances.
libraryDependencies += "com.nrinaudo" %% "kantan.regex-scalaz" % "0.5.1"

// Provides cats type class instances.
libraryDependencies += "com.nrinaudo" %% "kantan.regex-cats" % "0.5.1"

// Provides joda-time decoders.
libraryDependencies += "com.nrinaudo" %% "kantan.regex-joda-time" % "0.5.1"

// Provides refined decoders.
libraryDependencies += "com.nrinaudo" %% "kantan.regex-refined" % "0.5.1"

// Provides enumeratum decoders.
libraryDependencies += "com.nrinaudo" %% "kantan.regex-enumeratum" % "0.5.1"

// Provides libra decoders.
libraryDependencies += "com.nrinaudo" %% "kantan.regex-libra" % "0.5.1"
```

## Motivation

Regular expressions, for all their flaws, are still extremely useful to extract content from raw strings. Scala,
unfortunately, doesn't do much with that - the regex library is great for checking matches, but not extracting
well-typed data from them.

Kantan.regex is meant to fill that void - and nothing else. By that, I mean that if you need to use regular expressions
as predicates (does a string match a certain pattern?), kantan.regex is absolutely not the right tool for the job. If,
on the other hand, you need to extract bits of strings as custom, composite types, in a safe way and checked at compile
time, then it might just be.
