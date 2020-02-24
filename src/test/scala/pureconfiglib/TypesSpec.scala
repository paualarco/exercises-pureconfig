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

import org.scalacheck.ScalacheckShapeless._
import org.scalaexercises.Test
import org.scalatestplus.scalacheck.Checkers
import org.scalatest.refspec.RefSpec
import shapeless.HNil
import scala.concurrent.duration._
import scala.language.postfixOps

class TypesSpec extends RefSpec with Checkers {

  def `load primitive types` = {
    check(
      Test.testSuccess(
        SupportedTypes.loadPrimitivesConfig _,
        "primitive" :: true :: 2.1d :: 1.0f :: 1 :: 100L :: 1
          .asInstanceOf[Short] :: 'p' :: HNil))
  }

  def `load optional config` = {
    check(
      Test.testSuccess(
        SupportedTypes.loadOptionalConfig _,
        Option("PureOption") :: None :: Option(101) :: HNil))
  }

  def `load collections config` = {
    check(
      Test.testSuccess(
        SupportedTypes.loadCollectionsConfig _,
        List('a', 'e', 'i', 'o', 'u') :: Set(1, 2, 3, 4, 5) :: Map(
          1 -> "A",
          2 -> "E",
          3 -> "I",
          4 -> "O",
          5 -> "U") :: HNil))
  }

  def `load time config` = {
    check(
      Test.testSuccess(SupportedTypes.loadTimeConfig _, 2 :: 2020 :: 29 :: 13 :: 21 :: 30 :: HNil))
  }

  def `load duration config` =
    check(Test.testSuccess(SupportedTypes.loadDurationConfig _, (20 minutes) :: HNil))

  def `load paths config` = {
    check(
      Test.testSuccess(
        SupportedTypes.loadPathsConfig _,
        "src/main/resources" :: "src/main/resources/application.conf" :: "https://pureconfig.github.io" :: "https://pureconfig.github.io/docs/index.html" :: HNil
      ))
  }

  def `load application config` = {
    check(Test.testSuccess(
      SupportedTypes.loadApplicationConfig _,
      None :: Option("present") :: None :: List[String]() :: Set[String]() :: Map[Int, String]() :: HNil
    ))
  }

}
