/*
 * Copyright 2020 47 Degrees, LLC. <http://www.47deg.com>
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
import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.time.format.DateTimeFormatter
import java.time.Month.FEBRUARY

import org.scalaexercises.definitions._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import pureconfig.ConvertHelpers.catchReadError
import pureconfig.generic.ProductHint
import pureconfig.{ConfigSource, _}
import pureconfiglib.Domain.{
  ApplicationConfig,
  CollectionsConfig,
  DurationConfig,
  OptionConfig,
  PathConfig,
  PrimitivesConf,
  TimeConfig
}
import pureconfig.configurable._
import pureconfig.generic.auto._

import scala.language.postfixOps
import scala.concurrent.duration._

/** @param name Supported Types
 */
object SupportedTypes extends AnyFlatSpec with Matchers with Section {

  implicit def hint[T] =
    ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

  /**
   * Before of all, it is important to know that Pureconfig allows you to use different naming conventions for case classes and fields, in which by default,
   * it expects config kets to be written in kebab case (such as `my-field`) and the associated field names are wiritten in camel case (such as `myField`).
   * A mapping between different naming conventions is done using a ConfigFieldMapping object, with which one can construct a ProductHint.
   * - `CamelCase` (examples: `camelCase`, `useMorePureconfig`)
   * - `SnakeCase` (examples: `snake_case`, `use_more_pureconfig`)
   * - `KebabCase` (examples: `kebab-case`, `use-more-pureconfig`)
   * - `PascalCase`: (examples: `PascalCase`, `UseMorePureconfig`)
   *
   * These exercises are using the `CamelCase` field mapping hint as:
   * {{{
   * implicit def hint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))
   * }}}
   *
   * PureConfig also comes with baked-in support for many types, most of them from the standard Java and Scala libraries.
   * In this section we will see an very complete overview of examples with most of the types that pureconfig supports.
   * Each exercise has a `case class` that wraps the values loaded from the configuration source on it.
   *
   * Let's then start with the more basic types, the primitive ones:
   * String, `Boolean`, `Double`, `Float`, `Int`, `Long`, `Short`, `Char`.
   *
   * In which the wrapper configuration case class in this case is called ´PrimitivesConf´.
   * {{{
   * case class PrimitivesConf(string: String, bool: Boolean, double: Double, float: Float, int: Int, long: Long, short: Short, char: Char)
   * }}}
   *
   *
   * */
  def loadPrimitivesConfig(
      string: String,
      bool: Boolean,
      double: Double,
      float: Float,
      int: Int,
      long: Long,
      short: Short,
      char: Char): Unit = {
    val primitivesSource = ConfigSource.string(
      "{ " +
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
    primitivesConfig.string shouldBe string
    primitivesConfig.bool shouldBe bool
    primitivesConfig.double shouldBe double
    primitivesConfig.float shouldBe float
    primitivesConfig.int shouldBe int
    primitivesConfig.long shouldBe long
    primitivesConfig.short shouldBe short
    primitivesConfig.char shouldBe char
    //hint double, float and long can be identified by its suffix respectively: d, f, L
  }

  /** The default behavior of ConfigReaders that are derived in PureConfig is to return a KeyNotFound
   * failure when a required key is missing unless its type is an Option, in which case it is read as a None.
   * {{{
   * case class OptionConfig(optionA: Option[String], optionB: Option[String], optionC: Option[Int])
   * }}}
   *
   **/
  def loadOptionalConfig(
      optionA: Option[String],
      optionB: Option[String],
      optionC: Option[Int]): Unit = {
    val configSource =
      ConfigSource.string("{ optionA: PureOption, optionC: 101 }")
    val optionConfig: OptionConfig = configSource.loadOrThrow[OptionConfig]
    optionConfig.optionA shouldBe optionA
    optionConfig.optionB shouldBe optionB
    optionConfig.optionC shouldBe optionC
  }

  /** Usually, collection types are needed to be defined in configuration files, in which as you may imagine, pureconfig allows to work with them too.
   * The following exercise shows an example for `List, `Set` and `Map[Int, String]` from the `scala.collection` package.
   *
   * For some types, PureConfig cannot automatically derive a reader because there are multiple ways to convert a configuration value to them.
   * For instance, reading maps with non-string keys, unless a way to convert the keys to and from strings is provided, PureConfig won’t be able to derive a reader.
   *
   * {{{
   * case class CollectionsConfig(
   * list: List[Char],
   * set: Set[Int],
   * map: Map[Int, String])
   * }}}
   *
   **/
  def loadCollectionsConfig(list: List[Char], set: Set[Int], map: Map[Int, String]): Unit = {
    import pureconfig.ConvertHelpers._
    implicit val mapReader =
      genericMapReader[Int, String](catchReadError(_.toInt))
    val configSource = ConfigSource.string(
      "{ " +
        "list: [ a, e, i, o, u ], " +
        "set: [ 1, 2, 3, 4, 5 ], " +
        "map : { 1: A, 2: E, 3: I, 4: O, 5: U} " +
        "}")
    val collectionsConfig = configSource.loadOrThrow[CollectionsConfig]
    collectionsConfig.list shouldBe list
    collectionsConfig.set shouldBe set
    collectionsConfig.map shouldBe map
  }

  /** When working with dates in configuration, it is also needed to create converters for reading them.
   * For example, `LocalDate` in PureConfig cannot derive a reader because there are multiple
   * `DateTimeFormatters that can be used to convert a string into a `LocalDate.
   * Examples of different formats are `yyyy-mm-dd`, e.g. "2016-01-01"`, and `yyyymmdd`, e.g. `"20160101"`.
   *
   * For those types, PureConfig provides a way to create readers from the necessary parameters. `
   * These methods can be found under the package pureconfig.configurable.
   * Once the output of a `pureconfig.configurable` method for a certain type is in scope, PureConfig can start using that configured reader.
   *
   *
   * {{{
   * import pureconfig.configurable._
   * implicit val localDateConvert = localDateConfigConvert(DateTimeFormatter.ISO_DATE)
   *
   * case class TimeConfig(
   * duration: Duration,
   * finiteDuration: FiniteDuration,
   * dayOfWeek: DayOfWeek,
   * monthDay: MonthDay,
   * month: Month,
   * year: Year,
   * localDate: LocalDate,
   * localDateTime: LocalDateTime)
   * }}}
   *
   **/
  def loadTimeConfig(
      month: Int,
      year: Int,
      day: Int,
      hours: Int,
      minues: Int,
      seconds: Int): Unit = {
    val configSource = ConfigSource.string(
      "{ " +
        "localDate: 2020-02-29, " +
        "localDateTime: \"2020-02-29T13:21:30\"" +
        "}")
    implicit val localDateConvert     = localDateConfigConvert(DateTimeFormatter.ISO_DATE)
    implicit val localDateTimeConvert = localDateTimeConfigConvert(DateTimeFormatter.ISO_DATE_TIME)
    val timeConfig                    = configSource.loadOrThrow[TimeConfig]
    timeConfig.localDate shouldBe LocalDate.of(year, FEBRUARY, day)
    timeConfig.localDateTime shouldBe LocalDateTime.of(
      timeConfig.localDate,
      LocalTime.of(hours, minues, seconds))
  }

  /** All those duration types within the scala package `scala.concurrent.durations` also can be read in this format from config sources,
   * in this case with no need of using any converter.
   *
   * {{{
   * case class DurationConfig(duration: Duration, finiteDuration: FiniteDuration)
   * }}}
   *
   **/
  def loadDurationConfig(finiteDuration: FiniteDuration): Unit = {

    val configSource = ConfigSource.string(
      "{ " +
        "duration: Inf, " +
        "finiteDuration: 20 minutes" +
        "}")
    val timeConfig = configSource.loadOrThrow[DurationConfig]
    timeConfig.duration shouldBe Duration.Inf
    timeConfig.finiteDuration shouldBe finiteDuration
  }

  /** Another use case very common when dealing with configurations is to find string paths and urls,
   * in which PureConfig translate them into `java.nio.file.Path`, `java.io.File`, `java.net.URL`, `java.net.URI`.
   *
   * {{{
   * case class PathConfig(path: java.nio.file.Path, file: java.io.File, url: URL, uri: URI)
   * }}}
   *
   * */
  def loadPathsConfig(path: String, file: String, url: String, uri: String): Unit = {
    val configSource = ConfigSource.string(
      "{ " +
        "path = src/main/resources, " +
        "file: src/main/resources/application.conf, " +
        "url: \"https://pureconfig.github.io\", " +
        "uri: \"https://pureconfig.github.io/docs/index.html\"" +
        "}")
    val pathConfig: PathConfig = configSource.loadOrThrow[PathConfig]
    pathConfig.path shouldBe java.nio.file.Paths.get(path)
    pathConfig.file shouldBe new java.io.File(file)
    pathConfig.url shouldBe new URL(url)
    pathConfig.uri shouldBe new URI(uri)
  }

  /** To finish, the following exercise is a combination of all the above use case in the same config source,
   * being seen as the representation of the application config.
   * As you would notice, only fields for option and collections configuration have been defined in the sours,
   * however, the `ApplicationConf` does have all the sub configurations defined as an option.
   * Also in this case the converters for `Map` and `java.time._` types are needed even if they are not present in the
   * config source, but because they were declared in the `case class`.
   *
   * {{{
   * case class ApplicationConfig(primitivesConf: Option[PrimitivesConf],
                               optionConfig: Option[OptionConfig],
                               collectionsConfig: Option[CollectionsConfig],
                               timeConfig: Option[TimeConfig],
                               durationConfig: Option[DurationConfig],
                               pathConfig: Option[PathConfig]
                              )   * }}}
   *
   * */
  def loadApplicationConfig(
      optionA: Option[String],
      optionB: Option[String],
      optionC: Option[Int],
      list: List[String],
      set: Set[String],
      map: Map[Int, String]): Unit = {

    val configSource = ConfigSource.string(
      "{ " +
        "optionConfig: { optionB: present }, " +
        "collectionsConfig: { list: [], set: [], map: {} }" +
        "}")
    implicit val mapReader =
      genericMapReader[Int, String](catchReadError(_.toInt))
    implicit val localDateConvert           = localDateConfigConvert(DateTimeFormatter.ISO_DATE)
    implicit val localDateTimeConvert       = localDateTimeConfigConvert(DateTimeFormatter.ISO_DATE_TIME)
    val pathConfig: ApplicationConfig       = configSource.loadOrThrow[ApplicationConfig]
    val optionConfig: OptionConfig          = pathConfig.optionConfig.get
    val collectionConfig: CollectionsConfig = pathConfig.collectionsConfig.get

    pathConfig.primitivesConf.isDefined shouldBe false
    pathConfig.optionConfig.isDefined shouldBe true
    pathConfig.collectionsConfig.isDefined shouldBe true
    pathConfig.timeConfig.isDefined shouldBe false
    pathConfig.durationConfig.isDefined shouldBe false
    pathConfig.pathConfig.isDefined shouldBe false
    optionConfig.optionA shouldBe optionA
    optionConfig.optionB shouldBe optionB
    optionConfig.optionC shouldBe optionC
    collectionConfig.list shouldBe list
    collectionConfig.set shouldBe set
    collectionConfig.map shouldBe map
  }

}
