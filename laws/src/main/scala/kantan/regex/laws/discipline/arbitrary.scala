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

  implicit def arbRegex[A: Arbitrary]: Arbitrary[Regex[A]] =
    Arbitrary(
      arb[String ⇒ List[A]].map(
        f ⇒
          new Regex[A] {
            override def eval(str: String) = f(str).iterator
        }
      )
    )

  // - Arbitrary errors ------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val arbCompileError: Arbitrary[CompileError] =
    Arbitrary(arbException.arbitrary.map(e ⇒ CompileError(e)))

  implicit val arbTypeError: Arbitrary[DecodeError.TypeError] =
    Arbitrary(arbException.arbitrary.map(e ⇒ DecodeError.TypeError(e)))

  implicit val arbNoSuchGroupId: Arbitrary[DecodeError.NoSuchGroupId] =
    Arbitrary(arb[Int].map(DecodeError.NoSuchGroupId.apply))

  implicit val arbEmptyGroup: Arbitrary[DecodeError.EmptyGroup.type] =
    Arbitrary(Gen.const(DecodeError.EmptyGroup))

  implicit val arbDecodeError: Arbitrary[DecodeError] =
    Arbitrary(oneOf(arbNoSuchGroupId.arbitrary, arbTypeError.arbitrary, arbEmptyGroup.arbitrary))

  implicit val arbRegexError: Arbitrary[RegexError] =
    Arbitrary(oneOf(arb[DecodeError], arb[CompileError]))

  implicit val cogenRegexCompileError: Cogen[CompileError]               = Cogen[String].contramap(_.message)
  implicit val cogenRegexTypeError: Cogen[DecodeError.TypeError]         = Cogen[String].contramap(_.message)
  implicit val cogenRegexNoSuchGroupId: Cogen[DecodeError.NoSuchGroupId] = Cogen[Int].contramap(_.id)
  implicit val cogenRegexEmptyGroup: Cogen[DecodeError.EmptyGroup.type]  = Cogen[Unit].contramap(_ ⇒ ())
  implicit val cogenRegexDecodeError: Cogen[DecodeError] = Cogen { (seed: Seed, err: DecodeError) ⇒
    err match {
      case error: DecodeError.TypeError       ⇒ cogenRegexTypeError.perturb(seed, error)
      case error: DecodeError.NoSuchGroupId   ⇒ cogenRegexNoSuchGroupId.perturb(seed, error)
      case error: DecodeError.EmptyGroup.type ⇒ cogenRegexEmptyGroup.perturb(seed, error)
    }
  }

  implicit val cogenRegexError: Cogen[RegexError] = Cogen { (seed: Seed, err: RegexError) ⇒
    err match {
      case error: DecodeError  ⇒ cogenRegexDecodeError.perturb(seed, error)
      case error: CompileError ⇒ cogenRegexCompileError.perturb(seed, error)
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

  // - Decoders --------------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------

  implicit def arbGroupDecoder[A: Arbitrary]: Arbitrary[GroupDecoder[A]] =
    Arbitrary(arb[Option[String] ⇒ DecodeResult[A]].map(GroupDecoder.from))

}
