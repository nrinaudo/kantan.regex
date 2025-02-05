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

package kantan.regex.generic

import kantan.regex.DecodeResult
import kantan.regex.Match
import kantan.regex.MatchDecoder

/** Custom [[MatchDecoder]] implementation for decoding `HList`. */
trait DerivedMatchDecoder[A] extends MatchDecoder[A] {
  def decodeFrom(e: Match, index: Int): DecodeResult[A]
  override def decode(e: Match) =
    decodeFrom(e, 1)
}

object DerivedMatchDecoder {
  def apply[A](implicit ev: DerivedMatchDecoder[A]): DerivedMatchDecoder[A] =
    macro imp.summon[DerivedMatchDecoder[A]]
}
