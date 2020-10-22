package com.github.laca.w

import collection.JavaConverters._

// RemoteWebServer-t használó SeLap

//trait RemSeLapT extends SeLap { }   //hátha kell (nem biztos)

case class HistoriaValasz
( aktAblak: String
, histHossz: Long
, histSorsz: Long
// , ??
)

case class Link
( absHref: String
, ujLapra: Boolean    // elem.getAttribute("target") == "_blank"  vagy vmi ilyesmi
, szoveg: String
)

import SeRemKliens._   // helyett RemSeLap._ , de belül   --az meg nem műx, dr None lesz
class RemSeLap(override val pURL: String)  extends SeLap(pURL) //with RemSeLapT
{
  //**/println ("kapott url:"+url)  SeLap-ból megcsinálja
  //if (url.isEmpty) dr.get("about:blank") else dr.get(url)
  //import RemSeLap._   // belül kell lennie, mert különben unrecoverable cycle resolving import   --nem műx, dr None lesz
  println ("ablak azon: " + dr.getWindowHandle)  // megnyitja a böngészőt, ha még nincs nyitva, és akkor létrehoz egy lapokAblakonkentHistoriaSzerint bejegyzést is
  if (url.isEmpty)
    println("üres cím, nem történik semmi más")
  else
  { try { dr.get(url) }
    catch
    { case ex:Throwable => 
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
      url = dr.getCurrentUrl //nem var! - de.
      /**/println ("title:"+dr.getTitle)
      //**/println ("lapcím:"+dr.klLapCim)
      //**/println ("capabilities:"+dr.asInstanceOf[org.openqa.selenium.remote.RemoteWebDriver].getCapabilities)
      /**/println ("url:"+url)
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
  }

  var linkek: collection.mutable.Buffer[Link] = collection.mutable.Buffer()
  var kattintanivalok: Any = _

  //override def o/*:Serializable*/ = LapValasz(pill.toString, url, cim, html, kep, "se", fuggoSeTip, Some(HistoriaValasz(aktAblak, histHossz, aktHistoriaSorszam)))
  override def o = LapValasz(pill.toString, url, cim, html, kep, "se", fuggoSeTip, Some(LapAdatok(Some(linkek), None, Some(ablakok))))

  override def htmlFrissit = 
  {
    html = dr.getPageSource
    this
  }

  override def kepFrissit(par: Option[String]=None) =
  {
    par match
    {
      case Some("fel") => dr.executeScript("window.scrollBy(0,-600)", "");
      case Some("le") => dr.executeScript("window.scrollBy(0,600)", ""); // a kép magassága alapból 694
      case _ =>
    }
    kep = Kep("png", "base64", dr.getScreenshotAs(org.openqa.selenium.OutputType.BASE64))
    this
  }

  override def linkekFrissit =
  {
    linkek =
      dr.findElements(org.openqa.selenium.By.cssSelector("a[href]")).asScala
      .map(elem =>
            Link( elem.getAttribute("href")
                , false
                , elem.getText
                )
          )
    /**/println(linkek)
    this
  }

  override def kattintanivalokFrissit =
  {
    this
  }
}

object RemSeLap
  extends SeRemKliens {}
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