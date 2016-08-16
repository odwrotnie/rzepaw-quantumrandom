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

  def nextInteger(n: Int = Int.MaxValue, fresh: Boolean = false): Option[Int] = fresh match {
    case true => client.response(1).flatMap(_.data.headOption)
    case false => queue.size match {
      case i if i <= 0 =>
        None
      case i if i < MIN =>
        fill(queue)
        Some(queue.dequeue() % n)
      case _ =>
        Some(queue.dequeue() % n)
    }
  }

  def nextIntegerOrNonquantum(n: Int = Int.MaxValue, fresh: Boolean = false): Int = nextInteger(n, fresh) match {
    case Some(i) => i
    case _ =>
      val nonquantumRandom = Random.nextInt(n)
      logger.warn(s"Nonquantum random number generated - $nonquantumRandom")
      nonquantumRandom
  }

  def one[T](i: Iterable[T]): T = {
    require(i.nonEmpty, "The iterable is empty")
    i.toSeq(nextIntegerOrNonquantum(i.size))
  }
}
