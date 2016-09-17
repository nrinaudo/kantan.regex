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

/** Provides instance creation and summoning methods for [[GroupDecoder]]. */
object GroupDecoder {
  /** Summons an implicit instance of [[[GroupDecoder GroupDecoder[A]]] if one can be found.
    *
    * This is a convenience method and equivalent to calling `implicitly[GroupDecoder[A]]`
    */
  def apply[A](implicit ev: GroupDecoder[A]): GroupDecoder[A] = macro imp.summon[GroupDecoder[A]]

  /** Creates a new instance of [[GroupDecoder]] from the specified function.
    *
    * Before using this method, consider summoning an existing instance of [[GroupDecoder]] and adapting that. If you
    * want a [[[GroupDecoder GroupDecoder[B]]]], already have a [[[GroupDecoder GroupDecoder[A]]]] and can write a
    * function `f` of type `A ⇒ B`, then it's probably better to write:
    * {{{
    *   GroupDecoder[A].map(f)
    * }}}
    */
  def from[A](f: Option[String] ⇒ DecodeResult[A]): GroupDecoder[A] = Decoder.from(f)

  @deprecated("use from instead (see https://github.com/nrinaudo/kantan.regex/issues/8)", "0.1.3")
  def apply[A](f: Option[String] ⇒ DecodeResult[A]): GroupDecoder[A] = GroupDecoder.from(f)
}

/** Declares all default [[GroupDecoder]] instances. */
trait GroupDecoderInstances {
  /** Turns a `StringDecoder` instance into a [[GroupDecoder]] one.
    *
    * This provides free support for all primitive types (as well as a few convenience ones, such as `java.io.File`).
    */
  implicit def fromString[A: StringDecoder]: GroupDecoder[A] =
    GroupDecoder.from(_.map(StringDecoder[A]
      .mapError { error ⇒ DecodeError.TypeError(error.getMessage, error.getCause)}.decode)
      .getOrElse(DecodeResult.emptyGroup))

  /** Turns a [[GroupDecoder GroupDecoder[A]]] into a [[GroupDecoder GroupDecoder[Option[A]]]].
    *
    * This means that, provided you know how to decode an `A`, you will always have free support for `Option[A]`.
    */
  implicit def optGroupDecoder[A: GroupDecoder]: GroupDecoder[Option[A]] =
    Decoder.optionalDecoder

  /** Turns a [[GroupDecoder GroupDecoder[A]]] and [[GroupDecoder GroupDecoder[B]]] into a
    * [[GroupDecoder GroupDecoder[Either[A, B]]]].
    *
    * This means that, provided you know how to decode an `A` and a `B`, you will always have free support for
    * `Either[A, B]`.
    */
  implicit def eitherGroupDecoder[A: GroupDecoder, B: GroupDecoder]: GroupDecoder[Either[A, B]] =
    Decoder.eitherDecoder
}
