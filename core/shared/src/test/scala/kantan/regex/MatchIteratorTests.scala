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

import implicits._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

@SuppressWarnings(Array("org.wartremover.warts.While"))
class MatchIteratorTests extends AnyFunSuite with ScalaCheckPropertyChecks with Matchers {
  def toMatchIterator(is: List[Int]): Iterator[Int] =
    Regex[Int](rx"-?\d+").eval(is.mkString(" ")).map(_.fold(e => throw e, identity))

  test("MatchIterator should fail when reading more than the maximum number of elements") {
    forAll { is: List[Int] =>
      val it = toMatchIterator(is)
      while(it.hasNext) it.next()

      intercept[NoSuchElementException](it.next())
      ()
    }
  }

  test("MatchIterator should contain the expected number of elements") {
    forAll { is: List[Int] =>
      val it = toMatchIterator(is)
      is.indices.foreach(_ => it.next())
      it.hasNext should be(false)
    }
  }
}
