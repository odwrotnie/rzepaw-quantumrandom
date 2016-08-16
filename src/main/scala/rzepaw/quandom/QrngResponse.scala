package rzepaw.quandom

import com.typesafe.scalalogging.LazyLogging

import scala.util.parsing.json.JSON

case class QrngResponse(tpe: String, length: Int, data: List[Int], success: Boolean)

object QrngResponse
  extends LazyLogging {

  class CC[T] { def unapply(a:Any):Option[T] = Some(a.asInstanceOf[T]) }

  object M extends CC[Map[String, Any]]
  object LD extends CC[List[Double]]
  object LS extends CC[List[String]]
  object S extends CC[String]
  object D extends CC[Double]
  object B extends CC[Boolean]

  def parseUInt16(jsonString: String): Option[QrngResponse] = try {
    for {
      M(map) <- JSON.parseFull(jsonString)
      B(success) = map("success")
      S(tpe) = map("type")
      D(length) = map("length")
      LD(data) = map("data")
    } yield QrngResponse(tpe, length.toInt, data.map(_.toInt), success)
  } catch {
    case e: java.util.NoSuchElementException =>
      logger.error(s"Parsing error: ${ e.getMessage }")
      None
  }

  def parseHex16(jsonString: String): Option[QrngResponse] = try {
    for {
      M(map) <- JSON.parseFull(jsonString)
      B(success) = map("success")
      S(tpe) = map("type")
      D(length) = map("length")
      LS(data) = map("data")
    } yield QrngResponse(tpe, length.toInt, data.map(h => Integer.parseInt(h, 16)), success)
  } catch {
    case e: java.util.NoSuchElementException =>
      logger.error(s"Parsing error: ${ e.getMessage }")
      None
  }
}
