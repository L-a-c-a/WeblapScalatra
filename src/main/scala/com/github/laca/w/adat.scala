package com.github.laca.w

//import org.scalatra._    egyért (Params) nem importálunk
import java.time.Instant

case class Alap   //ami minden lapnak van
( url: String
, cim: String
, html: String
, kep: Kep
)
case class Kep
( tip: String  //png, jpg, ...
, kodolas: String  // base64, ...
, tartalom: String
)

case class AlapTipussal
( a: Alap
, tip: String
)
// ezt az önszívatást abbafejezem - minden lapos lesz, kivéve Kép

case class LapValasz
( azon: String
, url: String
, cim: String
, html: String
, kep: Kep
, tip: String
, altip: String  //pl. seTip
, lapadatok: Option[Any]  //pl. Se-nél ablakinfók
)

trait Lap
{
  var url: String = ""
  var cim: String  = "még üres cím" // ha nem implementálom itt, cim -et és cim_= -t újra kell definiálni a leszármazottban (ha muszáj var-nak lennie) (de az nem működött)
  var html: String = "<p>még üres tartalom</p>"
  var kep = Lap.pirosPotty
  def o:Serializable = Alap(url, cim, html, kep)

  val pill = Instant.now

  Lap.lapok += pill -> this
  /**/println("lapok: " + Lap.lapok)

  def htmlFrissit = this
  def kepFrissit = this
  def linkekFrissit = this
  def kattintanivalokFrissit = this
}

//class UresLap (val pURL: String, tip: String) extends Lap { url = pURL }

class Moricka (val pURL: String, tip: String) extends Lap
//class Moricka (var _o: AlapTipussal) extends Lap
{
  cim = "Móricka"
  url = pURL
  //override def o:Serializable = AlapTipussal(Alap(url, cim, html, kep), tip)
  override def o:Serializable = LapValasz(pill.toString, url, cim, html, kep, tip, "", None)

  override def htmlFrissit = { html = "<p>még üres(?) tartalom</p>"; this}
}

object Lap
{
  def apply (par: org.scalatra.Params): Lap = apply(par("tip"), par("url"))
  def apply (tip: String, url:String/* = "about:blank"*/): Lap =            //itt még nem tudni, hogy az about:blank -nak van-e értelme
  {
    tip match
    {
      //case "se" => new SeLap(par("url"))
      case "se" => new RemSeLap(url)  //seTip-től függő még egy elágazás?! vagy egy apply a RemSeLap objektumba?
      //case "lap" => new Moricka(par("url"), par("tip"))
      case _ => new Moricka(url, tip)
    }
  }

  val lapok: collection.mutable.Map[java.time.Instant, Lap] = collection.mutable.Map.empty

  def muv (par: org.scalatra.Params):Lap =
  {
    val pill = Instant.parse(par("azon"))
    val lap:Lap = lapok(pill)
    //művelet a lapon
    //**/lap.html = "<p>Módosított forrás!</p>"
    par("frissitendo") match
    {
      case "html" => lap.htmlFrissit
      case "kep" => lap.kepFrissit
      case "linkek" => lap.linkekFrissit
      case "kattintanivalok" => lap.kattintanivalokFrissit
      case _ => lap
    }
    
    //lap
  }

  val pirosPotty = Kep("png", "base64", "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==")

  val pngLaca = Kep("png", "base64", """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
+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""")

}

case class Konf
( lapTip: String
, seTip: String    // FFDR (Firefox), HUDR (HtmlUnit), PHDR (Phantom), CHRDR, JBDR (jBrowserDriver), ...
)

object KONFIG
{
  lazy val konf =
    // fájlból be: ld. másik projekt adatosztályok.scala
    //Konf("se", "JBDR")  // a lapTip így tűl van határozva, mert paraméterben is átjön - esetleg innen a default-ot kapja a front
    Konf("se", "FFDR")
}
