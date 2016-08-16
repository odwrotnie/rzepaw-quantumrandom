package hello

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec
import rzepaw.quandom.QuantumRandom

class QuantumRandomTest
  extends FlatSpec
  with LazyLogging {

  "This" should "work" in {
    (1 to 1000) foreach { i =>
      val r = QuantumRandom.nextIntegerOrNonquantum()
      logger.info(s"Random number ($i): $r")
    }
  }

  "Min max" should "work" in {
    val rands = (1 to 1000) map(_ => QuantumRandom.nextIntegerOrNonquantum())
    logger.info("Min: " + rands.min)
    logger.info("Max: " + rands.max)
  }
}
