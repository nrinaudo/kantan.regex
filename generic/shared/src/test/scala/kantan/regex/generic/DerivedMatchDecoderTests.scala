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
package generic

import generic.arbitrary._
import implicits._
import kantan.codecs.shapeless.laws._
import laws._
import laws.discipline._
import org.scalacheck.Arbitrary

object Instances {
  case class Simple(i: Int)
  case class Complex(i: Int, b: Boolean, c: Option[Byte])

  def toMatch(p: Pattern, is: String*): Match = {
    val matcher = p.matcher(is.mkString(" "))
    matcher.find()
    new Match(matcher)
  }

  implicit val arbLegal: Arbitrary[LegalMatch[Or[Complex, Simple]]] =
    arbLegalValue((o: Or[Complex, Simple]) =>
      o match {
        case Left(Complex(i, b, c)) =>
          toMatch(rx"(-?\d+) (true|false) (-?\d+)?", i.toString, b.toString, c.fold("")(_.toString))

        case Right(Simple(i)) => toMatch(rx"(-?\d+)", i.toString)

      }
    )
}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
class DerivedMatchDecoderTests extends DisciplineSuite {

  import Instances._

  checkAll("MatchDecoder[Complex Or Simple]", MatchDecoderTests[Complex Or Simple].decoder[Byte, Float])

}
