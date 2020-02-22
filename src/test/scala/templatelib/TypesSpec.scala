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

package templatelib

import org.scalacheck.ScalacheckShapeless._
import org.scalaexercises.Test
import org.scalatest.refspec.RefSpec
import org.scalatestplus.scalacheck.Checkers
import pureconfig.ConfigSource
import pureconfiglib.SupportedTypes
import shapeless.HNil

import scala.concurrent.duration._

class TypesSpec extends RefSpec with Checkers {

  def `check primitive types` = {
    check(Test.testSuccess(SupportedTypes.loadPrimitivesConfig _, "primitive" :: true :: 2.1d :: 1.0f :: 1 :: 100L :: 1.asInstanceOf[Short] :: 'p' :: HNil))
  }

  def `check paths config` = {
    check(Test.testSuccess(SupportedTypes.loadPathsConfig _, "src/main/resources":: "src/main/resources/application.conf" :: "https://pureconfig.github.io" :: "https://pureconfig.github.io/docs/index.html" :: HNil))
  }

  def `check duration config` = {
    check(Test.testSuccess(SupportedTypes.loadDurationConfig _, 510 :: 1 :: HNil))
  }

  def `check time config` = {
    check(Test.testSuccess(SupportedTypes.loadTimeConfig _,  2 :: 2020 :: 29 :: 13 :: 21 :: 30 :: HNil))
  }

}