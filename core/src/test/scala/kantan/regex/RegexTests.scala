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

import kantan.regex.implicits._
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class RegexTests extends FunSuite with GeneratorDrivenPropertyChecks with Matchers {
  test("All matches should be decoded as expected.") {
    forAll { is: List[Int] ⇒
      val regex = ("-?\\d+").asUnsafeRegex[Int].map(_.get)
      regex.eval(is.mkString(" ")).toList should be(is)
    }
  }

  test("Invalid regular expressions should not compile") {
    "[".asRegex[Int].isFailure should be(true)
  }

  test("Regexes obtained from a pattern should have that pattern as a toString") {
    val pattern = rx"-?\d+"
    Regex[Int](pattern).toString should be(pattern.toString)
  }
}
