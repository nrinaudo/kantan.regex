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

import java.util.regex.Pattern

trait Compiler[A] {
  def compile(a: A): CompileResult[Pattern]
}

object Compiler {
  def apply[A](f: A ⇒ CompileResult[Pattern]): Compiler[A] = new Compiler[A] {
    override def compile(a: A) = f(a)
  }

  def safe[A](f: A ⇒ Pattern): Compiler[A] = Compiler(f andThen CompileResult.success)

  implicit val scalaRegex: Compiler[scala.util.matching.Regex] = safe(_.pattern)
  implicit val pattern: Compiler[Pattern] = safe(identity)
  implicit val string: Compiler[String] = Compiler(s ⇒ CompileResult(Pattern.compile(s)))
}
