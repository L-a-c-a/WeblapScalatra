package com.github.laca.w

/*trait SeLapT extends Lap
{
  //var/*?*/  dr = SeHttpKliens()  itt nem jó helyen van, ld. object SeLap (és akkor SeLapT fölösleges is)
  //def o:Serializable = Alap(url, cim, html, kep)
}*/

//import SeLap._
class SeLap (val pURL: String)  extends Lap //SeLapT
{ 
  url = pURL
  html = "Laca.png"
  kep = Lap.pngLaca
  cim = "Se Lap"

  /**/println ("kapott url:"+url)
  //**/println ("title:"+dr.getTitle)
  //**/println ("lapcím:"+dr.klLapCim)
  //**/println ("capabilities:"+dr.asInstanceOf[org.openqa.selenium.remote.RemoteWebDriver].getCapabilities)
  //**/println ("url:"+dr.getCurrentUrl)

  override def htmlFrissit = 
  { /**/html = "<p>módosított tartalom</p>"
    this
  }

}

  /*
object SeLap
{
  private var drOpt: Option[AnyRef] = None
  def dr: SeHttpKliens =
  {
    if (drOpt == None) drOpt = Some(SeHttpKliens())
    drOpt.get.asInstanceOf[SeHttpKliens]  //futásidőben derül ki, hogy ezt nem lehet
      // java.lang.ClassCastException: class org.openqa.selenium.firefox.FirefoxDriver cannot be cast to class com.github.laca.w.SeHttpKliens 
      // (org.openqa.selenium.firefox.FirefoxDriver and com.github.laca.w.SeHttpKliens are in unnamed module of loader org.eclipse.jetty.webapp.WebAppClassLoader @3a079870)
  }

  def drClose = 
  {
    dr.quit
    drOpt = None
    println("dr csuk")
  }
  
}  */
