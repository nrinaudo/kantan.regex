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

trait Regex[A]  { self ⇒
  def map[B](f: A ⇒ B): Regex[B] = new Regex[B] {
    override def eval(s: String) =  self.eval(s).map(f)
  }

  def eval(str: String): Iterator[A]
}

/** Provides [[Regex]] compilation methods. */
object Regex {
  /** Attempts to compile the specified string as a [[Regex]].
    *
    * This method is safe - results are wrapped in a [[CompileResult]] value.
    */
  def compile[A](expr: String)(implicit da: MatchDecoder[A]): CompileResult[Regex[DecodeResult[A]]] = {
    CompileResult {
      val pattern = Pattern.compile(expr)

      new Regex[DecodeResult[A]] {
        override def eval(s: String) = new MatchIterator(pattern.matcher(s)).map(m ⇒ da.decode(m))
        override def toString() = pattern.toString
      }
    }
  }

  /** Attempts to compile the specified string as a [[Regex]].
    *
    * This method is unsafe - errors result in thrown exceptions. It's often better to use the safe alternative,
    * [[compile]].
    */
  def unsafeCompile[A: MatchDecoder](expr: String): Regex[DecodeResult[A]] = compile(expr).get
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
