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
import org.scalatest.refspec.RefSpec
import org.scalatestplus.scalacheck.Checkers
import pureconfiglib.Domain.Example
import shapeless.HNil

import scala.concurrent.Future

class LoadingConfigSpec extends RefSpec with Checkers {

  def `loads values using loadOrThrow` =
    check(Test.testSuccess(LoadingConfig.loadOrThrowExample _, "first" :: 1 :: HNil))

  def `checks if loadOrThrow config is failed` =
    check(Test.testSuccess(LoadingConfig.loadOrThrowFailure _, true :: HNil))

  def `checks if loaded config returned Right or Left` =
    check(Test.testSuccess(LoadingConfig.loadExample _, true :: false :: HNil))

  def `checks merged source is overwritten` =
    check(Test.testSuccess(LoadingConfig.mergeSources _, "easy" :: 4 :: HNil))

  def `checks that optional sources falls back to default one` =
    check(Test.testSuccess(LoadingConfig.optionalConfig _, Example("fifth", 5) :: HNil))

  def `checks the a config can be chosen when having multiple sources` =
    check(Test.testSuccess(LoadingConfig.multipleSources _, Example("b", 7) :: HNil))

}
