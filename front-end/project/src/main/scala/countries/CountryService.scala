package countries

import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.window
import org.scalajs.dom.ext.Ajax
import play.api.libs.json.Json

import scala.concurrent.Future

object CountryService extends JsonFormat {

  import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def getCountries(continent: String): Future[Seq[Country]] = {

    val future: Future[XMLHttpRequest] = Ajax.get(s"http://${window.location.host}:9090/countries?continent=${continent}")
    future.map(_.responseText)
      .map { s => Json.parse(s).as[List[Country]] }
  }

}
