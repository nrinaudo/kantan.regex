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

object ops {
  implicit class StringOps(val str: String) extends AnyVal {
    private def eval[A](s: String, r: CompileResult[Regex[DecodeResult[A]]]): Iterator[RegexResult[A]] =
      r.map(_.eval(s)).recover { case e ⇒ Iterator(Result.failure(e)) }.get

    def evalRegex[A](r: Regex[A]): Iterator[A] = r.eval(str)

    def evalRegex[A: MatchDecoder](expr: String): Iterator[RegexResult[A]] =
      eval(str, Regex.compile(expr))
    def evalRegex[A: GroupDecoder](expr: String, group: Int): Iterator[RegexResult[A]] =
      eval(str, Regex.compile(expr, group))

    def evalUnsafeRegex[A: MatchDecoder](expr: String): Iterator[A] =
      Regex.unsafeCompile(expr).eval(str).map(_.get)
    def evalUnsafeRegex[A: GroupDecoder](expr: String, group: Int): Iterator[A] =
      Regex.unsafeCompile(expr, group).eval(str).map(_.get)
  }

  implicit class CompilableOps[S](val expr: S) extends AnyVal {
    def asRegex[A: GroupDecoder](group: Int)(implicit cs: Compiler[S]): CompileResult[Regex[DecodeResult[A]]] =
      Regex.compile(expr, group)
    def asRegex[A: MatchDecoder](implicit cs: Compiler[S]): CompileResult[Regex[DecodeResult[A]]] = Regex.compile(expr)

    def asUnsafeRegex[A: GroupDecoder](group: Int)(implicit cs: Compiler[S]): Regex[DecodeResult[A]] =
      Regex.unsafeCompile(expr, group)
    def asUnsafeRegex[A: MatchDecoder](implicit cs: Compiler[S]): Regex[DecodeResult[A]] = Regex.unsafeCompile(expr)
  }
}
