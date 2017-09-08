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

package kantan.regex.ops

import kantan.regex.{Compiler, _}

/** Provides useful syntax for types that have a [[Compiler]] instance. */
final class CompilerOps[S: Compiler](val expr: S) {

  /** Compiles this value as a [[Regex]]. */
  def asRegex[A: MatchDecoder]: CompileResult[Regex[DecodeResult[A]]] =
    Compiler[S].compile(expr)
  def asRegex[A: GroupDecoder](group: Int): CompileResult[Regex[DecodeResult[A]]] =
    Compiler[S].compile(expr, group)

  def asUnsafeRegex[A: MatchDecoder]: Regex[DecodeResult[A]] = Compiler[S].unsafeCompile(expr)

  /** Unsafe version of [[asRegex[A](group:Int)*]]. */
  def asUnsafeRegex[A: GroupDecoder](group: Int): Regex[DecodeResult[A]] =
    Compiler[S].unsafeCompile(expr, group)
}

trait ToCompilerOps {
  implicit def toRegexCompilerOps[A: Compiler](a: A): CompilerOps[A] = new CompilerOps(a)
}

object compiler extends ToCompilerOps
