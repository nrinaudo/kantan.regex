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

import kantan.codecs.Decoder

object MatchDecoder extends GeneratedMatchDecoders {
  def apply[A](implicit da: MatchDecoder[A]): MatchDecoder[A] = da

  def apply[A](f: Match ⇒ DecodeResult[A]): MatchDecoder[A] =
    Decoder[Match, A, DecodeError, codecs.type](f)

  def fromGroup[A](index: Int)(implicit da: GroupDecoder[A]): MatchDecoder[A] = MatchDecoder { m ⇒
    m.group(index).flatMap(da.decode)
  }

  def fromGroup[A](name: String)(implicit da: GroupDecoder[A]): MatchDecoder[A] = MatchDecoder { m ⇒
    m.group(name).flatMap(da.decode)
  }
}
