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

import kantan.regex.DecodeResult
import kantan.regex.GroupDecoder
import kantan.regex.MatchDecoder
import kantan.regex.Pattern
import kantan.regex.Regex

/** Enriches `String` with useful regex-related syntax. */
final class StringOps(val str: String) extends AnyVal {
  private def eval[A](s: String, r: Regex[DecodeResult[A]]): Iterator[DecodeResult[A]] =
    r.eval(s)

  /** Shorthand for [[Regex.eval]]. */
  def evalRegex[A](r: Regex[A]): Iterator[A] =
    r.eval(str)

  def evalRegex[A: MatchDecoder](p: Pattern): Iterator[DecodeResult[A]] =
    eval(str, Regex[A](p))

  def evalRegex[A: GroupDecoder](p: Pattern, group: Int): Iterator[DecodeResult[A]] =
    eval(str, Regex[A](p, group))

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def unsafeEvalRegex[A: MatchDecoder](p: Pattern): Iterator[A] =
    evalRegex(p).map(_.fold(e => throw e, identity))

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def unsafeEvalRegex[A: GroupDecoder](p: Pattern, group: Int): Iterator[A] =
    evalRegex(p, group).map(_.fold(e => throw e, identity))
}

trait ToStringOps {
  implicit def toRegexStringOps(str: String): StringOps =
    new StringOps(str)
}

object string extends ToStringOps
