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

/** Root class for all regular expression related errors. */
sealed abstract class RegexError extends Exception with Product with Serializable

/** Describes errors that occur while compiling a regular expression. */
sealed case class CompileError(message: String) extends RegexError {
  override final def getMessage = message
}

object CompileError {
  def apply(msg: String, t: Throwable): CompileError = new CompileError(msg) {
    override def getCause = t
  }

  def apply(t: Throwable): CompileError = CompileError(Option(t.getMessage).getOrElse("Compile error"), t)
}

sealed abstract class DecodeError extends RegexError

object DecodeError {
  final case class EmptyGroup() extends DecodeError

  final case class NoSuchGroupId(id: Int) extends DecodeError

  sealed case class TypeError(message: String) extends DecodeError {
    override final def getMessage = message
  }

  object TypeError {
    def apply(msg: String, t: Throwable): TypeError = new TypeError(msg) {
      override def getCause = t
    }

    def apply(t: Throwable): TypeError = TypeError(Option(t.getMessage).getOrElse("Type error"), t)
  }
}
