package rzepaw.quandom

import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable
import scala.util.Random

object QuantumRandom
  extends LazyLogging {

  private val client = QrngClient(QrngClient.HEX16_3)

  private val MIN = 100
  private val MAX = 1000
  private lazy val queue: mutable.Queue[Int] = {
    val q = new mutable.Queue[Int]
    fill(q)
    q
  }
  private def fill(q: mutable.Queue[Int]): Unit = client.response(MAX).map(_.data) foreach { list =>
    q ++= list
  }

  def nextInteger(fresh: Boolean = false): Option[Int] = fresh match {
    case true => client.response(1).flatMap(_.data.headOption)
    case false => queue.size match {
      case i if i <= 0 =>
        None
      case i if i < MIN =>
        fill(queue)
        Some(queue.dequeue())
      case _ =>
        Some(queue.dequeue())
    }
  }

  def nextIntegerOrNonquantum(fresh: Boolean = false): Int = nextInteger(fresh) match {
    case Some(i) => i
    case _ =>
      val nonquantumRandom = Random.nextInt(client.tpe.maxValue + 1)
      logger.warn(s"Nonquantum random number generated - $nonquantumRandom")
      nonquantumRandom
  }
}
