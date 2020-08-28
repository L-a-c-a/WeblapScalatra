package com.github.laca.w

import org.openqa.selenium.remote.RemoteWebDriver

// RemoteWebServer-t használó SeLap

trait RemSeLapT extends SeLap { }   //hátha kell (nem biztos)

import RemSeLap._
class RemSeLap(override val url: String)  extends SeLap(url) with RemSeLapT
{
  //**/println ("kapott url:"+url)  SeLap-ból megcsinálja
  if (url!="") dr.get(url)
  /**/println ("title:"+dr.getTitle)
  //**/println ("lapcím:"+dr.klLapCim)
  /**/println ("capabilities:"+dr.asInstanceOf[org.openqa.selenium.remote.RemoteWebDriver].getCapabilities)
  /**/println ("url:"+dr.getCurrentUrl)

}

object RemSeLap
{
  private var drOpt: Option[RemoteWebDriver] = None
  def dr: RemoteWebDriver =
  {
    if (drOpt == None)
    { println("dr nyit")
      drOpt = Some(SeRemKliens())
    }
    drOpt.get
  }

  def drClose = 
  {
    dr.quit
    drOpt = None
    println("dr csuk")
  }

}