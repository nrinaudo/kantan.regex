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

package kantan.regex.ops

import kantan.codecs.Result
import kantan.regex.{Compiler, Regex, _}

/** Enriches `String` with useful regex-related syntax. */
final class StringOps(val str: String) extends AnyVal {
  private def eval[A](s: String, r: CompileResult[Regex[DecodeResult[A]]]): Iterator[RegexResult[A]] =
    r.map(_.eval(s)).recover { case e ⇒ Iterator(Result.failure(e)) }.get

  /** Shorthand for [[Regex.eval]]. */
  def evalRegex[A](r: Regex[A]): Iterator[A] = r.eval(str)

  /** Compiles and evaluates the specified regular expression against this string. */
  def evalRegex[A: MatchDecoder](expr: String): Iterator[RegexResult[A]] =
    eval(str, Compiler[String].compile(expr))

  /** Compiles and evaluates the specified regular expression against this string.
    *
    * This method is useful when extracting simple types (that is, types that have a [[GroupDecoder]]) from groups
    * rather than entire matches.
    *
    * For example, imagine that we have the following regular expression: `\[(\d+)\]`. Each match will contain the
    * wrapping brackets - `[123]`, for example. In order to only extract information from the first group rather
    * than the entire match, one would write:
    * {{{
    *   str.evalRegex[Int]("\\[(\\d+)\\]", 1)
    * }}}
    *
    * @param expr  regular expression to compile and evaluate.
    * @param group index of the group from which to extract data in each match.
    */
  def evalRegex[A: GroupDecoder](expr: String, group: Int): Iterator[RegexResult[A]] =
    eval(str, Compiler[String].compile(expr, group))

  /** Unsafe version of [[evalRegex[A](expr:String)*]] .*/
  def unsafeEvalRegex[A: MatchDecoder](expr: String): Iterator[A] =
    evalRegex(Compiler[String].unsafeCompile[A](expr)).map(_.get)

  /** Unsafe version of [[evalRegex[A](expr:String,group:Int)*]] .*/
  def unsafeEvalRegex[A: GroupDecoder](expr: String, group: Int): Iterator[A] =
    evalRegex(Compiler[String].unsafeCompile[A](expr, group)).map(_.get)
}

trait ToStringOps {
  implicit def toRegexStringOps(str: String): StringOps = new StringOps(str)
}

object string extends ToStringOps
