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

import kantan.codecs.Result
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class DecodeResultTests extends FunSuite with GeneratorDrivenPropertyChecks {
  test("DecodeResult.success should return a success") {
    forAll { i: Int ⇒ assert(DecodeResult.success(i) == Result.Success(i))}
  }

  test("DecodeResult.apply should return a success on 'good' values") {
    forAll { i: Int ⇒ assert(DecodeResult(i) == Result.Success(i))}
  }

  test("DecodeResult.apply should return a failure on 'bad' values") {
    forAll { e: Exception ⇒ assert(DecodeResult(throw e) == Result.Failure(DecodeError.TypeError(e)))}
  }

  test("DecodeResult.noSuchGroupId should return a failure ") {
    forAll { i: Int ⇒ assert(DecodeResult.noSuchGroupId(i) == Result.Failure(DecodeError.NoSuchGroupId(i)))}
  }

  test("DecodeResult.noSuchGroupName should return a failure ") {
    forAll { s: String ⇒ assert(DecodeResult.noSuchGroupName(s) == Result.Failure(DecodeError.NoSuchGroupName(s)))}
  }
}