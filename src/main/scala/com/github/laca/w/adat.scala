package com.github.laca.w

import org.scalatra._

case class Alap   //ami minden lapnak van
( url: String
, cim: String
)

case class AlapTipussal
( a: Alap
, tip: String
)

trait Lap
{
  val url: String
  var cim: String  = "még üres cím" // ha nem implementálom itt, cim -et és cim_= -t újra kell definiálni a leszármazottban (ha muszáj var-nak lennie) (de az nem működött)
  def o:Serializable = Alap(url, cim)
}

class Moricka (val url: String, tip: String) extends Lap
//class Moricka (var _o: AlapTipussal) extends Lap
{
  cim = "Móricka"
  override def o:Serializable = AlapTipussal(Alap(url, cim), tip)
}

object Lap
{
  def apply (par: Params): Lap =
  {
    par("tip") match
    {
      /**/case _ => new Moricka(par("url"), par("tip"))
    }
  }
}