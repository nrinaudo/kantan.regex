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
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class CompilableOpsTests extends FunSuite with GeneratorDrivenPropertyChecks with Matchers {
  test("asRegex should succeed for valid regular expressions") {
    "\\d+".asRegex[Int] should be a 'right
    "\\d+".asRegex[Int](1) should be a 'right

    rx"\\d+".asRegex[Int] should be a 'right
    rx"\\d+".asRegex[Int](1) should be a 'right

    "\\d+".r.asRegex[Int] should be a 'right
    "\\d+".r.asRegex[Int](1) should be a 'right
  }

  test("asRegex should fail for invalid regular expressions") {
    "[".asRegex[Int] should be a 'left
    "[".asRegex[Int](1) should be a 'left
  }

  test("asUnsafeRegex should succeed for valid regular expressions") {
    "\\d+".asUnsafeRegex[Int]
    "\\d+".asUnsafeRegex[Int](1)

    rx"\\d+".asUnsafeRegex[Int]
    rx"\\d+".asUnsafeRegex[Int](1)

    "\\d+".r.asUnsafeRegex[Int]
    "\\d+".r.asUnsafeRegex[Int](1)
    ()
  }

  test("asUnsafeRegex should fail for invalid regular expressions") {
    intercept[Exception]("[".asUnsafeRegex[Int])
    intercept[Exception]("[".asUnsafeRegex[Int](1))
    ()
  }
}
