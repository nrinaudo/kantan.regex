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

package kantan.regex.libra

import kantan.regex.GroupDecoder
import kantan.regex.MatchDecoder
import kantan.regex.laws.discipline.DisciplineSuite
import kantan.regex.laws.discipline.GroupDecoderTests
import kantan.regex.laws.discipline.MatchDecoderTests
import kantan.regex.laws.discipline.SerializableTests
import kantan.regex.libra.arbitrary._
import libra.Quantity
import shapeless.HNil

class LibraCodecTests extends DisciplineSuite {

  checkAll("GroupDecoder[Quantity[Double, HNil]]", GroupDecoderTests[Quantity[Double, HNil]].decoder[String, Float])
  checkAll("GroupDecoder[Quantity[Double, HNil]]", SerializableTests[GroupDecoder[Quantity[Double, HNil]]].serializable)

  checkAll("MatchDecoder[Quantity[Double, HNil]]", MatchDecoderTests[Quantity[Double, HNil]].decoder[String, Float])
  checkAll("MatchDecoder[Quantity[Double, HNil]]", SerializableTests[MatchDecoder[Quantity[Double, HNil]]].serializable)

  checkAll("GroupDecoder[Quantity[Int, HNil]]", GroupDecoderTests[Quantity[Int, HNil]].decoder[String, Float])
  checkAll("GroupDecoder[Quantity[Int, HNil]]", SerializableTests[GroupDecoder[Quantity[Int, HNil]]].serializable)

  checkAll("MatchDecoder[Quantity[Int, HNil]]", MatchDecoderTests[Quantity[Int, HNil]].decoder[String, Float])
  checkAll("MatchDecoder[Quantity[Int, HNil]]", SerializableTests[MatchDecoder[Quantity[Int, HNil]]].serializable)

}
