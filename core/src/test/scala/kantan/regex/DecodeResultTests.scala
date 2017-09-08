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

import kantan.codecs.scalatest.ResultValues
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

@SuppressWarnings(Array("org.wartremover.warts.Throw"))
class DecodeResultTests extends FunSuite with GeneratorDrivenPropertyChecks with Matchers with ResultValues {
  test("DecodeResult.success should return a success") {
    forAll { i: Int ⇒
      DecodeResult.success(i).success.value should be(i)
    }
  }

  test("DecodeResult.apply should return a success on 'good' values") {
    forAll { i: Int ⇒
      DecodeResult(i).success.value should be(i)
    }
  }

  test("DecodeResult.apply should return a failure on 'bad' values") {
    forAll { e: Exception ⇒
      DecodeResult(throw e).failure.value should be(DecodeError.TypeError(e))
    }
  }

  test("DecodeResult.typeError should return a failure") {
    forAll { e: Exception ⇒
      DecodeResult.typeError(e).failure.value should be(DecodeError.TypeError(e))

      DecodeResult.typeError(e.getMessage) match {
        case Failure(DecodeError.TypeError(m)) ⇒ m should be(e.getMessage)
        case a                                 ⇒ fail(s"$a was not a type error")
      }
    }
  }

  test("DecodeResult.noSuchGroupId should return a failure ") {
    forAll { i: Int ⇒
      DecodeResult.noSuchGroupId(i).failure.value should be(DecodeError.NoSuchGroupId(i))
    }
  }
}
