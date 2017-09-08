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

import kantan.regex.DecodeError.TypeError
import kantan.regex.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class ErrorTests extends FunSuite with GeneratorDrivenPropertyChecks {
  test("CompileErrors should be equal if the underlying errors are the same") {
    forAll { (e1: CompileError, e2: RegexError) ⇒
      assert((e1 == e2) == ((e1, e2) match {
        case (CompileError(t1), CompileError(t2)) ⇒ t1 == t2
        case _                                    ⇒ false
      }))
    }
  }

  test("CompileErrors should have identical hashCodes if the underlying errors have the same hashCodes") {
    forAll { (e1: CompileError, e2: CompileError) ⇒
      assert((e1.hashCode() == e2.hashCode()) == (e1.message.hashCode == e2.message.hashCode))
    }
  }

  test("TypeErrors should be equal if the underlying errors are the same") {
    forAll { (e1: TypeError, e2: RegexError) ⇒
      assert((e1 == e2) == ((e1, e2) match {
        case (TypeError(t1), TypeError(t2)) ⇒ t1 == t2
        case _                              ⇒ false
      }))
    }
  }

  test("TypeErrors should have identical hashCodes if the underlying errors have the same hashCodes") {
    forAll { (e1: TypeError, e2: TypeError) ⇒
      assert((e1.hashCode() == e2.hashCode()) == (e1.message.hashCode == e2.message.hashCode))
    }
  }
}
