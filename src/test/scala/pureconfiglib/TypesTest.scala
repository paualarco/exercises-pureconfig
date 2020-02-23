package pureconfiglib

import org.scalaexercises.Test
import org.scalatest.FlatSpec
import pureconfiglib.SupportedTypes
import shapeless.HNil

import scala.concurrent.duration._
class TypesTest extends FlatSpec{

  "primitives" should "loadCorrectly" in {
    SupportedTypes.loadPrimitivesConfig("primitive", true, 2.1d, 1.0f, 1, 100L, 1.asInstanceOf[Short], 'p')
  }

  "path config" should "be infered" in {
    SupportedTypes.loadPathsConfig("src/main/resources", "src/main/resources/application.conf", "https://pureconfig.github.io", "https://pureconfig.github.io/docs/index.html")
  }
}
