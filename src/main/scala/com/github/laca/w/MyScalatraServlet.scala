package com.github.laca.w

import org.scalatra._

// http://scalatra.org/guides/2.3/formats/json.html
// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.json4s.jackson.JsonMethods._

// JSON handling support from Scalatra
import org.scalatra.json._

class MyScalatraServlet extends ScalatraServlet  with JacksonJsonSupport
{
  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats

  before() 
  {
    contentType = formats("json")
  }
  
  get("/") {
    views.html.hello()
  }

  get("/konf")
  { """{"konf": "érték"}"""}

}
