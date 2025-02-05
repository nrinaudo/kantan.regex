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

import java.text.SimpleDateFormat
import java.util.{Date, Locale}
import kantan.codecs.strings.StringCodec
import kantan.regex.laws.discipline.{DisciplineSuite, GroupDecoderTests, MatchDecoderTests}
import kantan.regex.laws.discipline.arbitrary._

class DateDecoderTests extends DisciplineSuite {

  implicit val codec: StringCodec[Date] =
    StringCodec.dateCodec(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH))

  checkAll("GroupDecoder[Date]", GroupDecoderTests[Date].decoder[Int, Int])
  checkAll("MatchDecoder[Date]", MatchDecoderTests[Date].decoder[Int, Int])

}
