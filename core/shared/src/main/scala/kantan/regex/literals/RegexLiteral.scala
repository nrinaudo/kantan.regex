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

import java.util.regex.Pattern
import scala.reflect.macros.blackbox.Context
import scala.util.{Failure, Success, Try => UTry}

final class RegexLiteral(val sc: StringContext) extends AnyVal {
  def rx(args: Any*): Pattern = macro RegexLiteral.rxImpl
}

// Relatively distatefull trick to get rid of spurious warnings.
trait RegexLiteralMacro {
  def rxImpl(c: Context)(args: c.Expr[Any]*): c.Expr[Pattern]
}

object RegexLiteral extends RegexLiteralMacro {

  override def rxImpl(c: Context)(args: c.Expr[Any]*): c.Expr[Pattern] = {
    import c.universe._

    c.prefix.tree match {
      case Apply(_, List(Apply(_, List(lit @ Literal(Constant(str: String)))))) =>
        UTry(Pattern.compile(str)) match {
          case Failure(_) => c.abort(c.enclosingPosition, s"Illegal regex: '$str'")
          case Success(_) =>
            reify {
              Pattern.compile(c.Expr[String](lit).splice)
            }
        }
      case _ =>
        c.abort(c.enclosingPosition, "rx can only be used on string literals")
    }
  }
}

trait ToRegexLiteral {
  implicit def toRegexLiteral(sc: StringContext): RegexLiteral = new RegexLiteral(sc)
}
