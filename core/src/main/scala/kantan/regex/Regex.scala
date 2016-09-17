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

/** Compiled version of a regular expression. */
trait Regex[A] { self ⇒
  /** Turns a `Regex[A]` into a `Regex[B]` by applying the specified function to each result. */
  def map[B](f: A ⇒ B): Regex[B] = new Regex[B] {
    override def eval(str: String) = self.eval(str).map(f)
  }

  /** Evaluates the regular expression against the specified string. */
  def eval(str: String): Iterator[A]
}

object Regex {
  /** Turns the specified pattern into a [[Regex]].
    *
    * Decoding is achieved by applying whatever [[MatchDecoder]] is in scope for `A` on each match.
    *
    * This method is made with [[kantan.regex.literals.RegexLiteral literals]] in mind. Should you be working with
    * other types (such as non-literal strings, for instance), [[kantan.regex.ops.CompilerOps]] might prove more
    * helpful.
    */
  def apply[A: MatchDecoder](pattern: Pattern): Regex[DecodeResult[A]] = new Regex[DecodeResult[A]] {
    override def eval(s: String) = new MatchIterator(pattern.matcher(s)).map(m ⇒ MatchDecoder[A].decode(m))
    override def toString = pattern.toString
  }

  /** Turns the specified pattern into a [[Regex]].
    *
    * Decoding is achieved by applying whatever [[GroupDecoder]] is in scope for `A` on group index `group` of each
    * match.
    *
    * This method is made with [[kantan.regex.literals.RegexLiteral literals]] in mind. Should you be working with
    * other types (such as non-literal strings, for instance), [[kantan.regex.ops.CompilerOps]] might prove more
    * helpful.
    */
  def apply[A: GroupDecoder](pattern: Pattern, group: Int): Regex[DecodeResult[A]] =
    Regex(pattern)(MatchDecoder.fromGroup(group))
}
