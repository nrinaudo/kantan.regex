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

package kantan.regex.generic

import java.util.regex.Pattern
import kantan.codecs.shapeless.laws._
import kantan.codecs.shapeless.laws.discipline.arbitrary._
import kantan.regex.Match
import kantan.regex.all._
import kantan.regex.laws.{IllegalMatch, LegalMatch}
import kantan.regex.laws.discipline.MatchDecoderTests
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class DerivedMatchDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  def toMatch(p: Pattern, is: String*): Match = {
    val matcher = p.matcher(is.mkString(" "))
    matcher.find()
    new Match(matcher)
  }

  case class Simple(i: Int)
  case class Complex(i: Int, b: Boolean, c: Option[Byte])

  implicit val arbLegal: Arbitrary[LegalMatch[Or[Complex, Simple]]] = arbLegalValue((o: Or[Complex, Simple]) ⇒ o match {
    case Left(Complex(i, b, c)) ⇒
      toMatch(rx"(-?\d+) (true|false) (-?\d+)?", i.toString, b.toString, c.fold("")(_.toString))

    case Right(Simple(i)) ⇒ toMatch(rx"(-?\d+)", i.toString)

  })

  implicit val arbIllegal: Arbitrary[IllegalMatch[Or[Complex, Simple]]] = arbIllegalValue { (m: Match) ⇒
    if(m.length >= 3) {
      m.decode[Int](1).isFailure ||
      m.decode[Boolean](2).isFailure ||
      m.decode[Byte](3).isFailure
    }
    else if(m.length >= 1) m.decode[Int](1).isFailure
    else true
  }(Arbitrary(implicitly[Arbitrary[String]].arbitrary.map(s ⇒ toMatch(rx".*", s))))

  checkAll("MatchDecoder[Or[Complex, Simple]]", MatchDecoderTests[Or[Complex, Simple]].decoder[Byte, Float])
}
