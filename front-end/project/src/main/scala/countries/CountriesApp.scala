package countries


import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}
import com.thoughtworks.binding.Binding.Vars
import org.scalajs.dom.{Event, Node, document}
import com.thoughtworks.binding.{Binding, FutureBinding,dom}

object CountriesApp extends App {
  dom.render(document.getElementById("countries"), countriesTable)


  private def getCountries: FutureBinding[Seq[Country]] = FutureBinding(CountryService.getCountries("europe"))

  val data = Vars.empty[Country]
  @dom
  def countriesTable: Binding[Node] = {
    getCountries.bind match {
      case Some(Success(s)) =>
        data.value ++= s
        <table class="table table-striped table-hover">
          <thead >
            <th>code</th>
            <th>name</th>
          </thead>
          <tbody>
            {for(c<-data) yield
            <tr>
              <td>{c.code}</td>
              <td>{c.name}</td>

            </tr>

            }
          </tbody>
        </table>
      case Some(Failure(e)) =>
        println(e.toString)
        <div class="alert alert-danger">Sorry something went wrong {e.toString}</div>
      case _ => <div style="margin:15px 0"><i class="fa-3x fas fa-spinner fa-pulse"></i></div>
    }
  }

}
