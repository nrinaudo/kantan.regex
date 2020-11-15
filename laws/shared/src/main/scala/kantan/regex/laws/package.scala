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

import kantan.codecs.laws.{CodecValue, DecoderLaws}
import kantan.codecs.laws.CodecValue.{IllegalValue, LegalValue}

package object laws {
  type GroupDecoderLaws[A] = DecoderLaws[Option[String], A, DecodeError, codecs.type]
  type MatchDecoderLaws[A] = DecoderLaws[Match, A, DecodeError, codecs.type]

  type LegalGroup[A]   = LegalValue[Option[String], A, codecs.type]
  type IllegalGroup[A] = IllegalValue[Option[String], A, codecs.type]
  type GroupValue[A]   = CodecValue[Option[String], A, codecs.type]

  type LegalMatch[A]   = LegalValue[Match, A, codecs.type]
  type IllegalMatch[A] = IllegalValue[Match, A, codecs.type]
  type MatchValue[A]   = CodecValue[Match, A, codecs.type]

}
