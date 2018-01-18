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
package laws.discipline

import DecodeError.{NoSuchGroupId, TypeError}
import imp.imp
import java.util.regex.Pattern
import kantan.codecs.laws._
import laws._
import org.scalacheck.{Arbitrary, Cogen, Gen}
import org.scalacheck.Arbitrary.{arbitrary ⇒ arb}
import org.scalacheck.Gen._
import org.scalacheck.rng.Seed

object arbitrary extends kantan.regex.laws.discipline.ArbitraryInstances

trait ArbitraryInstances
    extends kantan.codecs.laws.discipline.ArbitraryInstances with kantan.regex.laws.discipline.ArbitraryArities {
  // - Arbitrary errors ------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val arbCompileError: Arbitrary[CompileError] =
    Arbitrary(arbException.arbitrary.map(e ⇒ CompileError(e)))
  implicit val arbTypeError: Arbitrary[TypeError] =
    Arbitrary(arbException.arbitrary.map(e ⇒ TypeError(e)))
  implicit val arbNoSuchGroupId: Arbitrary[NoSuchGroupId] =
    Arbitrary(arb[Int].map(NoSuchGroupId.apply))
  implicit val arbDecodeError: Arbitrary[DecodeError] =
    Arbitrary(oneOf(arb[NoSuchGroupId], arbTypeError.arbitrary))
  implicit val arbRegexError: Arbitrary[RegexError] =
    Arbitrary(oneOf(arb[DecodeError], arb[CompileError]))

  implicit val cogenRegexDecodeError: Cogen[DecodeError] = Cogen { (seed: Seed, err: DecodeError) ⇒
    err match {
      case DecodeError.EmptyGroup       ⇒ seed
      case DecodeError.NoSuchGroupId(i) ⇒ imp[Cogen[Int]].perturb(seed, i)
      case DecodeError.TypeError(msg)   ⇒ imp[Cogen[String]].perturb(seed, msg)
    }
  }

  // - Arbitrary results -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit def arbDecodeResult[A: Arbitrary]: Arbitrary[DecodeResult[A]] =
    Arbitrary(
      oneOf(
        imp[Arbitrary[A]].arbitrary.map(DecodeResult.success),
        arbDecodeError.arbitrary.map(DecodeResult.failure)
      )
    )

  // - Arbitrary groups -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit def arbLegalGroup[A](implicit la: Arbitrary[LegalString[A]]): Arbitrary[LegalGroup[A]] =
    Arbitrary(la.arbitrary.map(_.mapEncoded(Option.apply).tag[codecs.type]))

  implicit def arbIllegalGroup[A](implicit la: Arbitrary[IllegalString[A]]): Arbitrary[IllegalGroup[A]] =
    Arbitrary(
      Gen.oneOf(
        la.arbitrary.map(_.mapEncoded(Option.apply).tag[codecs.type]),
        Gen.const(CodecValue.IllegalValue[Option[String], A, codecs.type](Option.empty))
      )
    )

  implicit def arbLegalGroupOpt[A](implicit la: Arbitrary[LegalString[A]]): Arbitrary[LegalGroup[Option[A]]] =
    Arbitrary(
      Gen.oneOf(
        la.arbitrary.map(_.mapEncoded(Option.apply).mapDecoded(Option.apply).tag[codecs.type]),
        Gen.const(CodecValue.LegalValue[Option[String], Option[A], codecs.type](Option.empty, Option.empty))
      )
    )

  implicit def arbIllegalGroupOpt[A](implicit la: Arbitrary[IllegalString[A]]): Arbitrary[IllegalGroup[Option[A]]] =
    Arbitrary(la.arbitrary.map(_.mapEncoded(Option.apply).mapDecoded(Option.apply).tag[codecs.type]))

  // - Arbitrary matches -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val cogenMatch: Cogen[Match] = Cogen.cogenString.contramap(_.toString)

  def toMatch(str: String): Match = {
    val matcher = Pattern.compile("(?smiU).*").matcher(str)
    matcher.find()
    new Match(matcher)
  }

  implicit def arbLegalMatch[A](implicit la: Arbitrary[LegalString[A]]): Arbitrary[LegalMatch[A]] =
    Arbitrary(la.arbitrary.map(_.mapEncoded(toMatch).tag[codecs.type]))

  implicit def arbIllegalMatch[A](implicit ia: Arbitrary[IllegalString[A]]): Arbitrary[IllegalMatch[A]] =
    Arbitrary(ia.arbitrary.map(_.mapEncoded(toMatch).tag[codecs.type]))
}
