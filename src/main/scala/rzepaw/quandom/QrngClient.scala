package rzepaw.quandom

import java.io.{InputStreamReader, InputStream, BufferedReader, Reader}
import java.net.URL
import java.nio.charset.Charset
import com.typesafe.scalalogging.LazyLogging
import rzepaw.quandom.QrngClient.TpeMax

import scala.util.parsing.json.JSON

case class QrngClient(tpe: TpeMax = QrngClient.UINT16)
  extends LazyLogging {

  val HOST = "https://qrng.anu.edu.au"
  val PATH = "API/jsonI.php"
  def url(length: Int) = s"$HOST/$PATH?length=$length&type=${ tpe.name }"
  val CHARSET = "UTF-8"
  logger.debug(s"QRNG Client URL: $HOST/$PATH")

  private def readAll(rd: Reader): String = {
    val bufferedReader = new BufferedReader(rd)
    val builder: StringBuilder = new StringBuilder()
    Stream.continually(bufferedReader.readLine) takeWhile(_ != null) mkString
  }

  private def readJsonFromUrl(length: Int): Option[QrngResponse] = try {
    val is: InputStream = new URL(url(length)).openStream()
    try {
      val rd: BufferedReader = new BufferedReader(new InputStreamReader(is, Charset.forName(CHARSET)))
      val jsonString: String = readAll(rd)
      logger.debug(s"HTTP Response: $jsonString")
      tpe match {
        case QrngClient.UINT16 => QrngResponse.parseUInt16(jsonString)
      }
    } finally {
      is.close()
    }
  } catch {
    case e: java.net.UnknownHostException =>
      logger.error(s"Uknown host: $HOST")
      None
  }

  def response(length: Int): Option[QrngResponse] = readJsonFromUrl(length)
}

object QrngClient {
  case class TpeMax(name: String, max: Int)
  val UINT16 = TpeMax("uint16", 65535)
}
