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
import scala.annotation.tailrec
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

object codecs extends TupleDecoders {
  implicit def fromGroup[A](implicit da: GroupDecoder[A]): MatchDecoder[A] = MatchDecoder.fromGroup(0)

  implicit def fromString[A](implicit da: StringDecoder[A]): GroupDecoder[A] =
    da.tag[codecs.type].mapError(DecodeError.TypeError.apply)

  implicit def fromCbf[F[_], A](implicit da: GroupDecoder[A], cbf: CanBuildFrom[Nothing, A, F[A]]): MatchDecoder[F[A]] =
    MatchDecoder[F[A]] { (m: Match) ⇒
      @tailrec
      def loop(i: Int, curr: DecodeResult[mutable.Builder[A, F[A]]]): DecodeResult[F[A]] =
        if(i > m.length || !curr.isSuccess) curr.map(_.result())
        else loop(i + 1, for {
          fa ← curr
          a  ← m.decode[A](i)
        } yield {
          fa += a
          fa
        })

      loop(1, DecodeResult.success(cbf()))
    }
}
