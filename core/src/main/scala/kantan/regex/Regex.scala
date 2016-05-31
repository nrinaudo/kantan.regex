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

trait Regex[A] { self ⇒
  def map[B](f: A ⇒ B): Regex[B] = Regex(s ⇒ self.eval(s).map(f))
  def eval(str: String): Iterator[A]
}

object Regex {
  def apply[A](f: String ⇒ Iterator[A]): Regex[A] = new Regex[A] {
    override def eval(str: String) = f(str)
  }

  def apply[A](pattern: Pattern)(implicit da: MatchDecoder[A]): Regex[DecodeResult[A]] = new Regex[DecodeResult[A]] {
    override def eval(s: String) = new MatchIterator(pattern.matcher(s)).map(m ⇒ da.decode(m))
    override def toString = pattern.toString
  }
}
