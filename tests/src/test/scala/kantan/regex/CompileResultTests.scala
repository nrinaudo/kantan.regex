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

import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class CompileResultTests extends FunSuite with GeneratorDrivenPropertyChecks {
  test("CompileResult.success should return a success") {
    forAll { i: Int ⇒ assert(CompileResult.success(i) == Success(i))}
  }

  test("CompileResult.apply should return a success on 'good' values") {
    forAll { i: Int ⇒ assert(CompileResult(i) == Success(i))}
  }

  test("CompileResult.apply should return a failure on 'bad' values") {
    forAll { e: Exception ⇒ assert(CompileResult(throw e) == Failure(CompileError(e)))}
  }
}
