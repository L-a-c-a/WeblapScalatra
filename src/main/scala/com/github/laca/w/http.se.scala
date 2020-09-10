package com.github.laca.w

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver
//import com.machinepublishers.jbrowserdriver.JBrowserDriver;
//import com.machinepublishers.jbrowserdriver.Settings;
import collection.JavaConverters._

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

object SeRemKliens
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
    dr.quit
    drOpt = None
    Lap.lapok.clear   //de lehet, hogy (al)típusra szűrni kéne... NEM, mert determinálva vannak, csak egyféle lehet benne egyszerre
    lapokAblakonkentHistoriaSzerint.clear
    println("dr csuk")
  }

  def fuggoSeTip = if (drOpt==None) "" else KONFIG.konf.seTip

  def ablakok =
  {
    dr.getWindowHandles.asScala.map(h => h -> (if (h==dr.getWindowHandle) "akt" else ""))
  }

  def muv(par: org.scalatra.Params)/*:Serializable*/ =
  {
    par("muv") match
    {
      case "csuk" =>
      {
        drClose
        """{"se": "csukva"}"""
      }
      case "kepes" => """{"capabilities": """" + dr.asInstanceOf[org.openqa.selenium.remote.RemoteWebDriver].getCapabilities + """"}"""
      case "ablak" => ablakok  // ()=> Set[String, String], az egész muv ettől (par)=> Object lesz, de simán tud belőle json-t csinálni
      case "ujablak" => 
      {
        dr.executeScript("window.open('', '');")
        ablakok
      }
      //case "ablakstatusz" => AblakStatuszValasz(histHossz, aktHistoriaSorszam, AblakStatuszValasz(par("abl").toLong)(0L))
      case "ablakstatusz" => lapokAblakonkentHistoriaSzerint(par("abl"))  //az ablak egész históriája
    }
  }

  val lapokAblakonkentHistoriaSzerint: collection.mutable.Map[String, collection.mutable.Map[Long, java.time.Instant]] = collection.mutable.Map.empty
  // ablakAzon -> (históriaSorszám -> lapAzon)
  var aktAblak = ""
  
  def histHossz = dr.executeScript("return history.length;").asInstanceOf[Long]
  def ujHistoriaElem(lap: java.time.Instant) = lapokAblakonkentHistoriaSzerint(aktAblak) += histHossz -> lap

  var aktHistoriaSorszam = 0L


}
