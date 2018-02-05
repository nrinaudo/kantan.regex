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

import _root_.scalaz._, Scalaz._
import _root_.scalaz.scalacheck.ScalazProperties._
import arbitrary._, equality._
import kantan.codecs.scalaz.laws.discipline.ScalazDisciplineSuite

class DecoderTests extends ScalazDisciplineSuite {

  // Note that we're not testing MatchDecoder instances - Match is mutable and using MatchDecoder as a purely functional
  // abstraction is wrong.

  checkAll("GroupDecoder", plus.laws[GroupDecoder])
  checkAll("GroupDecoder", monadError.laws[GroupDecoder, DecodeError])

}
