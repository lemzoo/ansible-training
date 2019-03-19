package countries

import play.api.libs.json.Json
//noinspection TypeAnnotation
trait JsonFormat {
  implicit val CountryFormat = Json.format[Country]
}
