/*
 * Copyright 2017 Nicolas Rinaudo
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

import kantan.codecs.laws._
import kantan.regex.implicits._
import kantan.regex.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class StringOpsTests extends FunSuite with GeneratorDrivenPropertyChecks {
  test("evalRegex should succeed for valid regular expressions") {
    forAll { value: LegalString[Int] ⇒
      assert(value.encoded.evalRegex[Int](rx"-?\d+").toList == List(DecodeResult.success(value.decoded)))
      assert(value.encoded.evalRegex[Int](rx"-?\d+", 0).toList == List(DecodeResult.success(value.decoded)))
      assert(value.encoded.evalRegex("-?\\d+".asUnsafeRegex[Int]).toList == List(DecodeResult.success(value.decoded)))
    }

    forAll { value: IllegalString[Int] ⇒
      assert(value.encoded.evalRegex[Int](rx"-?\d+").forall(_.isFailure))
      assert(value.encoded.evalRegex[Int](rx"-?\d+", 0).forall(_.isFailure))
      assert(value.encoded.evalRegex("-?\\d+".asUnsafeRegex[Int]).forall(_.isFailure))
    }
  }

  test("unsafeEvalRegex should succeed for valid regular expressions and valid matches") {
    forAll { value: LegalString[Int] ⇒
      assert(value.encoded.unsafeEvalRegex[Int](rx"-?\d+").toList == List(value.decoded))
      assert(value.encoded.unsafeEvalRegex[Int](rx"-?\d+", 0).toList == List(value.decoded))
    }
  }
}
