package com.github.laca.w

trait HttpKliens
{

}

class NullHttpKliens extends HttpKliens { }

object HttpKliens
{
  //nem ide kell: implicit val setip = KONFIG.konf.seTip
  def apply() =   //tip: selenium, jsoup, selenide, ...
  { KONFIG.konf.lapTip match
    {
      case "se" => SeHttpKliens
      case _ => new NullHttpKliens
    }
  }
}