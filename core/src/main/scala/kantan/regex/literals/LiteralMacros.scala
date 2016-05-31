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

package kantan.regex.literals

import kantan.regex._
import kantan.regex.macros._

object LiteralMacros {
  def rxImpl(c: Context)(args: c.Expr[Any]*): c.Expr[Pattern] = {
    import c.universe._
    import java.util.regex.Pattern

    c.prefix.tree match {
      case Apply(_, List(Apply(_, List(lit@Literal(Constant(regex: String)))))) ⇒
        try {
          Pattern.compile(regex)
          reify(Pattern.compile(c.Expr[String](lit).splice))
        }
        catch {
          case e: Exception ⇒ c.abort(c.enclosingPosition, s"Illegal regular expression: $regex")
        }

      case _ ⇒
        c.abort(c.enclosingPosition, s"rx can only be used on string literals")
    }
  }
}
