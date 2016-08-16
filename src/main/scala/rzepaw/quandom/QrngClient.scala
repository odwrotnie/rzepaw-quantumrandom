package rzepaw.quandom

import java.io.{InputStreamReader, InputStream, BufferedReader, Reader}
import java.net.URL
import java.nio.charset.Charset
import com.typesafe.scalalogging.LazyLogging
import rzepaw.quandom.QrngClient._

import scala.util.parsing.json.JSON

case class QrngClient(tpe: Request = QrngClient.UINT16)
  extends LazyLogging {

  logger.debug(s"QRNG Client URL: $HOST/$PATH")

  private def readAll(rd: Reader): String = {
    val bufferedReader = new BufferedReader(rd)
    val builder: StringBuilder = new StringBuilder()
    Stream.continually(bufferedReader.readLine) takeWhile(_ != null) mkString
  }

  private def readJsonFromUrl(length: Int): Option[QrngResponse] = try {
    val is: InputStream = new URL(tpe.url(length)).openStream()
    try {
      val rd: BufferedReader = new BufferedReader(new InputStreamReader(is, Charset.forName(CHARSET)))
      val jsonString: String = readAll(rd)
      logger.debug(s"HTTP Response: $jsonString")
      tpe match {
        case QrngClient.UINT16 => QrngResponse.parseUInt16(jsonString)
        case QrngClient.HEX16_1 => QrngResponse.parseHex16(jsonString)
        case QrngClient.HEX16_2 => QrngResponse.parseHex16(jsonString)
        case QrngClient.HEX16_3 => QrngResponse.parseHex16(jsonString)
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

  val HOST = "https://qrng.anu.edu.au"
  val PATH = "API/jsonI.php"
  val CHARSET = "UTF-8"

  case class Request(tpe: String, maxValue: Int, blockSize: Int = 1) {
    def url(length: Int) = s"$HOST/$PATH?length=$length&type=$tpe&size=$blockSize"
  }

  val UINT16 = Request("uint16", 65535, 1)
  val HEX16_1 = Request("hex16", Integer.parseInt("ff" * 1, 16), 1)
  val HEX16_2 = Request("hex16", Integer.parseInt("ff" * 2, 16), 2)
  val HEX16_3 = Request("hex16", Integer.parseInt("ff" * 3, 16), 3)
}
