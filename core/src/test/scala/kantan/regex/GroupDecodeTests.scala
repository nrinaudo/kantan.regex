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

import kantan.regex.laws.discipline.arbitrary._
import kantan.regex.ops._
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class GroupDecodeTests extends FunSuite with GeneratorDrivenPropertyChecks with Matchers {
  test("Instances created through GroupDecoder.apply should behave as expected") {
    forAll { (s: String, f: (Option[String] ⇒ DecodeResult[Int])) ⇒
      implicit val decoder: GroupDecoder[Int] = GroupDecoder.from(f)

      val r = ".*".asUnsafeRegex[Int]
      r.eval(s).next should be(f(Some(s)))
    }
  }

  test("The instance summoning method should behave as expected") {
    forAll { (f: (Option[String] ⇒ DecodeResult[Int])) ⇒
      implicit val decoder: GroupDecoder[Int] = GroupDecoder.from(f)

      decoder should be theSameInstanceAs GroupDecoder[Int]
    }
  }
}
