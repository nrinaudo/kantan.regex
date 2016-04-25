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

sealed abstract class RegexError

sealed abstract class DecodeError extends RegexError

final case class CompileError(cause: Throwable) extends RegexError {
  override def toString: String = s"CompileError(${cause.getMessage})"

  override def equals(obj: Any) = obj match {
    case CompileError(cause2) ⇒ cause.getClass == cause2.getClass
    case _                 ⇒ false
  }

  override def hashCode(): Int = cause.hashCode()
}

object DecodeError {
  final case class NoSuchGroupId(id: Int) extends DecodeError

  final case class NoSuchGroupName(name: String) extends DecodeError

  final case class TypeError(cause: Throwable) extends DecodeError {
    override def toString: String = s"TypeError(${cause.getMessage})"

    override def equals(obj: Any) = obj match {
      case TypeError(cause2) ⇒ cause.getClass == cause2.getClass
      case _                 ⇒ false
    }

    override def hashCode(): Int = cause.hashCode()
  }
}
