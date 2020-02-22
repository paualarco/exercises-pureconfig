/*
 * Copyright 2019 47 Degrees, LLC. <http://www.47deg.com>
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

package pureconfiglib

import org.scalaexercises.definitions._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import pureconfig.{ConfigSource, _}
import pureconfiglib.Domain.{Example, multiExampleSource}

import scala.util.Try

/** @param name Supported Types
  */
object SupportedTypes extends AnyFlatSpec with Matchers with Section {

  /** PureConfig comes with baked-in support for many types, most of them from the standard Java and Scala libraries.
   *
   * `String`, `Boolean`, `Double`, `Float`, `Int`, `Long`, `Short`, `Char`.
   *
   *
   * `URL`, `URI`,
   * `Duration`, `FiniteDuration`, `java.lang.Enum`, `java.time`, `java.io.File`, `java.util.UUID`,
   * `java.nio.file.Path`, `java.util.regex.Pattern`, `scala.util.matching.Regex`, `java.math.BigDecimal`,
   * `java.math.BigInteger`, `scala.math.BigDecimal`, `scala.math.BigInt`, `ConfigValue`, `ConfigObject` and `ConfigList`;
   * Additionally, PureConfig also handles the following collections and composite Scala structures:
   * `Option`, `TraversableOnce`, `Map[String, *]`, `shapeless.HList` and case classes.
   * java.nio.file.Path;
   * java.util.regex.Pattern and scala.util.matching.Regex;
   * java.math.BigDecimal, java.math.BigInteger, scala.math.BigDecimal, and scala.math.BigInt;
   * Typesafe ConfigValue, ConfigObject and ConfigList;
   * {{{
   * case class Example(name: String, number: Int)
   * }}}
   *
   * Import pureconfig._ and pureconfig.generic.auto._ in the context where the config will be loaded.
   * Then config can be loaded into Example` instance using different methods.
   *
   * A configuration source is commonly defined in the `application.conf`, added as a resource file of your application, usually placed in (`src/main/resources`)
   * But it can also be parsed from string type, as in it has been done in the following examples.
   *
   * */
  def loadOrThrowExample(name: String, number: Int): Unit = {
    val configSource = ConfigSource.string("{ name = first, number = 1 }")
    val example = configSource.loadOrThrow[Example]
    example.name shouldBe name
    example.number shouldBe number
  }


}
