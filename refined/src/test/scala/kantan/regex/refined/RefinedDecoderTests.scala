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

package kantan.regex.refined

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import kantan.codecs.laws.discipline.SerializableTests
import kantan.regex.{GroupDecoder, MatchDecoder}
import kantan.regex.laws.discipline.{GroupDecoderTests, MatchDecoderTests}
import kantan.regex.refined.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class RefinedDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("GroupDecoder[Int Refined Positive]", GroupDecoderTests[Int Refined Positive].decoder[Int, Int])
  checkAll("GroupDecoder[Int Refined Positive]", SerializableTests[GroupDecoder[Int Refined Positive]].serializable)

  checkAll("MatchDecoder[Int Refined Positive]", MatchDecoderTests[Int Refined Positive].decoder[Int, Int])
  checkAll("MatchDecoder[Int Refined Positive]", SerializableTests[MatchDecoder[Int Refined Positive]].serializable)
}
