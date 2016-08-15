package hello

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec
import rzepaw.quandom.QuantumRandom

class QuantumRandomTest
  extends FlatSpec
  with LazyLogging {

  "This" should "work" in {
    (1 to 10) foreach { i =>
      val r = QuantumRandom.nextIntegerOrNonquantum()
      logger.info(s"Random number ($i): $r")
    }
  }
}
