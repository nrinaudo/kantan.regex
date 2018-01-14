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
package java8

import arbitrary._
import java.time.Instant
import laws.discipline._

class InstantDecoderTests extends DisciplineSuite {

  checkAll("GroupDecoder[Instant]", GroupDecoderTests[Instant].decoder[Int, Int])
  checkAll("GroupDecoder[Instant]", SerializableTests[GroupDecoder[Instant]].serializable)

  checkAll("MatchDecoder[Instant]", MatchDecoderTests[Instant].decoder[Int, Int])
  checkAll("MatchDecoder[Instant]", SerializableTests[MatchDecoder[Instant]].serializable)

}
