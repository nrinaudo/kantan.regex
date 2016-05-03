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

import kantan.regex.ops._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class MatchTests extends FunSuite with GeneratorDrivenPropertyChecks {
  test("length should return the expected value") {
    def validating(length: Int): MatchDecoder[List[Int]] = MatchDecoder[List[Int]].contramapEncoded { (m: Match) ⇒
      assert(m.length == length)
      m
    }

    forAll(Gen.nonEmptyListOf(Arbitrary.arbitrary[Int])) { (is: List[Int]) ⇒
      implicit val decoder = validating(is.length)

      val regex = is.map(_ ⇒ "(-?\\d+)").mkString(" ").asUnsafeRegex[List[Int]].map(_.get)
      assert(regex.eval(is.mkString(" ")).next == is)
    }
  }

  test("Out of bound groups should generate a NoSuchGroupId") {
    def outOfBounds(i: Int): MatchDecoder[Int] = MatchDecoder[Int] { (m: Match) ⇒
      m.decode[Int](i)
    }

    forAll(Gen.nonEmptyListOf(Arbitrary.arbitrary[Int]), Arbitrary.arbitrary[Int].suchThat(_ != -1)) { (is, offset) ⇒
      val index = is.length + 1 + offset

      implicit val decoder = outOfBounds(index)
      val regex = is.map(_ ⇒ "(-?\\d+)").mkString(" ").asUnsafeRegex[Int]
      assert(regex.eval(is.mkString(" ")).next == DecodeResult.noSuchGroupId(index))
    }
  }

  test("Non-existing named groups should generate a NoSuchGroupName") {
    def noSuchName(id: String): MatchDecoder[Int] = MatchDecoder[Int] { (m: Match) ⇒
      m.decode[Int](id)
    }

    forAll(Gen.nonEmptyListOf(Arbitrary.arbitrary[Int]), Gen.identifier) { (is, id) ⇒
      implicit val decoder = noSuchName(id)
      val regex = is.map(_ ⇒ "(-?\\d+)").mkString(" ").asUnsafeRegex[Int]
      assert(regex.eval(is.mkString(" ")).next == DecodeResult.noSuchGroupName(id))
    }
  }
}
