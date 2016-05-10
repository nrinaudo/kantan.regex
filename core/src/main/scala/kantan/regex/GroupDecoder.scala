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
import kantan.codecs.strings._

object GroupDecoder {
  def apply[A](implicit da: GroupDecoder[A]): GroupDecoder[A] = da
  def apply[A](f: Option[String] ⇒ DecodeResult[A]): GroupDecoder[A] = new GroupDecoder[A] {
    override def decode(e: Option[String]) = f(e)
  }
}

trait GroupDecoderInstances {
  implicit def fromString[A](implicit da: StringDecoder[A]): GroupDecoder[A] =
      GroupDecoder(_.map(da.mapError(DecodeError.TypeError.apply).decode)
        .getOrElse(DecodeResult.emptyGroup))


  implicit def optGroupDecoder[A](implicit da: GroupDecoder[A]): GroupDecoder[Option[A]] =
    Decoder.optionalDecoder

  implicit def eitherGroupDecoder[A: GroupDecoder, B: GroupDecoder]: GroupDecoder[Either[A, B]] =
    Decoder.eitherDecoder
}
