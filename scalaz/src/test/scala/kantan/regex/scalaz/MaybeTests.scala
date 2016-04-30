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

package kantan.regex.scalaz

import kantan.regex.laws.discipline._
import kantan.regex.scalaz.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline
import scalaz.Maybe

class MaybeTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val legalMaybeGroup = arbLegalGroup[Maybe[Int]]
  implicit val illegalMaybeGroup = arbIllegalGroup[Maybe[Int]]

  implicit val legalMaybeMatch = arbLegalMatch[Maybe[Int]]
  implicit val illegalMaybeMatch = arbIllegalMatch[Maybe[Int]]

  checkAll("GroupDecoder[Maybe[Int]]", GroupDecoderTests[Maybe[Int]].decoder[Int, Int])
  checkAll("MatchDecoder[Maybe[Int]]", MatchDecoderTests[Maybe[Int]].decoder[Int, Int])
}
