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

package kantan.regex.java8

import kantan.regex.GroupDecoder
import kantan.regex.MatchDecoder
import kantan.regex.java8.arbitrary._
import kantan.regex.laws.discipline.DisciplineSuite
import kantan.regex.laws.discipline.GroupDecoderTests
import kantan.regex.laws.discipline.MatchDecoderTests
import kantan.regex.laws.discipline.SerializableTests

import java.time.LocalDateTime

class LocalDateTimeDecoderTests extends DisciplineSuite {

  checkAll("GroupDecoder[LocalDateTime]", GroupDecoderTests[LocalDateTime].decoder[Int, Int])
  checkAll("GroupDecoder[LocalDateTime]", SerializableTests[GroupDecoder[LocalDateTime]].serializable)

  checkAll("MatchDecoder[LocalDateTime]", MatchDecoderTests[LocalDateTime].decoder[Int, Int])
  checkAll("MatchDecoder[LocalDateTime]", SerializableTests[MatchDecoder[LocalDateTime]].serializable)

}
