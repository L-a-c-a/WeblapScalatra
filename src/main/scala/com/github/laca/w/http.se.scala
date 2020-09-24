package com.github.laca.w

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver
//import com.machinepublishers.jbrowserdriver.JBrowserDriver;
//import com.machinepublishers.jbrowserdriver.Settings;
import collection.JavaConverters._
import util.chaining._

/**
 * Selenium böngésző
 */
trait SeHttpKliens extends HttpKliens //with JavascriptExecutor with TakesScreenshot with WebDriver
{ 
  // ha az idekerülő implementációk nem használják ezeket a trait-eket, akkor feleslegesek az import-ok és GenSeHttpKliens-ben a def-ek
  def csuk = {}
  def klLapCim = "nincs címe"
}

trait RemSeHttpKliens extends SeHttpKliens //WebDriver with JavascriptExecutor with TakesScreenshot  // with org.openqa.selenium.HasCapabilities
{ 
  // ezt keverik be a RemoteWebDrive-ből származó osztályokból (FirefoxWebDriver, JBrowserDriver, ...) származó saját osztályok
  //def csuk:Unit = {}
}

class GenSeHttpKliens extends SeHttpKliens 
{
// Missing implementations for 17 members. Stub implementations follow:
  // Members declared in org.openqa.selenium.JavascriptExecutor
  def executeAsyncScript(x$1: String, x$2: Object*): Object = ???
  def executeScript(x$1: String, x$2: Object*): Object = ???
  def isJavascriptEnabled(): Boolean = ???
  
  // Members declared in org.openqa.selenium.TakesScreenshot
  def getScreenshotAs[X](x$1: org.openqa.selenium.OutputType[X]): X = ???
  
  // Members declared in org.openqa.selenium.WebDriver
  def close(): Unit = ???
  def findElement(x$1: org.openqa.selenium.By): org.openqa.selenium.WebElement = ???
  def findElements(x$1: org.openqa.selenium.By): java.util.List[org.openqa.selenium.WebElement] = ???
  def get(x$1: String): Unit = ???
  def getCurrentUrl(): String = ???
  def getPageSource(): String = ???
  def getTitle(): String = ???
  def getWindowHandle(): String = ???
  def getWindowHandles(): java.util.Set[String] = ???
  def manage(): org.openqa.selenium.WebDriver.Options = ???
  def navigate(): org.openqa.selenium.WebDriver.Navigation = ???
  def quit(): Unit = ???
  def switchTo(): org.openqa.selenium.WebDriver.TargetLocator = ???
}

/*
class JBrowserSeHttpKliens extends JBrowserDriver/*(Settings.builder().javaExportModules(true).build())*/ with SeHttpKliens   // a JBrowserDriver osztály, ezt kell előre írni
{

} //nem műx se így, se úgy - https://www.bountysource.com/issues/47010283-support-java-9-now-that-it-s-officially-pre-ga
*/

object SeHttpKliens
{ 
  def apply() =
  { /**/println(s"apply SeHttpKliens (${KONFIG.konf.seTip})")
    KONFIG.konf.seTip match
    {
      //case "JBDR" => new JBrowserSeHttpKliens
      case "FFDR" => FFSeHttpKliens()
      case _ => new GenSeHttpKliens
    }
  }
}

case class AblakStatuszValasz
( histHossz: Long
, histSorsz: Long
, lap: LapValasz
)

trait SeRemKliens
{ 
  def apply() =
  { /**/println(s"apply SeRemKliens (${KONFIG.konf.seTip})")
    KONFIG.konf.seTip match
    {
      //case "JBDR" => new JBrowserDriver
      case "FFDR" => new org.openqa.selenium.firefox.FirefoxDriver(FFSeHttpKliens.opt)
      case _ => new org.openqa.selenium.remote.RemoteWebDriver(new org.openqa.selenium.MutableCapabilities)
    }
  }

  private var drOpt: Option[RemoteWebDriver] = None
  def dr: RemoteWebDriver =
  {
    if (drOpt == None)
    { println("dr nyit")
      drOpt = Some(SeRemKliens())
      aktAblak = drOpt.get.getWindowHandle
      /**/println("kezdő ablak azon: " + aktAblak)    // 15 szokott lenni FFDR-nél
      lapokAblakonkentHistoriaSzerint += aktAblak -> collection.mutable.Map.empty
    }
    drOpt.get
  }

  def drClose = 
  {
    drOpt foreach  //nem biztos, hogy nyitva van, pl. ScalatraBootstrap destruktorában
    { dr =>
      {
        dr.quit
        drOpt = None
        Lap.lapok.clear   //de lehet, hogy (al)típusra szűrni kéne... NEM, mert determinálva vannak, csak egyféle lehet benne egyszerre
        lapokAblakonkentHistoriaSzerint.clear
        println("dr csuk")
      }
    }
  }

  def fuggoSeTip = if (drOpt==None) "" else KONFIG.konf.seTip

  def ablakok =
  {
    drOpt map (d => d.getWindowHandles.asScala.map(h => h -> (if (h==d.getWindowHandle) "akt" else ""))) getOrElse Set("nincs" -> "")
  }

  def muv(par: org.scalatra.Params)/*:Serializable*/ =
  {
    par("muv") match
    {
      case "csuk" =>
      {
        drClose
        lapokAblakonkentHistoriaSzerint.clear
        """{"se": "csukva"}"""
      }
      case "kepes" => """{"capabilities": """" + dr.asInstanceOf[org.openqa.selenium.remote.RemoteWebDriver].getCapabilities + """"}"""
      case "ablak" => ablakok  // ()=> Set[String, String], az egész muv ettől (par)=> Object lesz, de simán tud belőle json-t csinálni
      case "ujablak" => 
      { /**/println(s"ujblak elején $lapokAblakonkentHistoriaSzerint ${lapokAblakonkentHistoriaSzerint.keySet} ${dr.getWindowHandles}")
        dr.executeScript("window.open('', '');")  //most pont 1 db lapokAblakonkentHistoriaSzerint-ben nem szereplő ablak-azonosító van
        /**/println(s" ${lapokAblakonkentHistoriaSzerint.keySet} ${dr.getWindowHandles.asScala}")
        val ujAblakAzon = (dr.getWindowHandles.asScala diff lapokAblakonkentHistoriaSzerint.keySet).head  /**/.tap(u=>println(s"ujAblakAzon=$u"))
        ablakValt(ujAblakAzon)
        lapokAblakonkentHistoriaSzerint += ujAblakAzon -> collection.mutable.Map(/*1L -> (new RemSeLap("")).pill*/)
        /**/println(s"...végén $lapokAblakonkentHistoriaSzerint")
        ablakok
      }
      //case "ablakstatusz" => AblakStatuszValasz(histHossz, aktHistoriaSorszam, AblakStatuszValasz(par("abl").toLong)(0L))
      case "ablakstatusz" =>
      ( lapokAblakonkentHistoriaSzerint/**/.tap(println)/**/(par("abl"))   
        .map{case (k,v) => ( k
                           , Map( "url" -> Lap.lapok(v).url
                                , "cim" -> Lap.lapok(v).cim
                                )
                           )
            } //az ablak egész históriája
        + (0L -> ("akt" -> aktHistoriaSorszam)) // a história 1-től sorszámozódik, így 0 alatt átadhatok az egész ablakra/históriára jellemző adatot
      )
      case "navig" => navig(par)
      case "ablakvalt" => { ablakValt(par("abl")); ablakok }
    }
  }

  val lapokAblakonkentHistoriaSzerint: collection.mutable.Map[String, collection.mutable.Map[Long, java.time.Instant]] = collection.mutable.Map.empty
  // ablakAzon -> (históriaSorszám -> lapAzon)
  var aktAblak = ""
  
  def histHossz = dr.executeScript("return history.length;").asInstanceOf[Long]
  def ujHistoriaElem(lap: java.time.Instant) = 
  {
    val ujAktAblak = drOpt.get.getWindowHandle
    if (aktAblak != ujAktAblak)  // LEHET, HOGY MEGVÁLTOZOTT! pl. file:///tmp hatására 15-ről 32-re (FFDR-ben)
    {
      lapokAblakonkentHistoriaSzerint += ujAktAblak -> lapokAblakonkentHistoriaSzerint(aktAblak)
      lapokAblakonkentHistoriaSzerint -= aktAblak
      aktAblak = ujAktAblak
    }
    lapokAblakonkentHistoriaSzerint(aktAblak) += histHossz -> lap
  }

  var aktHistoriaSorszam = 0L

  def navig(par:org.scalatra.Params) = //vissza, frissítés, előre
  {
    val delta = par.get("delta").getOrElse("0").toLongOption.getOrElse(0L) // -1, 0, +1
    dr.executeScript(s"history.go($delta)")
    aktHistoriaSorszam += delta
    //AblakStatuszValasz(histHossz, aktHistoriaSorszam, Lap.lapok(lapokAblakonkentHistoriaSzerint(aktAblak)(aktHistoriaSorszam)).o.asInstanceOf[LapValasz])
    Lap.lapok(lapokAblakonkentHistoriaSzerint(aktAblak)(aktHistoriaSorszam)).o
  }

/*
  def ujAblakAzon = //amit létrehoztunk executeScript(window.open)-nel, de még nincs benne lapokAblakonkentHistoriaSzerint-ben (csak egy van ilyen)
  {
    (dr.getWindowHandles.asScala diff lapokAblakonkentHistoriaSzerint.keySet).head//Option getOrElse ""
  }
*/  //ha nem kell máshová, jó a val az "ujablak" case-funkcióban

  def ablakValt(ujazon: String) =
  {
    dr.switchTo.window(ujazon)
    aktAblak = ujazon
  }

}

object SeRemKliens extends SeRemKliens {}
