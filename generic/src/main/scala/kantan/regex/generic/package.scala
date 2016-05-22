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

import kantan.regex.export.Exported
import shapeless._

package object generic {
  // - HList decoders --------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** [[MatchDecoder]] instance for `HList`, provided all elements in the `HList` have a [[GroupDecoder]]. */
  implicit def hlistMatchDecoder[H, T <: HList]
  (implicit dh: GroupDecoder[H], dt: DerivedMatchDecoder[T]): DerivedMatchDecoder[H :: T] =
    new DerivedMatchDecoder[H :: T] {
      override def decodeFrom(e: Match, index: Int): DecodeResult[H :: T] = for {
        h ← e.decode[H](index)
        t ← dt.decodeFrom(e, index + 1)
      } yield h :: t
    }

  /** [[MatchDecoder]] for `HNil` (always succeeds). */
  implicit val hnilMatchDecoder: DerivedMatchDecoder[HNil] = new DerivedMatchDecoder[HNil] {
    override def decodeFrom(e: Match, index: Int) = DecodeResult.success(HNil)
  }

  /** [[GroupDecoder]] for `HList` of size 1, provided the single element has a [[GroupDecoder]]. */
  implicit def hlistGroupDecoder[H](implicit dh: GroupDecoder[H]): GroupDecoder[H :: HNil] = dh.map(h ⇒ h :: HNil)



  // - Coproduct decoders ----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** [[GroupDecoder]] instance for `Coproduct`s, provided all alternatives have a [[GroupDecoder]]. */
  implicit def coproductGroupDecoder[H, T <: Coproduct]
  (implicit dh: GroupDecoder[H], dt: GroupDecoder[T]): GroupDecoder[H :+: T] =
    GroupDecoder(m ⇒ dh.decode(m).map(Inl.apply).orElse(dt.decode(m).map(Inr.apply)))

  /** [[MatchDecoder]] instance for `Coproduct`s, provided all alternatives have a [[MatchDecoder]]. */
  implicit def coproductMatchDecoder[H, T <: Coproduct]
  (implicit dh: MatchDecoder[H], dt: MatchDecoder[T]): MatchDecoder[H :+: T] =
    MatchDecoder(m ⇒ dh.decode(m).map(Inl.apply).orElse(dt.decode(m).map(Inr.apply)))

  /** End case for [[coproductMatchDecoder]], fails the decoding process. */
  implicit val cnilMatchDecoder: MatchDecoder[CNil] =
    MatchDecoder(m ⇒ DecodeResult.typeError(s"Failed to decode $m as a coproduct"))

  /** End case for [[coproductGroupDecoder]], fails the decoding process. */
  implicit val cnilGroupDecoder: GroupDecoder[CNil] =
    GroupDecoder(g ⇒ DecodeResult.typeError(s"Failed to decode $g as a coproduct"))



  // - Case class decoders ---------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** Provides [[MatchDecoder]] instances for case classes where all fields have a [[GroupDecoder]]. */
  implicit def caseClassMatchDecoder[A, R <: HList]
  (implicit gen: Generic.Aux[A, R], ev: R <:< HList, dr: MatchDecoder[R]): Exported[MatchDecoder[A]] =
    Exported(MatchDecoder(s ⇒ dr.decode(s).map(gen.from)))

  /** Provides [[GroupDecoder]] instances for case classes with exactly one field, provided it has a
    * [[GroupDecoder]]. */
  implicit def caseClassGroupDecoder[A, R <: HList]
  (implicit gen: Generic.Aux[A, R], ev: R <:< HList, dr: GroupDecoder[R]): Exported[GroupDecoder[A]] =
    Exported(GroupDecoder(s ⇒ dr.decode(s).map(gen.from)))



  // - Sum type decoders -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** Provides [[GroupDecoder]] instances for sum types where all alternatives have a [[GroupDecoder]]. */
  implicit def sumTypeMatchDecoder[A, R <: Coproduct]
  (implicit gen: Generic.Aux[A, R], dr: MatchDecoder[R]): Exported[MatchDecoder[A]] =
    Exported(MatchDecoder(m ⇒ dr.decode(m).map(gen.from)))

  /** Provides [[MatchDecoder]] instances for sum types where all alternatives have a [[MatchDecoder]]. */
  implicit def sumTypeGroupDecoder[A, R <: Coproduct]
  (implicit gen: Generic.Aux[A, R], dr: GroupDecoder[R]): Exported[GroupDecoder[A]] =
    Exported(GroupDecoder(group ⇒ dr.decode(group).map(gen.from)))
}
