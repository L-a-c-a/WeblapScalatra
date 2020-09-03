package com.github.laca.w

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
{ //nem tudtam eldönteni, hol legyenek; lesznek mind a kettőben
  def dr = SeRemKliens.dr
  def fuggoSeTip = SeRemKliens.fuggoSeTip
  //drClose meg nem ebbe az osztályba kell
}