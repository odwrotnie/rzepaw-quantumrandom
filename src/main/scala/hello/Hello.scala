package hello

import com.typesafe.scalalogging.LazyLogging
import rzepaw.quandom.QuantumRandom

object Hi
  extends LazyLogging {

  def main(args: Array[String]) = {
    logger.info(s"Random: " + QuantumRandom.nextIntegerOrNonquantum)
  }
}
