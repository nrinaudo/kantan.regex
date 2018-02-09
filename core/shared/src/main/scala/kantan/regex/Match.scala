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
import scala.util.Try

/** Represents a single match in a regular expression evaluation.
  *
  * It's usually better not to interact with [[Match]] directly - more often than not, adapting existing instances of
  * [[MatchDecoder]] is the better solution. When in a situation where you *must* deal with such values directly,
  * it's important not to hold onto them: due to the way kantan.regex works internally, [[Match]] is mutable.
  */
class Match private[regex] (private val matcher: Matcher) {

  /** Number of groups in the match. */
  val length: Int =
    // scala.js compatibility: https://github.com/scala-js/scala-js/issues/1727
    // It's acceptable to consider the group count to be 0 here, since it's never going to be used.
    Try(matcher.groupCount).getOrElse(0)

  /** Attempts to decode group `index` as a value of type `A`. */
  def decode[A: GroupDecoder](index: Int): DecodeResult[A] =
    if(index < 0 || index > length) DecodeResult.noSuchGroupId(index)
    else GroupDecoder[A].decode(Option(matcher.group(index)))

  override def toString = matcher.toString

  /** Underlying pattern. */
  def pattern(): Pattern =
    // This was exposed for testing purposes - we need a stable identifier to be able to write a useful Cogen[Match].
    matcher.pattern()
}
