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
package scalaz

import _root_.scalaz.Show
import _root_.scalaz.scalacheck.ScalazProperties.{equal => equ}
import arbitrary._
import kantan.codecs.scalaz.laws.discipline.ScalazDisciplineSuite

class ErrorTests extends ScalazDisciplineSuite {

  checkAll("RegexError", equ.laws[RegexError])

  checkAll("DecodeError", equ.laws[DecodeError])
  checkAll("DecodeError.NoSuchGroupId", equ.laws[DecodeError.NoSuchGroupId])
  checkAll("DecodeError.TypeError", equ.laws[DecodeError.TypeError])
  checkAll("DecodeError.EmptyGroup", equ.laws[DecodeError.EmptyGroup.type])

  checkAll("CompileError", equ.laws[CompileError])

  test("Show[CompileError] should yield a string containing the expected message") {
    forAll { error: CompileError =>
      Show[CompileError].shows(error) should include(error.message)
      Show[RegexError].shows(error) should include(error.message)
    }
  }

  test("Show[DecodeError.NoSuchGroupId] should yield a string containing the expected group id") {
    forAll { error: DecodeError.NoSuchGroupId =>
      Show[DecodeError.NoSuchGroupId].shows(error) should include(error.id.toString)
      Show[DecodeError].shows(error) should include(error.id.toString)
      Show[RegexError].shows(error) should include(error.id.toString)
    }
  }

  test("Show[DecodeError.TypeError] should yield a string containing the expected message") {
    forAll { error: DecodeError.TypeError =>
      Show[DecodeError.TypeError].shows(error) should include(error.message)
      Show[DecodeError].shows(error) should include(error.message)
      Show[RegexError].shows(error) should include(error.message)
    }
  }

  test("Show[DecodeError.EmptyGroup] should yield a string containing 'empty group'") {
    val expected = "empty group"

    forAll { error: DecodeError.EmptyGroup.type =>
      Show[DecodeError.EmptyGroup.type].shows(error) should include(expected)
      Show[DecodeError].shows(error) should include(expected)
      Show[RegexError].shows(error) should include(expected)
    }
  }

}
