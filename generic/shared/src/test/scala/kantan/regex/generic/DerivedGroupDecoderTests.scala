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
package generic

import arbitrary._
import kantan.codecs.shapeless.laws._
import laws._
import laws.discipline._
import org.scalacheck.Arbitrary

@SuppressWarnings(Array("org.wartremover.warts.Null"))
class DerivedGroupDecoderTests extends DisciplineSuite {

  implicit val arbLegal: Arbitrary[LegalGroup[Int Or Boolean]] = arbLegalValue(
    (o: Or[Int, Boolean]) ⇒
      o match {
        case Left(i)  ⇒ Option(i.toString)
        case Right(b) ⇒ Option(b.toString)
    }
  )

  checkAll("GroupDecoder[Int Or Boolean]", GroupDecoderTests[Int Or Boolean].decoder[Byte, String])

}
