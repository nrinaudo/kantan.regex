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
  implicit class CompilableOps[S](val expr: S) extends AnyVal {
    private def eval[A](s: String, r: CompileResult[Regex[DecodeResult[A]]]): Iterator[RegexResult[A]] =
      r.map(_.eval(s)).recover { case e ⇒ Iterator(Result.failure(e)) }.get

    def evalRegex[A: MatchDecoder](s: String)(implicit cs: Compiler[S]): Iterator[RegexResult[A]] =
      eval(s, asRegex)
    def evalRegex[A: GroupDecoder](s: String, group: Int)(implicit cs: Compiler[S]): Iterator[RegexResult[A]] =
      eval(s, asRegex(group))

    def evalUnsafeRegex[A: MatchDecoder](s: String)(implicit cs: Compiler[S]): Iterator[A] =
      asUnsafeRegex[A].eval(s).map(_.get)
    def evalUnsafeRegex[A: GroupDecoder](s: String, group: Int)(implicit cs: Compiler[S]): Iterator[A] =
      asUnsafeRegex[A](group).eval(s).map(_.get)

    def asRegex[A: GroupDecoder](group: Int)(implicit cs: Compiler[S]): CompileResult[Regex[DecodeResult[A]]] =
      Regex.compile(expr, group)
    def asRegex[A: MatchDecoder](implicit cs: Compiler[S]): CompileResult[Regex[DecodeResult[A]]] = Regex.compile(expr)

    def asUnsafeRegex[A: GroupDecoder](group: Int)(implicit cs: Compiler[S]): Regex[DecodeResult[A]] =
      Regex.unsafeCompile(expr, group)
    def asUnsafeRegex[A: MatchDecoder](implicit cs: Compiler[S]): Regex[DecodeResult[A]] = Regex.unsafeCompile(expr)
  }
}
