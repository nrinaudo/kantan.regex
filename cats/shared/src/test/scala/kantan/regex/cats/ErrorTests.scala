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

package kantan.regex.cats

import cats.Show
import cats.kernel.laws.discipline.EqTests
import kantan.regex.{CompileError, DecodeError, RegexError}
import kantan.regex.cats.arbitrary._
import kantan.regex.laws.discipline.DisciplineSuite

class ErrorTests extends DisciplineSuite {

  checkAll("RegexError", EqTests[RegexError].eqv)

  checkAll("DecodeError", EqTests[DecodeError].eqv)
  checkAll("DecodeError.NoSuchGroupId", EqTests[DecodeError.NoSuchGroupId].eqv)
  checkAll("DecodeError.TypeError", EqTests[DecodeError.TypeError].eqv)
  checkAll("DecodeError.EmptyGroup", EqTests[DecodeError.EmptyGroup.type].eqv)

  checkAll("CompileError", EqTests[CompileError].eqv)

  test("Show[CompileError] should yield a string containing the expected message") {
    forAll { error: CompileError =>
      Show[CompileError].show(error) should include(error.message)
      Show[RegexError].show(error) should include(error.message)
    }
  }

  test("Show[DecodeError.NoSuchGroupId] should yield a string containing the expected group id") {
    forAll { error: DecodeError.NoSuchGroupId =>
      Show[DecodeError.NoSuchGroupId].show(error) should include(error.id.toString)
      Show[DecodeError].show(error) should include(error.id.toString)
      Show[RegexError].show(error) should include(error.id.toString)
    }
  }

  test("Show[DecodeError.TypeError] should yield a string containing the expected message") {
    forAll { error: DecodeError.TypeError =>
      Show[DecodeError.TypeError].show(error) should include(error.message)
      Show[DecodeError].show(error) should include(error.message)
      Show[RegexError].show(error) should include(error.message)
    }
  }

  test("Show[DecodeError.EmptyGroup] should yield a string containing 'empty group'") {
    val expected = "empty group"

    forAll { error: DecodeError.EmptyGroup.type =>
      Show[DecodeError.EmptyGroup.type].show(error) should include(expected)
      Show[DecodeError].show(error) should include(expected)
      Show[RegexError].show(error) should include(expected)
    }
  }

}
