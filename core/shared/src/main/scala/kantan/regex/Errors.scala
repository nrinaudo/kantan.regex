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

import kantan.codecs.error.{Error, ErrorCompanion}

/** Root class for all regular expression related errors. */
sealed abstract class RegexError(msg: String) extends Error(msg)

/** Describes errors that occur while compiling a regular expression. */
sealed case class CompileError(message: String) extends RegexError(message)

object CompileError extends ErrorCompanion("an unspecified compile error occurred")(s => new CompileError(s))

sealed abstract class DecodeError(msg: String) extends RegexError(msg)

object DecodeError {
  @SuppressWarnings(Array("org.wartremover.warts.ObjectThrowable"))
  case object EmptyGroup extends DecodeError("an empty group was found")

  @SuppressWarnings(Array("org.wartremover.warts.StringPlusAny"))
  final case class NoSuchGroupId(id: Int) extends DecodeError(s"no group exist with identifier $id")

  sealed case class TypeError(message: String) extends DecodeError(message)

  object TypeError extends ErrorCompanion("an unspecified type error occurred")(s => new TypeError(s))
}
