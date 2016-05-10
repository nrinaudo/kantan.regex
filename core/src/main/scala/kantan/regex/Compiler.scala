/*
 * Copyright 2016 Nicolas Rinaudo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kantan.regex

import java.util.regex.{Matcher, Pattern}

/** Type class for types that can be compiled to instances of [[Regex]].
  *
  * The vast majority of the time, there should be no reason to interact with this as default instances are provided
  * for most types anyone's likely to need.
  */
trait Compiler[E] {
  /** Compiles the specified expression into a [[Regex]]. */
  def compile[B](expr: E)(implicit db: MatchDecoder[B]): CompileResult[Regex[DecodeResult[B]]]

  /** Compiles the specified expression into a [[Regex]].
    *
    * Each match is turned into a `B` by decoding group `group` using `B`'s implicit [[GroupDecoder]] instance.
    */
  def compile[B: GroupDecoder](expr: E, group: Int): CompileResult[Regex[DecodeResult[B]]] =
    compile(expr)(MatchDecoder.fromGroup[B](group))

  /** Unsafe version of `compile`. */
  def unsafeCompile[B: MatchDecoder](expr: E): Regex[DecodeResult[B]] = compile(expr).get

  /** Unsafe version of `compile`. */
  def unsafeCompile[B: GroupDecoder](expr: E, group: Int): Regex[DecodeResult[B]] = compile(expr, group).get
}

/** Provides default instances, instance creation and instance summoning methods. */
object Compiler {
  /** Returns an instance of [[Compiler]] for `A` if one is found in scope, fails compilation otherwise. */
  def apply[A](implicit ca: Compiler[A]): Compiler[A] = ca

  /** Creates a new [[Compiler]] instance from a function that turns `A` into a `Pattern`. */
  def fromPattern[A](f: A ⇒ CompileResult[Pattern]): Compiler[A] = new Compiler[A] {
    override def compile[B](expr: A)(implicit db: MatchDecoder[B]): CompileResult[Regex[DecodeResult[B]]] =
      f(expr).map { pattern ⇒
        new Regex[DecodeResult[B]] {
          override def eval(s: String) = new MatchIterator(pattern.matcher(s)).map(m ⇒ db.decode(m))
          override def toString = pattern.toString
        }
      }
  }

  /** Provides compilation for Scala Regexes. */
  implicit val scalaRegex: Compiler[scala.util.matching.Regex] = fromPattern(r ⇒ CompileResult.success(r.pattern))
  /** Provides compilation for Java Patterns. */
  implicit val pattern: Compiler[Pattern] = fromPattern(p ⇒ CompileResult.success(p))
  /** Provides compilation for Strings. */
  implicit val string: Compiler[String] = fromPattern(s ⇒ CompileResult(Pattern.compile(s)))
}

private class MatchIterator(val matcher: Matcher) extends Iterator[Match] {
  var nextSeen = false
  val m = new Match(matcher)

  override def hasNext = {
    if(!nextSeen) nextSeen = matcher.find()
    nextSeen
  }
  override def next(): Match = {
    if(!hasNext) throw new NoSuchElementException
    nextSeen = false
    m
  }
}
