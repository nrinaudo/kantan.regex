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

package kantan.regex.cats

import _root_.cats.data.Xor
import kantan.regex.cats.arbitrary._
import kantan.regex.laws._
import kantan.regex.laws.discipline._
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class XorDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  // These are necessary to disambiguate between the free LegalGroup and free LegalValue[Xor] implicits.
  implicit val legalGroup: Arbitrary[LegalGroup[Int Xor Boolean]] = arbLegalXor
  implicit val illegalGroup: Arbitrary[IllegalGroup[Int Xor Boolean]] = arbIllegalXor
  implicit val legalMatch: Arbitrary[LegalMatch[Int Xor Boolean]] = arbLegalXor
  implicit val illegalMatch: Arbitrary[IllegalMatch[Int Xor Boolean]] = arbIllegalXor

  checkAll("GroupDecoder[Int Xor Boolean]", GroupDecoderTests[Int Xor Boolean].decoder[Int, Int])
  checkAll("MatchDecoder[Int Xor Boolean]", MatchDecoderTests[Int Xor Boolean].decoder[Int, Int])
}
