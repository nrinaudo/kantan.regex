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

import kantan.codecs.Result

/** Provides creation methods for [[CompileResult]]. */
object CompileResult {
  /** Creates a successful compilation result containing the specified value. */
  def success[A](a: A): CompileResult[A] = Result.success(a)

  /** Creates a new [[CompileResult]] with the specified value.
    *
    * If an exception is thrown during evaluation of the specified by-name parameter, the returned value will be a
    * failure. Otherwise, it will be a success.
    */
  def apply[A](a: ⇒ A): CompileResult[A] = Result.nonFatal(a).leftMap(CompileError.apply)
}
