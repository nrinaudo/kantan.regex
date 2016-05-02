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

object ops {
  /** Enriches strings with useful methods. */
  implicit class StringOps(val str: String) extends AnyVal {
    /** Attempts to compile the string into a [[Regex]].
      *
      * This is equivalent to calling [[Regex.unsafeCompile]]. Note that, as the name implies, this method is unsafe:
      * should the input string be ill-formed, an exception will be thrown. It's often preferable to use the safe
      * alternative, [[Regex.compile]].
      */
    def regex[A: MatchDecoder]: Regex[DecodeResult[A]] = Regex.unsafeCompile(str)
  }
}
