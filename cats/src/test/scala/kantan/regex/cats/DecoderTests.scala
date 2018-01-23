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
package cats

import _root_.cats.Eq
import _root_.cats.data.EitherT
import _root_.cats.instances.all._
import _root_.cats.laws.discipline.{MonadErrorTests, SemigroupKTests}
import _root_.cats.laws.discipline.SemigroupalTests.Isomorphisms
import laws.discipline._, arbitrary._, equality._

class DecoderTests extends DisciplineSuite {

  // Note that we're not testing MatchDecoder instances - Match is mutable and using MatchDecoder as a purely functional
  // abstraction is wrong.

  // For some reason, these are not derived automatically. I *think* it's to do with GroupDecoder being a type alias
  // for a type with many holes, but this is slightly beyond me.
  implicit val eqGroupEitherT: Eq[EitherT[GroupDecoder, DecodeError, Int]] = EitherT.catsDataEqForEitherT
  implicit val groupIso: Isomorphisms[GroupDecoder]                        = Isomorphisms.invariant

  checkAll("GroupDecoder", SemigroupKTests[GroupDecoder].semigroupK[Int])
  checkAll("GroupDecoder", MonadErrorTests[GroupDecoder, DecodeError].monadError[Int, Int, Int])

}
