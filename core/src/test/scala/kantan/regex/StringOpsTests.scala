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
import kantan.codecs.laws._
import laws.discipline.arbitrary._
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class StringOpsTests extends FunSuite with GeneratorDrivenPropertyChecks with Matchers {
  test("evalRegex should succeed for valid regular expressions") {
    forAll { value: LegalString[Int] ⇒
      value.encoded.evalRegex[Int](rx"-?\d+").toList should be(List(DecodeResult.success(value.decoded)))
      value.encoded.evalRegex[Int](rx"-?\d+", 0).toList should be(List(DecodeResult.success(value.decoded)))
      value.encoded.evalRegex("-?\\d+".asUnsafeRegex[Int]).toList should be(List(DecodeResult.success(value.decoded)))
    }

    forAll { value: IllegalString[Int] ⇒
      every(value.encoded.evalRegex[Int](rx"-?\d+").toList) should be a 'failure
      every(value.encoded.evalRegex[Int](rx"-?\d+", 0).toList) should be a 'failure
      every(value.encoded.evalRegex("-?\\d+".asUnsafeRegex[Int]).toList) should be a 'failure
    }
  }

  test("unsafeEvalRegex should succeed for valid regular expressions and valid matches") {
    forAll { value: LegalString[Int] ⇒
      value.encoded.unsafeEvalRegex[Int](rx"-?\d+").toList should be(List(value.decoded))
      value.encoded.unsafeEvalRegex[Int](rx"-?\d+", 0).toList should be(List(value.decoded))
    }
  }
}
