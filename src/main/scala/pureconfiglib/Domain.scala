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

  //`String`, `Boolean`, `Double`, `Float`, `Int`, `Long`, `Short`, `Char`.
  case class PrimitivesConf(string: String, bool: Boolean, double: Double, float: Float, int: Int, long: Long, short: Short, char: Char)

  //`URL`, `URI`,`java.util.UUID`,`java.nio.file.Path`, `java.io.File`,`java.util.regex.Pattern`, `scala.util.matching.Regex`
  case class PathConfig(path: java.nio.file.Path, file: java.io.File, url: URL, uri: URI)

  case class DurationConfig(duration: Duration, finiteDuration: FiniteDuration)
  case class TimeConfig(localDate: LocalDate, localDateTime: LocalDateTime)

}
