package com.github.laca.w

import org.scalatra.test.scalatest._

class MyScalatraServletTests extends ScalatraFunSuite {

  addServlet(classOf[MyScalatraServlet], "/*")

  test("GET / on MyScalatraServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

  test("GET /konf")
  {
    get("/konf")
    {
      status should equal (200)
      body should include ("konf")
    }
  }

}
