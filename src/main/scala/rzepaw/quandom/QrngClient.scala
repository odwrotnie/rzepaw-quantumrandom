package rzepaw.quandom

import java.io.{InputStreamReader, InputStream, BufferedReader, Reader}
import java.net.URL
import java.nio.charset.Charset
import com.typesafe.scalalogging.LazyLogging
import rzepaw.quandom.QrngClient.TpeMax

import scala.util.parsing.json.JSON

/**
  * https://qrng.anu.edu.au/API/api-demo.php
  *
  * @param length
  */
case class QrngClient(length: Int, tpe: TpeMax = QrngClient.UINT16)
  extends LazyLogging {

  val URL = s"https://qrng.anu.edu.au/API/jsonI.php?length=$length&type=${ tpe.name }"
  val CHARSET = "UTF-8"
  logger.debug(s"QRNG Client URL: $URL")

  private def readAll(rd: Reader): String = {
    val bufferedReader = new BufferedReader(rd)
    val builder: StringBuilder = new StringBuilder()
    Stream.continually(bufferedReader.readLine) takeWhile(_ != null) mkString
  }

  private def readJsonFromUrl(url: String): Option[QrngResponse] = try {
    val is: InputStream = new URL(url).openStream()
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
      logger.error(s"Uknown host: $URL")
      None
  }

  def response: Option[QrngResponse] = readJsonFromUrl(URL)
}

object QrngClient {
  case class TpeMax(name: String, max: Int)
  val UINT16 = TpeMax("uint16", 65535)
}
