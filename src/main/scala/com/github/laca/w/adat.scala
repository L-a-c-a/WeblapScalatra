package com.github.laca.w

import org.scalatra._

case class Alap   //ami minden lapnak van
( url: String
, cim: String
)

//case class Moricka (a: Alap)

trait Lap
{
  //var o: Alap
  var o: Serializable  // o -t és o_= -t kell újradefiniálni a leszármazottban

  //def feldolg = Map("url" -> a.url, "cim" -> a.cim)
}

case class AlapTipussal
( a: Alap
, tip: String
)
// case class nem túl jó a célra, mert immutábilis, és nehézkes új értéket adni t.a.url -nak

//class Moricka (alap: Alap, tip: String) extends Lap
class Moricka (var _o: AlapTipussal) extends Lap
{
  //override var a = alap
  //val t = tip
  //override def feldolg = super.feldolg ++ Map("tip" -> t)
  //override var o:AlapTipussal = AlapTipussal(alap, tip)   // o, mint objektum
  def o_= (op:Serializable):Unit = { o = op }
  def o = _o
}

object Lap
{
  /*
  def apply(a: Alap, tip: String): Lap =
  {
    tip match
    {
      //**/case _ => new Moricka(Alap("http://localhost", "Móricka"), tip)//.asInstanceOf[Lap]
      /**/case _ => new Moricka(a, tip)
    }
  }

  def apply(par: Params): Lap =
  {
    apply(Alap(par("url"), "még üres cím"), par("tip"))
  }
  */
  def apply (par: Params): Lap =
  {
    par("tip") match
    {
      /**/case _ => new Moricka(AlapTipussal(Alap(par("url"), "még üres cím"), par("tip")))
    }
  }
}