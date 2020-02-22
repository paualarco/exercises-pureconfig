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

import java.net.{URI, URL}
import java.time.{DayOfWeek, LocalDate, LocalDateTime, LocalTime, Month, YearMonth}
import java.time.format.DateTimeFormatter

import org.scalaexercises.definitions._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import pureconfig.generic.ProductHint
import pureconfig.{ConfigSource, _}
import pureconfiglib.Domain.{DurationConfig, PathConfig, PrimitivesConf, TimeConfig}
import pureconfig.configurable._
import java.time.Month.FEBRUARY

import scala.concurrent.duration._
import pureconfig.generic.auto._
/** @param name Supported Types
  */
object SupportedTypes extends AnyFlatSpec with Matchers with Section {


  implicit def hint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

  /** PureConfig comes with baked-in support for many types, most of them from the standard Java and Scala libraries.
   *
   * `String`, `Boolean`, `Double`, `Float`, `Int`, `Long`, `Short`, `Char`.
   *
   * {{{
   * case class PrimitivesConf(string: String, bool: Boolean, double: Double, float: Float, int: Int, long: Long, short: Short, char: Char)
   * }}}
   *
   *
   * */
  def loadPrimitivesConfig(string: String, bool: Boolean, double: Double, float: Float,
                           int: Int, long: Long, short: Short, char: Char): Unit = {
    val primitivesSource = ConfigSource.string("{ " +
      "string = primitive, " +
      "bool = true," +
      "double = 2.1," +
      "float = 1.0," +
      "int = 1," +
      "long = 100," +
      "short = 1," +
      "char = p," +
      " }")
    val primitivesConfig = primitivesSource.loadOrThrow[PrimitivesConf]
    println(primitivesConfig)
    primitivesConfig.string shouldBe string
    primitivesConfig.bool   shouldBe bool
    primitivesConfig.double shouldBe double
    primitivesConfig.float  shouldBe float
    primitivesConfig.int    shouldBe int
    primitivesConfig.long   shouldBe long
    primitivesConfig.short  shouldBe short
    primitivesConfig.char   shouldBe char
    //hint dobule, float and long can be identified by its suffix respectively: d, f, L
  }

 /**
   * `URL`, `URI`,`java.util.UUID`,`java.nio.file.Path`, `java.io.File`
   * {{{
   * case class PathConfig(path: java.nio.file.Path, file: java.io.File, url: URL, uri: URI)
   * }}}
   *
   * */
 def loadPathsConfig(path: String, file: String, url: String, uri: String): Unit = {
   val configSource = ConfigSource.string("{ " +
     "path = src/main/resources, " +
     "file: src/main/resources/application.conf, " +
     "url: \"https://pureconfig.github.io\", " +
     "uri: \"https://pureconfig.github.io/docs/index.html\"" +
     "}")
   val pathConfig: PathConfig = configSource.loadOrThrow[PathConfig]
   pathConfig.path shouldBe java.nio.file.Paths.get(path)
   pathConfig.file shouldBe new java.io.File(file)
   pathConfig.url  shouldBe new URL(url)
   pathConfig.uri  shouldBe new URI(uri)
 }

 /**
  * `Duration`, `FiniteDuration`
  * import scala.concurrent.durations._
  * {{{
  case class DurationConfig(duration: Duration, finiteDuration: FiniteDuration)
  * }}}
  *
  * */
  def loadDurationConfig(duration: Int, finiteDuration: Int): Unit = {
    val configSource = ConfigSource.string("{ " +
      "duration = 510 milliseconds, " +
      "finiteDuration: 1 hour" +
      "}")
    val timeConfig = configSource.loadOrThrow[DurationConfig]
    timeConfig.duration       shouldBe (duration milliseconds)
    timeConfig.finiteDuration shouldBe (finiteDuration hour)
  }

  /**
   * `Duration`, `FiniteDuration`, `java.time._`
   *
   * import pureconfig.configurable._
   * implicit val localDateConvert = localDateConfigConvert(DateTimeFormatter.ISO_DATE)
   *
   * {{{
   * case class TimeConfig(duration: Duration, finiteDuration: FiniteDuration, dayOfWeek: DayOfWeek, monthDay: MonthDay, month: Month, year: Year, localDate: LocalDate, localDateTime: LocalDateTime)
   * }}}
   *
   * */
  def loadTimeConfig(month: Int, year: Int, day: Int, hours: Int, minues: Int, seconds: Int): Unit = {
    val configSource = ConfigSource.string("{ " +
      "localDate: 2020-02-29, " +
      "localDateTime: \"2020-02-29T13:21:30\"" +
      "}")
    implicit val localDateConvert = localDateConfigConvert(DateTimeFormatter.ISO_DATE)
    implicit val localDateTimeConvert = localDateTimeConfigConvert(DateTimeFormatter.ISO_DATE_TIME)
    val timeConfig = configSource.loadOrThrow[TimeConfig]
    timeConfig.localDate      shouldBe LocalDate.of(year, FEBRUARY, day)
    timeConfig.localDateTime  shouldBe LocalDateTime.of(timeConfig.localDate, LocalTime.of(hours, minues, seconds))
  }

  /**
   * `java.math.BigDecimal`, `java.lang.Enum`,
  * `java.math.BigInteger`, `scala.math.BigDecimal`, `scala.math.BigInt`, `ConfigValue`, `ConfigObject` and `ConfigList`;
  * Additionally, PureConfig also handles the following collections and composite Scala structures:
    * `Option`, `TraversableOnce`, `Map[String, *]`, `shapeless.HList` and case classes.
    * Typesafe ConfigValue, ConfigObject and ConfigList;
  * {{{
    * case class Example(name: String, number: Int)
    * }}}
  *
  * */
  def loadOrThrowExample3(name: String, number: Int): Unit = {
    val configSource = ConfigSource.string("{ name = first, number = 1 }")

  }

}
