package rzepaw.quandom

import com.typesafe.scalalogging.LazyLogging

import scala.util.Random

object QuantumRandom
  extends LazyLogging {

  val client = QrngClient(1, QrngClient.UINT16)

  def nextInteger: Option[Int] = client.response.flatMap(_.data.headOption)
  def nextIntegerOrNonquantum: Int = nextInteger match {
    case Some(i) => i
    case _ =>
      val nonquantumRandom = Random.nextInt(client.tpe.max + 1)
      logger.warn(s"Nonquantum random number generated - $nonquantumRandom")
      nonquantumRandom
  }
}
