package com.github.laca.w

//import org.scalatra._    egyért (Params) nem importálunk

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

trait Lap
{
  val url: String
  var cim: String  = "még üres cím" // ha nem implementálom itt, cim -et és cim_= -t újra kell definiálni a leszármazottban (ha muszáj var-nak lennie) (de az nem működött)
  var html: String = "<p>még üres tartalom</p>"
  var kep = Kep("png", "base64", "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==")
  def o:Serializable = Alap(url, cim, html, kep)
}

class UresLap (val url: String, tip: String) extends Lap { }

class Moricka (val url: String, tip: String) extends Lap
//class Moricka (var _o: AlapTipussal) extends Lap
{
  cim = "Móricka"
  override def o:Serializable = AlapTipussal(Alap(url, cim, html, kep), tip)
}

object Lap
{
  def apply (par: org.scalatra.Params): Lap =
  {
    //*nagyon ideiglenesen:*/if (par("tip") != "se") SeLap.drClose
    par("tip") match
    {
      //case "se" => new SeLap(par("url"))
      case "se" => new RemSeLap(par("url"))  //seTip-től függő még egy elágazás?!
      case "lap" => new Moricka(par("url"), par("tip"))
      case _ => new UresLap(par("url"), par("tip"))
    }
  }
}

//case class KonfSeTip ()
case class Konf
( lapTip: String
, seTip: String // vagy KonfSeTip  // FFDR (Firefox), HUDR (HtmlUnit), PHDR (Phantom), CHRDR, JBDR (jBrowserDriver), ...
)

object KONFIG
{
  lazy val konf =
    // fájlból be: ld. másik projekt adatosztályok.scala
    //Konf("se", "JBDR")  // a lapTip így tűl van határozva, mert paraméterben is átjön - esetleg innen a default-ot kapja a front
    Konf("se", "FFDR")
}
