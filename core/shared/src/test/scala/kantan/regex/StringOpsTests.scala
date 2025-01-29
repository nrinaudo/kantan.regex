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

import kantan.codecs.laws.IllegalString
import kantan.codecs.laws.LegalString
import kantan.codecs.laws.discipline.arbitrary._
import kantan.regex.implicits._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class StringOpsTests extends AnyFunSuite with ScalaCheckPropertyChecks with Matchers {

  test("evalRegex should succeed for valid regular expressions") {

    forAll { value: LegalString[Int] =>
      value.encoded.evalRegex[Int](rx"-?\d+").toList should be(List(DecodeResult.success(value.decoded)))
      value.encoded.evalRegex[Int](rx"-?\d+", 0).toList should be(List(DecodeResult.success(value.decoded)))
      value.encoded.evalRegex("-?\\d+".asUnsafeRegex[Int]).toList should be(List(DecodeResult.success(value.decoded)))
    }

    forAll { value: IllegalString[Int] =>
      every(value.encoded.evalRegex[Int](rx"-?\d+").toList) should matchPattern { case Left(_) => }
      every(value.encoded.evalRegex[Int](rx"-?\d+", 0).toList) should matchPattern { case Left(_) => }
      every(value.encoded.evalRegex("-?\\d+".asUnsafeRegex[Int]).toList) should matchPattern { case Left(_) => }
    }

  }

  test("unsafeEvalRegex should succeed for valid regular expressions and valid matches") {
    forAll { value: LegalString[Int] =>
      value.encoded.unsafeEvalRegex[Int](rx"-?\d+").toList should be(List(value.decoded))
      value.encoded.unsafeEvalRegex[Int](rx"-?\d+", 0).toList should be(List(value.decoded))
    }
  }

}
