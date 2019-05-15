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
package literals

import contextual._
import java.util.regex.Pattern

object RegexLiteral extends Verifier[Pattern] {

  override def check(string: String): Either[(Int, String), Pattern] =
    // We could do a better job at analysing the error message and extracting the interesting bits, but:
    // - this is not guaranteed to always work - error message might change format
    // - it doesn't work with scala.js, and I'm not interested in this enough to have different error handling depending
    //   on the target platform.
    try {
      Right(Pattern.compile(string))
    } catch {
      case e: Exception => Left((0, e.getMessage))
    }

}
