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

import cats.instances.all._
import cats.laws.discipline.FunctorTests
import kantan.regex.Regex
import kantan.regex.cats.arbitrary._
import kantan.regex.cats.equality._
import kantan.regex.laws.discipline.DisciplineSuite

class RegexTests extends DisciplineSuite {

  checkAll("Regex", FunctorTests[Regex].functor[Int, Int, Int])

}
