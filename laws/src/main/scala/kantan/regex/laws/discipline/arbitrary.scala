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

package kantan.regex.laws.discipline

import java.util.regex.Pattern
import kantan.codecs.Result
import kantan.codecs.laws._
import kantan.regex._
import kantan.regex.DecodeError.{NoSuchGroupId, NoSuchGroupName}
import kantan.regex.laws._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.{arbitrary => arb}
import org.scalacheck.Gen._

object arbitrary extends kantan.regex.laws.discipline.ArbitraryInstances
                         with kantan.regex.laws.discipline.ArbitraryArities

trait ArbitraryInstances {
  // - Arbitrary errors ------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val arbCompileError: Arbitrary[CompileError] =
    Arbitrary(const(CompileError(new Exception)))
  implicit val arbNoSuchGroupId: Arbitrary[NoSuchGroupId] =
    Arbitrary(arb[Int].map(NoSuchGroupId.apply))
  implicit val arbNoSuchGroupName: Arbitrary[NoSuchGroupName] =
    Arbitrary(identifier.map(NoSuchGroupName.apply))
  implicit val arbDecodeError: Arbitrary[DecodeError] =
    Arbitrary(oneOf(arb[NoSuchGroupId], arb[NoSuchGroupName], const(DecodeError.TypeError(new Exception))))
  implicit val arbRegexError: Arbitrary[RegexError] =
    Arbitrary(oneOf(arb[DecodeError], arb[CompileError]))


  // - Arbitrary results -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit def arbDecodeResult[A](implicit arbA: Arbitrary[A]): Arbitrary[DecodeResult[A]] =
    Arbitrary(oneOf(arbA.arbitrary.map(Result.Success.apply), arbDecodeError.arbitrary.map(Result.Failure.apply)))


  // - Arbitrary matches -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  private def toMatch(str: String): Match = {
    val matcher = Pattern.compile(".*").matcher(str)
    matcher.find()
    new Match(matcher)
  }

  implicit def arbLegalMatch[A](implicit la: Arbitrary[LegalString[A]]): Arbitrary[LegalMatch[A]] = Arbitrary {
    la.arbitrary.map(_.mapEncoded(toMatch))
  }

  implicit def arbIllegalMatch[A](implicit ia: Arbitrary[IllegalString[A]]): Arbitrary[IllegalMatch[A]] = Arbitrary {
    ia.arbitrary.map(_.mapEncoded(toMatch))
  }
}
