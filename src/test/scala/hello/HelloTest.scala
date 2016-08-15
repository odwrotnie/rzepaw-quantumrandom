package hello

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class HelloTest
  extends FlatSpec
  with LazyLogging {

  "2 plus 2" should "equal 4" in {
    logger.info("Test")
    assert(2 + 2 == 4)
  }
}
