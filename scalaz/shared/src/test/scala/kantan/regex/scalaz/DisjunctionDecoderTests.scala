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
package scalaz

import _root_.scalaz.\/
import kantan.regex.scalaz.arbitrary._
import laws._
import laws.discipline._
import org.scalacheck.Arbitrary

class DisjunctionDecoderTests extends DisciplineSuite {

  implicit val legalGroup: Arbitrary[LegalGroup[Int \/ Boolean]]     = arbLegalDisjunction
  implicit val illegalGroup: Arbitrary[IllegalGroup[Int \/ Boolean]] = arbIllegalDisjunction
  implicit val legalMatch: Arbitrary[LegalMatch[Int \/ Boolean]]     = arbLegalDisjunction
  implicit val illegalMatch: Arbitrary[IllegalMatch[Int \/ Boolean]] = arbIllegalDisjunction

  checkAll("GroupDecoder[Int \\/ Boolean]", GroupDecoderTests[Int \/ Boolean].decoder[Int, Int])
  checkAll("MatchDecoder[Int \\/ Boolean]", MatchDecoderTests[Int \/ Boolean].decoder[Int, Int])

}
