package pureconfiglib

import pureconfig.ConfigSource

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
  
}
