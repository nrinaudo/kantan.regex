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
class CompileResultTests extends FunSuite with GeneratorDrivenPropertyChecks with Matchers with ResultValues {
  test("CompileResult.success should return a success") {
    forAll { i: Int ⇒
      CompileResult.success(i).success.value should be(i)
    }
  }

  test("CompileResult.apply should return a success on 'good' values") {
    forAll { i: Int ⇒
      CompileResult(i).success.value should be(i)
    }
  }

  test("CompileResult.apply should return a failure on 'bad' values") {
    forAll { e: Exception ⇒
      CompileResult(throw e).failure.value should be(CompileError(e))
    }
  }
}
