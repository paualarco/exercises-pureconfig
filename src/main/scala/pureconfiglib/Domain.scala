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
import java.time._

import pureconfig.ConfigSource

import scala.concurrent.duration.{Duration, FiniteDuration}

object Domain {

  final case class TenantInfo(value: Int) extends AnyVal

  case class Example(name: String, number: Int)

  final case class OtherStuff(pool: Boolean, gym: Boolean)

  case class Flat(
      isCurrentlyRented: Boolean,
      number: Int,
      street: String,
      pets: List[Double],
      tenants: Map[String, TenantInfo],
      mayBe: Option[String])

  val multiExampleSource = ConfigSource.string("""
    example-a: {
        name: a
        number: 6
    }
    example-b: {
        name: b
        number: 7
    }
""")

  case class PrimitivesConf(
      string: String,
      bool: Boolean,
      double: Double,
      float: Float,
      int: Int,
      long: Long,
      short: Short,
      char: Char)

  case class OptionConfig(optionA: Option[String], optionB: Option[String], optionC: Option[Int])

  case class CollectionsConfig(list: List[Char], set: Set[Int], map: Map[Int, String])

  case class TimeConfig(localDate: LocalDate, localDateTime: LocalDateTime)

  case class DurationConfig(duration: Duration, finiteDuration: FiniteDuration)

  case class PathConfig(path: java.nio.file.Path, file: java.io.File, url: URL, uri: URI)

  case class ApplicationConfig(
      primitivesConf: Option[PrimitivesConf],
      optionConfig: Option[OptionConfig],
      collectionsConfig: Option[CollectionsConfig],
      timeConfig: Option[TimeConfig],
      durationConfig: Option[DurationConfig],
      pathConfig: Option[PathConfig])
}
