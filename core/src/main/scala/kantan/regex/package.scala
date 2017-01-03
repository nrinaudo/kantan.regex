/*
 * Copyright 2017 Nicolas Rinaudo
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

package kantan

import kantan.codecs.{Decoder, Result}

package object regex {
  // - Convenience aliases ---------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type Pattern = java.util.regex.Pattern



  // - Decoder types ---------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** Type class for types that can be decoded from capturing groups.
    *
    * See the [[GroupDecoder$ companion object]] for instance creation methods.
    *
    * @documentable
    */
  type GroupDecoder[A] = Decoder[Option[String], A, DecodeError, codecs.type]

  /** Type class for type that can be decoded from regular expression matches.
    *
    * See the [[MatchDecoder$ companion object]] for instance creation methods.
    *
    * @documentable
    */
  type MatchDecoder[A] = Decoder[Match, A, DecodeError, codecs.type]



  // - Result types ----------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type Success[A] = Result.Success[A]
  val Success = Result.Success

  type Failure[A] = Result.Failure[A]
  val Failure = Result.Failure

  /** Result type for decoding operations.
    *
    * @documentable
    */
  type DecodeResult[A]  = Result[DecodeError, A]

  /** Result type for compilation operations.
    *
    * @documentable
    */
  type CompileResult[A] = Result[CompileError, A]

  /** Result type for all regex related operations (encompasses both [[DecodeResult]] and [[CompileResult]].
    *
    * @documentable
    */
  type RegexResult[A]   = Result[RegexError, A]
}
