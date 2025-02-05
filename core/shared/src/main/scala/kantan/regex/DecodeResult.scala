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

import kantan.codecs.ResultCompanion

/** Provides construction methods for [[DecodeResult]]. */
object DecodeResult extends ResultCompanion.WithDefault[DecodeError] {
  override protected def fromThrowable(t: Throwable): DecodeError.TypeError =
    DecodeError.TypeError(t)

  /** Creates a new [[kantan.regex.DecodeError.TypeError TypeError]] failure with the specified error message. */
  def typeError(str: String): DecodeResult[Nothing] =
    failure(DecodeError.TypeError(str))

  /** Creates a new [[kantan.regex.DecodeError.TypeError TypeError]] failure with the specified error. */
  def typeError(e: Exception): DecodeResult[Nothing] =
    failure(DecodeError.TypeError(e))

  /** Creates a new [[kantan.regex.DecodeError.NoSuchGroupId NoSuchgroupId]] failure for the specified group id. */
  def noSuchGroupId(id: Int): DecodeResult[Nothing] =
    failure(DecodeError.NoSuchGroupId(id))

  /** Creates a new [[kantan.regex.DecodeError.EmptyGroup EmptyGroup]] failure. */
  val emptyGroup: DecodeResult[Nothing] = failure(DecodeError.EmptyGroup)
}
