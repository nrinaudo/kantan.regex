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

package kantan.regex.laws

import kantan.codecs.laws.discipline.DisciplinePackage
import kantan.regex.DecodeError
import kantan.regex.Match
import kantan.regex.codecs

package object discipline extends DisciplinePackage {

  type GroupDecoderTests[A] = DecoderTests[Option[String], A, DecodeError, codecs.type]
  type MatchDecoderTests[A] = DecoderTests[Match, A, DecodeError, codecs.type]

}
