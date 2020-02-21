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

import org.scalatest.matchers.should.Matchers
import org.scalaexercises.definitions._
import org.scalatest.flatspec.AnyFlatSpec
import pureconfig.ConfigSource
import pureconfiglib.Domain.Example
import pureconfig._
import pureconfig.generic.auto._

import scala.tools.nsc.interpreter.Results.Result
import scala.util.Try

/** @param name section_title
  */
object LoadingConfig extends AnyFlatSpec with Matchers with Section {

  /** PureConfig is a Scala library for loading configuration files.
   * It reads Typesafe Config configurations written in HOCON, Java .properties, or JSON to native Scala classes in a boilerplate-free way.
   * Sealed traits, case classes, collections, optional values, and many other types are all supported out-of-the-box.
   * The first thing you have to do is to define the data types and a case class to hold the configuration.
   *
   * {{{
   * case class Example(name: String, number: Int)
   * }}}
   *
   * Import `pureconfig._` and `pureconfig.generic.auto._` in the context where the config will be loaded.
   * Then config can be loaded into `Example` instance using different methods.
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

  /** As you can have noticed, `loadOrThrowAnother[Example]` is the straight forward method to load configuration,
   * in which it will throw a `ConfigReaderException[A]` in case there is a any type mismatch or parsing error when loading.
   * */
  def loadOrThrowFailure(isFailure: Boolean): Unit = {
    //when
    val configSource = ConfigSource.string("{ name = second, number = this_should_be_an_integer }")
    val tryConfig = Try(configSource.loadOrThrow[Example])

    //then
    tryConfig.isFailure shouldBe isFailure
  }

  /** A safer method for loading it would be just `load[Example]`,
   * in which in this case will return an Monad Either of type `Either[ConfigReaderFailures, A]
   * */
  def loadExample(isFirstRigth: Boolean, isSecondRight: Boolean): Unit = {
    //when
    val config1: ConfigReader.Result[Example] = ConfigSource.string("{ name = third, number = 3 }").load[Example]
    val config2 = ConfigSource.string("{ name = third, number = this_should_be_an_integer }").load[Example]

    //then
    config1.isRight shouldBe isFirstRigth
    config2.isRight shouldBe isSecondRight
  }

  /** A common pattern when loading configs is to read and merge from multiple config sources,
   * maybe you have app-specific and user-specific configs you want to merge in some order,
   * or maybe you want to fall back to some default configuration if a file doesn’t exist or cannot be read.
   * */
  def mergeSources(name: String, number: Int): Unit = {
    val primarySource = ConfigSource.string("{ name = easy }")
    val secondarySource = ConfigSource.string("{ name = difficult, number = 4 }")
    val example = primarySource.withFallback(secondarySource).loadOrThrow[Example]
    example shouldBe Example(name, number)
  }

  /** Sometimes you want some of the sources in your chain to be optional.
   * You can call .optional / alternative source to return a fallBack config if the underlying source cannot be read:
   * */
  def optionalConfig(defaultExample: Example): Unit = {
    //when
    val defaultsSource = ConfigSource.string("{ name = fifth, number = 5 }")
    val nonExistingSource = ConfigSource.file("non-existing-file.conf")

    //then
    nonExistingSource.optional.withFallback(defaultsSource).load[Example] shouldBe Right(defaultExample)
    nonExistingSource.recoverWith { case _ => defaultsSource }.loadOrThrow[Example] shouldBe defaultExample
  }

  
  /* First thing you have to do is to import `pureconfig.generic.auto` and define data types and a case class to hold the configuration.
   * {{{
   *   final case class City(value: Int) extends AnyVal
   *   case class Flat(
   *   isCurrentlyRented: Boolean,
   *   city: Port,
   *   country: MyAdt,
   *   tenantNames: List[Double],
   *   tenantAges: Map[String, Int],
   *   option: Option[String])
   * }}}
   *
   *  to read a config from an `application.conf resource and convert it to a case class.
   *
   */
  def functionAssert(res0: Boolean): Unit = {
    true shouldBe res0
  }
}
