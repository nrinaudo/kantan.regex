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

import kantan.codecs.strings.StringDecoder

object codecs extends TupleDecoders {
  implicit def fromGroup[A](implicit da: GroupDecoder[A]): MatchDecoder[A] = MatchDecoder.fromGroup(0)

  implicit def fromString[A](implicit da: StringDecoder[A]): GroupDecoder[A] =
    da.tag[codecs.type].mapError(DecodeError.TypeError.apply)
}
