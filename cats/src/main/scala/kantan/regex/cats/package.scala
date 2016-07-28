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

import _root_.cats._
import kantan.codecs.cats.CatsInstances

package object cats extends CatsInstances {
  // - Regex instances -------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val regexFunctor: Functor[Regex] = new Functor[Regex] {
    override def map[A, B](fa: Regex[A])(f: A ⇒ B) = fa.map(f)
  }


  // - Eq instances for errors -----------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val compileErrorEq: Eq[CompileError] = Eq.fromUniversalEquals
  implicit val decodeErrorEq: Eq[DecodeError] = Eq.fromUniversalEquals
}
