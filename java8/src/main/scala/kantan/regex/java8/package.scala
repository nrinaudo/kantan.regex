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

import java.time._
import kantan.codecs.export.Exported
import kantan.codecs.strings.StringDecoder
import kantan.codecs.strings.java8.{TimeDecoderCompanion, ToFormatLiteral}

/** Declares [[kantan.regex.GroupDecoder]] instances for java8 date and time types.
  *
  * Note that the type for default codecs might come as a surprise: the wrapping `Exported` is used to lower their
  * priority. This is necessary because the standard use case will be to `import kantan.regex.java8._`, which
  * brings both the instance creation and default instances in scope. Without this type trickery, custom instances
  * and default ones would always clash.
  */
package object java8 extends TimeDecoderCompanion[Option[String], DecodeError, codecs.type] with ToFormatLiteral {

  override def decoderFrom[D](d: StringDecoder[D]) = codecs.fromString(d)

  implicit val defaultInstantGroupDecoder: Exported[GroupDecoder[Instant]] =
    Exported(defaultInstantDecoder)
  implicit val defaultZonedDateTimeGroupDecoder: Exported[GroupDecoder[ZonedDateTime]] =
    Exported(defaultZonedDateTimeDecoder)
  implicit val defaultOffsetDateTimeGroupDecoder: Exported[GroupDecoder[OffsetDateTime]] =
    Exported(defaultOffsetDateTimeDecoder)
  implicit val defaultLocalDateTimeGroupDecoder: Exported[GroupDecoder[LocalDateTime]] =
    Exported(defaultLocalDateTimeDecoder)
  implicit val defaultLocalDateGroupDecoder: Exported[GroupDecoder[LocalDate]] =
    Exported(defaultLocalDateDecoder)
  implicit val defaultLocalTimeGroupDecoder: Exported[GroupDecoder[LocalTime]] =
    Exported(defaultLocalTimeDecoder)

}
