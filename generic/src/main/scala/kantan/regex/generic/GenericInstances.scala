/*
 * Copyright 2017 Nicolas Rinaudo
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

import kantan.codecs.shapeless.ShapelessInstances
import kantan.regex._
import shapeless._

trait LowPrirityGenericInstances {
  implicit def hlistSingletonMatchDecoder[H: MatchDecoder]: MatchDecoder[H :: HNil] =
      MatchDecoder[H].map(_ :: HNil)
}

trait GenericInstances extends ShapelessInstances with LowPrirityGenericInstances {
  // - HList decoders --------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** [[MatchDecoder]] instance for `HList`, provided all elements in the `HList` have a [[GroupDecoder]]. */
  implicit def hlistMatchDecoder[H: GroupDecoder, T <: HList: DerivedMatchDecoder]: DerivedMatchDecoder[H :: T] =
    new DerivedMatchDecoder[H :: T] {
      override def decodeFrom(e: Match, index: Int): DecodeResult[H :: T] = for {
        h ← e.decode[H](index)
        t ← DerivedMatchDecoder[T].decodeFrom(e, index + 1)
      } yield h :: t
    }

  /** [[MatchDecoder]] for `HNil` (always succeeds). */
  implicit val hnilMatchDecoder: DerivedMatchDecoder[HNil] = new DerivedMatchDecoder[HNil] {
    override def decodeFrom(e: Match, index: Int) = DecodeResult.success(HNil)
  }

  /** [[GroupDecoder]] for `HList` of size 1, provided the single element has a [[GroupDecoder]]. */
  implicit def hlistGroupDecoder[H: GroupDecoder]: GroupDecoder[H :: HNil] = GroupDecoder[H].map(h ⇒ h :: HNil)


  // - Coproduct decoders ----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val cnilMatchDecoder: MatchDecoder[CNil] =
    cnilDecoder(m ⇒ DecodeError.TypeError(s"Failed to decode $m as a coproduct"))
  implicit val cnilGroupDecoder: GroupDecoder[CNil] =
    cnilDecoder(g ⇒ DecodeError.TypeError(s"Failed to decode $g as a coproduct"))
}
