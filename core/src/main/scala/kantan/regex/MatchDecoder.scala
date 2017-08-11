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
import kantan.codecs.DecoderCompanion
import kantan.codecs.collection.HasBuilder
import scala.annotation.tailrec
import scala.collection.mutable

/** Provides useful methods for [[MatchDecoder]] instance summoning and creation.
  *
  * If trying to create a [[MatchDecoder]] from a type for which you already have a [[GroupDecoder]], use
  * [[MatchDecoder$.fromGroup[A](index:Int)*]].
  *
  * Otherwise, the `ordered` or `decoder` methods make it simple to create [[MatchDecoder]] instances for more
  * complicated types.
  */
object MatchDecoder extends GeneratedMatchDecoders with DecoderCompanion[Match, DecodeError, codecs.type] {

  /** Creates a new [[MatchDecoder]] for a type that already has a [[GroupDecoder]].
    *
    * @param index index (from 1) of the group that should be extracted.
    */
  def fromGroup[A: GroupDecoder](index: Int): MatchDecoder[A] = from(_.decode(index))
}

/** Declares default [[MatchDecoder]] instances. */
trait MatchDecoderInstances {

  /** Turns a [[GroupDecoder]] into a [[MatchDecoder]] by having it look at the entire match rather than a specific
    * group. */
  implicit def fromGroup[A: GroupDecoder]: MatchDecoder[A] = MatchDecoder.fromGroup(0)

  /** Provides an instance of [[MatchDecoder]] for `Either[A, B]`, provided both `A` and `B` have a [[MatchDecoder]]. */
  implicit def eitherMatch[A: MatchDecoder, B: MatchDecoder]: MatchDecoder[Either[A, B]] =
    Decoder.eitherDecoder

  /** Provides an instance of [[MatchDecoder]] for `Option[A]`, provided `A` has a [[MatchDecoder]]. */
  implicit def optMatch[A](implicit da: GroupDecoder[Option[A]]): MatchDecoder[Option[A]] =
    MatchDecoder.fromGroup[Option[A]](0)(GroupDecoder.from { os ⇒
      da.decode(os.filter(_.nonEmpty))
    })

  // TODO: there *must* be a more elegant way to write this.
  // This is more of a hack and not terribly satisfactory, since regular expressions are much more limitted than I
  // initially assumed: matching "12345" against "(\d)*" will not result in 5 groups, but 2: "12345" and "5".
  implicit def fromCbf[F[_], A](implicit da: GroupDecoder[Option[A]], cbf: HasBuilder[F, A]): MatchDecoder[F[A]] =
    MatchDecoder.from { m ⇒
      @tailrec
      def loop(i: Int, curr: DecodeResult[mutable.Builder[A, F[A]]]): DecodeResult[F[A]] =
        if(i > m.length || !curr.isSuccess) curr.map(_.result())
        else
          loop(i + 1, for {
            fa ← curr
            a  ← m.decode[Option[A]](i)
          } yield {
            a.foreach(fa += _)
            fa
          })

      loop(1, DecodeResult.success(cbf.newBuilder))
    }
}
