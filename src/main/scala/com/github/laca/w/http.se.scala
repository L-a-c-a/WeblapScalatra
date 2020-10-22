package com.github.laca.w

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver
//import com.machinepublishers.jbrowserdriver.JBrowserDriver;
//import com.machinepublishers.jbrowserdriver.Settings;
import collection.JavaConverters._
import util.chaining._
import java.time.Instant

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

case class LapAdatok  //ez megy a LapValasz.lapadatok-ba (ami Option[Any])
( linkek: Option[collection.mutable.Buffer[Link]]
, kattintanivalok: Option[Serializable]
, ablakok: Option[SeRemKliens.Ablakok]
)

trait SeRemKliens
{ 
  private var drOpt: Option[RemoteWebDriver] = None
  def dr: RemoteWebDriver =
  {
    if (drOpt == None)
    { println("dr nyit")
      drOpt = Some(SeRemKliens())
      /**/println("drOpt="+drOpt)
      aktAblak = drOpt.get.getWindowHandle
      /**/println("kezdő ablak azon: " + aktAblak)    // 15 szokott lenni FFDR-nél
      lapokAblakonkentHistoriaSzerint += aktAblak -> collection.mutable.Map(0L -> szambolAblakAzon(0L)) //collection.mutable.Map.empty
    }
    drOpt.get
  }

  def drClose = 
  { /**/println("drOpt="+drOpt)
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

  type AblakStatusz = collection.mutable.Map[Long, Map[String,Any]] //azért Any, mert az aktAblakHistoriaSorszam Long
  val uresAblakStatusz:AblakStatusz = collection.mutable.Map.empty

  type Ablakok = collection.mutable.Set[(String, AblakStatusz)]

  def ablakok:Ablakok = //:SeRemKliens.Ablakok =
  {
    drOpt map (d => d.getWindowHandles.asScala.map(h => h -> (if (h==d.getWindowHandle) ablakStatusz(h) else uresAblakStatusz))
              ) getOrElse collection.mutable.Set("nincs" -> uresAblakStatusz)
  }

  def muv(par: org.scalatra.Params)/*:Serializable nem lehet, mert ablakok és ablakStatusz() nem az*/ =
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
      { 
        ujAblak
        ablakok
      }
      //case "ablakstatusz" => AblakStatuszValasz(histHossz, aktHistoriaSorszam, AblakStatuszValasz(par("abl").toLong)(0L))
      case "ablakstatusz" => ablakStatusz(par("abl"))
      case "navig" => navig(par)
      case "ablakvalt" => { ablakValt(par("abl")); ablakok }
    }
  }

  val lapokAblakonkentHistoriaSzerint: collection.mutable.Map[String, collection.mutable.Map[Long, java.time.Instant]] = collection.mutable.Map.empty
  // ablakAzon -> (históriaSorszám -> lapAzon)
  // Map[Long, Instant] helyett lehetne ArrayBuffer[Instant]    ... de akkor ablakStatusz egész másképp nézne ki (honnan szedném a k-t? - zipWithIndex)
  // A 0-s indexben az akt. históriasorszám lesz, mert az ablakfüggő! (méghozzá másodpercben az epochához képest, ha már Instant lett)
  
  private var _aktAblak = ""
  def aktAblak =
  {
    val ujAktAblak = drOpt.get.getWindowHandle
    if (_aktAblak != ujAktAblak)  // LEHET, HOGY MEGVÁLTOZOTT! pl. file:///tmp hatására 15-ről 32-re (FFDR-ben)
    {
      lapokAblakonkentHistoriaSzerint += ujAktAblak -> lapokAblakonkentHistoriaSzerint(_aktAblak)
      lapokAblakonkentHistoriaSzerint -= _aktAblak
      _aktAblak = ujAktAblak
    }
    ujAktAblak
  }
  def aktAblak_=(azon:String) = _aktAblak = azon

  def ujAblak =
  { /**/println(s"ujblak elején $lapokAblakonkentHistoriaSzerint ${lapokAblakonkentHistoriaSzerint.keySet} ${dr.getWindowHandles}")
    dr.executeScript("window.open('', '');")  //most pont 1 db lapokAblakonkentHistoriaSzerint -ben nem szereplő ablak-azonosító van
    /**/println(s" ${lapokAblakonkentHistoriaSzerint.keySet} ${dr.getWindowHandles.asScala}")
    val ujAblakAzon = (dr.getWindowHandles.asScala diff lapokAblakonkentHistoriaSzerint.keySet).head  /**/.tap(u=>println(s"ujAblakAzon=$u"))
    ablakValt(ujAblakAzon)
    lapokAblakonkentHistoriaSzerint += ujAblakAzon -> collection.mutable.Map(0L -> szambolAblakAzon(0L)/*1L -> (new RemSeLap("")).pill*/)
    /**/println(s"...végén $lapokAblakonkentHistoriaSzerint")
  }

  def ablakStatusz(ablAzon:String): SeRemKliens.AblakStatusz =    //mutable.Map[Long, Map[String,Any]]   , azért Any, mert az aktAblakHistoriaSorszam Long
  ( lapokAblakonkentHistoriaSzerint/**/.tap(println)/**/(ablAzon)   
    .map{case (k,v) =>  ( k
                        , if (k==0L)
                            Map ( "akt" -> aktAblakHistoriaSorszam(ablAzon)
                                , "abl" -> aktAblak   //ha változott, ezúton tudatjuk az előoldallal
                                )
                          else
                            Map ( "url" -> Lap.lapok(v).url
                                , "cim" -> Lap.lapok(v).cim
                                )
                        )
        } //az ablak egész históriája
    //+ (0L -> ("akt" -> aktHistoriaSorszam)) // a história 1-től sorszámozódik, így 0 alatt átadhatok az egész ablakra/históriára jellemző adatot
  )
  
  def histHossz = dr.executeScript("return history.length;").asInstanceOf[Long]
  def ujHistoriaElem(lap: java.time.Instant) = 
  {
    lapokAblakonkentHistoriaSzerint(aktAblak) += histHossz -> lap
  }

  //var aktHistoriaSorszam = 0L   NEM JÓ! aktAblak-függőnek kell lennie!
  def szambolAblakAzon(n:Long):Instant = Instant.ofEpochSecond(n)   //ha számot kell betenni ablakazon. helyébe
  implicit def ablakAzonbolSzam(i:Instant) = i.getEpochSecond   //ezt így pipe-'al lehetne használni, vagy implicit lehetne
  def aktAblakHistoriaSorszam(abl:String):Long = lapokAblakonkentHistoriaSzerint(aktAblak)(0L)  //.getEpochSecond - itt működik az implicit
  def aktHistoriaSorszam = aktAblakHistoriaSorszam(aktAblak)
  def aktHistoriaSorszam_=(sorsz:Long) = lapokAblakonkentHistoriaSzerint(aktAblak)(0L) = szambolAblakAzon(sorsz)

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

object SeRemKliens extends SeRemKliens 
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


}
