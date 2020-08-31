package com.github.laca.w

import org.openqa.selenium.remote.RemoteWebDriver

// RemoteWebServer-t használó SeLap

//trait RemSeLapT extends SeLap { }   //hátha kell (nem biztos)

import RemSeLap._
class RemSeLap(override val pURL: String)  extends SeLap(pURL) //with RemSeLapT
{
  //**/println ("kapott url:"+url)  SeLap-ból megcsinálja
  if (url!="")
  { dr.get(url)
    cim = dr.getTitle
    // url = dr.getCurrentUrl //nem var!
    /**/println ("title:"+dr.getTitle)
    //**/println ("lapcím:"+dr.klLapCim)
    /**/println ("capabilities:"+dr.asInstanceOf[org.openqa.selenium.remote.RemoteWebDriver].getCapabilities)
    /**/println ("url:"+dr.getCurrentUrl)
  }

  override def o:Serializable = ValaszObj(pill.toString, url, cim, html, kep, "se", fuggoSeTip)

  override def htmlFrissit = 
  {
    html = dr.getPageSource
    this
  }

  override def kepFrissit =
  {
    kep = Kep("png", "base64", dr.getScreenshotAs(org.openqa.selenium.OutputType.BASE64))
    this
  }

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

  def fuggoSeTip = if (drOpt==None) "" else KONFIG.konf.seTip

}