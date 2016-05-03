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

import java.util.regex.Matcher

trait Regex[A]  { self ⇒
  def map[B](f: A ⇒ B): Regex[B] = new Regex[B] {
    override def eval(s: String) =  self.eval(s).map(f)
  }

  def eval(str: String): Iterator[A]
}

/** Provides [[Regex]] compilation methods. */
object Regex {
  def compile[S, A](expr: S, group: Int)
                   (implicit da: GroupDecoder[A], cs: Compiler[S]): CompileResult[Regex[DecodeResult[A]]] =
    compile(expr)(MatchDecoder.fromGroup(group), cs)

  /** Attempts to compile the specified string as a [[Regex]].
    *
    * This method is safe - results are wrapped in a [[CompileResult]] value.
    */
  def compile[S, A](expr: S)(implicit da: MatchDecoder[A], cs: Compiler[S]): CompileResult[Regex[DecodeResult[A]]] = {
    cs.compile(expr).map { pattern ⇒
      new Regex[DecodeResult[A]] {
        override def eval(s: String) = new MatchIterator(pattern.matcher(s)).map(m ⇒ da.decode(m))
        override def toString() = pattern.toString
      }
    }
  }

  def unsafeCompile[S, A](expr: S, group: Int)
                         (implicit da: GroupDecoder[A], cs: Compiler[S]): Regex[DecodeResult[A]] =
    compile(expr, group).get


  def unsafeCompile[S: Compiler, A: MatchDecoder](expr: S): Regex[DecodeResult[A]] = compile(expr).get
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
