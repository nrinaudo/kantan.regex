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

import java.io.File
import java.net.{URI, URL}
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.{Date, Locale, UUID}
import kantan.codecs.strings.StringCodec
import kantan.regex.laws.discipline.{DisciplineSuite, SerializableTests}

class SerialisationTests extends DisciplineSuite {

  checkAll("GroupDecoder[BigDecimal]", SerializableTests[GroupDecoder[BigDecimal]].serializable)
  checkAll("MatchDecoder[BigDecimal]", SerializableTests[MatchDecoder[BigDecimal]].serializable)

  checkAll("GroupDecoder[BigInt]", SerializableTests[GroupDecoder[BigInt]].serializable)
  checkAll("MatchDecoder[BigInt]", SerializableTests[MatchDecoder[BigInt]].serializable)

  checkAll("GroupDecoder[Boolean]", SerializableTests[GroupDecoder[Boolean]].serializable)
  checkAll("MatchDecoder[Boolean]", SerializableTests[MatchDecoder[Boolean]].serializable)

  checkAll("GroupDecoder[Byte]", SerializableTests[GroupDecoder[Byte]].serializable)
  checkAll("MatchDecoder[Byte]", SerializableTests[MatchDecoder[Byte]].serializable)

  checkAll("GroupDecoder[Char]", SerializableTests[GroupDecoder[Char]].serializable)
  checkAll("MatchDecoder[Char]", SerializableTests[MatchDecoder[Char]].serializable)

  implicit val codec: StringCodec[Date] =
    StringCodec.dateCodec(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH))

  checkAll("GroupDecoder[Date]", SerializableTests[GroupDecoder[Date]].serializable)
  checkAll("MatchDecoder[Date]", SerializableTests[MatchDecoder[Date]].serializable)

  checkAll("GroupDecoder[Double]", SerializableTests[GroupDecoder[Double]].serializable)
  checkAll("MatchDecoder[Double]", SerializableTests[MatchDecoder[Double]].serializable)

  checkAll("GroupDecoder[Either[Int, Boolean]]", SerializableTests[GroupDecoder[Either[Int, Boolean]]].serializable)
  checkAll("MatchDecoder[Either[Int, Boolean]]", SerializableTests[MatchDecoder[Either[Int, Boolean]]].serializable)

  checkAll("GroupDecoder[File]", SerializableTests[GroupDecoder[File]].serializable)
  checkAll("MatchDecoder[File]", SerializableTests[MatchDecoder[File]].serializable)

  checkAll("GroupDecoder[Float]", SerializableTests[GroupDecoder[Float]].serializable)
  checkAll("MatchDecoder[Float]", SerializableTests[MatchDecoder[Float]].serializable)

  checkAll("GroupDecoder[Int]", SerializableTests[GroupDecoder[Int]].serializable)
  checkAll("MatchDecoder[Int]", SerializableTests[MatchDecoder[Int]].serializable)

  checkAll("GroupDecoder[Long]", SerializableTests[GroupDecoder[Long]].serializable)
  checkAll("MatchDecoder[Long]", SerializableTests[MatchDecoder[Long]].serializable)

  checkAll("GroupDecoder[Option[Int]]", SerializableTests[GroupDecoder[Option[Int]]].serializable)
  checkAll("MatchDecoder[Option[Int]]", SerializableTests[MatchDecoder[Option[Int]]].serializable)

  checkAll("GroupDecoder[Path]", SerializableTests[GroupDecoder[Path]].serializable)
  checkAll("MatchDecoder[Path]", SerializableTests[MatchDecoder[Path]].serializable)

  checkAll("GroupDecoder[Short]", SerializableTests[GroupDecoder[Short]].serializable)
  checkAll("MatchDecoder[Short]", SerializableTests[MatchDecoder[Short]].serializable)

  checkAll("GroupDecoder[String]", SerializableTests[GroupDecoder[String]].serializable)
  checkAll("MatchDecoder[String]", SerializableTests[MatchDecoder[String]].serializable)

  checkAll("GroupDecoder[URI]", SerializableTests[GroupDecoder[URI]].serializable)
  checkAll("MatchDecoder[URI]", SerializableTests[MatchDecoder[URI]].serializable)

  checkAll("GroupDecoder[URL]", SerializableTests[GroupDecoder[URL]].serializable)
  checkAll("MatchDecoder[URL]", SerializableTests[MatchDecoder[URL]].serializable)

  checkAll("GroupDecoder[UUID]", SerializableTests[GroupDecoder[UUID]].serializable)
  checkAll("MatchDecoder[UUID]", SerializableTests[MatchDecoder[UUID]].serializable)

}
