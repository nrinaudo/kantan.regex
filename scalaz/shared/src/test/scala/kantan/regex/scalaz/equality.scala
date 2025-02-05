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

package kantan.regex.scalaz

import kantan.regex.Regex
import scalaz.Equal
import scalaz.Scalaz._

object equality extends kantan.codecs.scalaz.laws.discipline.EqualInstances {

  implicit def equalRegex[A: Equal]: Equal[Regex[A]] =
    new Equal[Regex[A]] {
      override def equal(a1: Regex[A], a2: Regex[A]) =
        kantan.codecs.laws.discipline.equality
          .eq((str: String) => a1.eval(str).toList, (str: String) => a2.eval(str).toList)(
            implicitly[Equal[List[A]]].equal
          )
    }

}
