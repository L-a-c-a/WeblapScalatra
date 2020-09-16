package com.github.laca.w

// RemoteWebServer-t használó SeLap

//trait RemSeLapT extends SeLap { }   //hátha kell (nem biztos)

case class HistoriaValasz
( aktAblak: String
, histHossz: Long
, histSorsz: Long
// , ??
)

//import RemSeLap._
import SeRemKliens._
class RemSeLap(override val pURL: String)  extends SeLap(pURL) //with RemSeLapT
{
  //**/println ("kapott url:"+url)  SeLap-ból megcsinálja
  //if (url.isEmpty) dr.get("about:blank") else dr.get(url)
    /**/println ("eddigi ablak azon: " + dr.getWindowHandle)  // B+! Megváltozik!!!
  try { dr.get(url) }
  catch
  {
    case ex:Throwable => 
    {
      println("----WebDriver hiba!----")
      println(ex.getMessage)
    /**/println ("title:"+dr.getTitle)
    /**/println ("url:"+dr.getCurrentUrl)
    /**/println ("hist. hossz: " + histHossz)
      ex.printStackTrace
      //dr.get("about:blank")  //ez biztos jó  ...de nem kell, marad az eddigi lap (ha az a szűz about:blank is)
    }
  }
  //{
    cim = dr.getTitle
    // url = dr.getCurrentUrl //nem var!
    /**/println ("title:"+dr.getTitle)
    //**/println ("lapcím:"+dr.klLapCim)
    //**/println ("capabilities:"+dr.asInstanceOf[org.openqa.selenium.remote.RemoteWebDriver].getCapabilities)
    /**/println ("url:"+dr.getCurrentUrl)
    /**/println ("hist. hossz: " + histHossz)
    /**/println ("új??? ablak azon: " + dr.getWindowHandle)  // B+! Megváltozik!!! a file:///tmp címtől 15-ből 32 lesz!  ...és az isten tudja, mi maradt meg a históriából
    /**/println ("új??? hist. hossz: " + histHossz)   //ez legalább megmaradt - alighanem csak a lapokAblakonkentHistoriaSzerint romlik el
  //}
  /*dr.lapokA... helyett*/ 
  //SeRemKliens.lapokAblakonkentHistoriaSzerint(SeRemKliens.aktAblak) += dr.executeScript("return history.length;").asInstanceOf[Long] -> pill //ez célfüggvényért kiált
  ujHistoriaElem(pill)
  /**/println(SeRemKliens.lapokAblakonkentHistoriaSzerint)
  aktHistoriaSorszam = histHossz
  /**/println("akt. hist. sorsz.: " + aktHistoriaSorszam)

  override def o/*:Serializable*/ = LapValasz(pill.toString, url, cim, html, kep, "se", fuggoSeTip, Some(HistoriaValasz(aktAblak, histHossz, aktHistoriaSorszam)))

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

//object RemSeLap
  ///*extends SeRemKliens*/ {}
/*
{ //nem tudtam eldönteni, hol legyenek; lesznek mind a kettőben
  def dr = SeRemKliens.dr
  def fuggoSeTip = SeRemKliens.fuggoSeTip
  //drClose meg nem ebbe az osztályba kell
  def histHossz = SeRemKliens.histHossz
  def ujHistoriaElem(lap: java.time.Instant) = SeRemKliens.ujHistoriaElem(lap)

  def aktHistoriaSorszam = SeRemKliens.aktHistoriaSorszam
  def aktHistoriaSorszam_=(sz:Long) = SeRemKliens.aktHistoriaSorszam = sz
}
*/