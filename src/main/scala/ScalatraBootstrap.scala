import com.github.laca.w._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new MyScalatraServlet, "/*")
  }

  override def destroy(context: ServletContext) =  //NEM MŰX
  {
    println("scalatra életciklus destruálva")
    println("scalatra életciklus destruálva")
    println("scalatra életciklus destruálva")
    println("scalatra életciklus destruálva")
    println("scalatra életciklus destruálva")
    println("scalatra életciklus destruálva")
    println("scalatra életciklus destruálva")
    println("scalatra életciklus destruálva")
    SeRemKliens.drClose
  }


}
