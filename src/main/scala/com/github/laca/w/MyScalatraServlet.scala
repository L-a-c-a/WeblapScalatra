package com.github.laca.w

//import util.chaining._  //tap miatt  (sorrend nem mindegy, mert az org.scalatra -nak is van .util -ja - vagy scala.util....-t kell írni)
import org.scalatra._

// http://scalatra.org/guides/2.3/formats/json.html
// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.json4s.jackson.JsonMethods._

// JSON handling support from Scalatra
import org.scalatra.json._

class MyScalatraServlet extends ScalatraServlet  with JacksonJsonSupport with CorsSupport
{
  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats

  before() 
  {
    contentType = formats("json")
    //response.setHeader("Access-Control-Allow-Origin", "http://localhost:4201");
    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

    println (s"----------${java.time.Instant.now}----------")
    //**/println(request.getHeader("Access-Control-Request-Headers"))   //null
    /**/println(request.getHeader("Origin"))
    println (multiParams)
    println (request.getRequestURL +"?"+ request.getQueryString)
  }
  
  get("/") {
    views.html.hello()
  }

  get("/konf")
  { """{"konf": "érték"}"""}

  get("/lap")
  {
    /**/ println(s"url=${params("url")} tip=${params("tip")}")
    Lap(params)/*.tap(_.cim = "ez a címe")*/.o
    //  tap nélkül:
    //val l = Lap(params)
    //l.cim = "ez a címe"
    //l.o
  }

}
