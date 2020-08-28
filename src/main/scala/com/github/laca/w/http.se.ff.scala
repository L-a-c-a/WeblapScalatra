package com.github.laca.w

//import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//    import util.chaining._

class FFSeHttpKliens(o: FirefoxOptions) extends FirefoxDriver(o) with RemSeHttpKliens
{
  //super(FFSeHttpKliens.ffopt)
  //val ffo = (new FirefoxOptions())
  //ffo.setHeadless(true)
  override def csuk = quit
}

object FFSeHttpKliens
{
  def apply() =
  //**/new GenSeHttpKliens
  //new FFSeHttpKliens((new FirefoxOptions).setCapability("marionette", false).setHeadless(true))
  {
		System.setProperty("webdriver.gecko.driver", driverHelye);
    //o.setCapability("marionette", false) //nem megy, mert ambiguous reference: setCapability in class FirefoxOptions és setCapability in class MutableCapabilities
    //o.tap(FirefoxOptions.setCapability("marionette", false))   //így se megy
    new FirefoxDriver(opt)
  }
  
  val driverHelye = "/usr/local/bin/geckodriver"

  def opt =
  {
    val c = new org.openqa.selenium.MutableCapabilities
    c.setCapability("marionette", true)   //minden ellenkező híresztelés ellenére true
    val o = new FirefoxOptions(c)
    o.setHeadless(true)
  }

/*
  def ffopt =
  {
    val ffo = new FirefoxOptions()
    ffo.setHeadless(true)
    //setProxy?
    ffo
  }
*/

}