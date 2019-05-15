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
package enumeratum.values

import kantan.codecs.enumeratum.laws.discipline.{SerializableTests => _, _}
import laws.discipline._

class SerialisationTests extends DisciplineSuite {

  checkAll("GroupDecoder[EnumeratedByte]", SerializableTests[GroupDecoder[EnumeratedByte]].serializable)
  checkAll("MatchDecoder[EnumeratedByte]", SerializableTests[MatchDecoder[EnumeratedByte]].serializable)

  checkAll("GroupDecoder[EnumeratedChar]", SerializableTests[GroupDecoder[EnumeratedChar]].serializable)
  checkAll("MatchDecoder[EnumeratedChar]", SerializableTests[MatchDecoder[EnumeratedChar]].serializable)

  checkAll("GroupDecoder[EnumeratedInt]", SerializableTests[GroupDecoder[EnumeratedInt]].serializable)
  checkAll("MatchDecoder[EnumeratedInt]", SerializableTests[MatchDecoder[EnumeratedInt]].serializable)

  checkAll("GroupDecoder[EnumeratedLong]", SerializableTests[GroupDecoder[EnumeratedLong]].serializable)
  checkAll("MatchDecoder[EnumeratedLong]", SerializableTests[MatchDecoder[EnumeratedLong]].serializable)

  checkAll("GroupDecoder[EnumeratedShort]", SerializableTests[GroupDecoder[EnumeratedShort]].serializable)
  checkAll("MatchDecoder[EnumeratedShort]", SerializableTests[MatchDecoder[EnumeratedShort]].serializable)

  checkAll("GroupDecoder[EnumeratedString]", SerializableTests[GroupDecoder[EnumeratedString]].serializable)
  checkAll("MatchDecoder[EnumeratedString]", SerializableTests[MatchDecoder[EnumeratedString]].serializable)

}
