val ScalatraVersion = "2.7.0"

organization := "com.github.laca"

name := "WeblapScalatra"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.13.1"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
//  "org.eclipse.jetty" % "jetty-webapp" % "9.4.28.v20200408" % "container",
//  "org.eclipse.jetty" % "jetty-webapp" % "9.4.31.v20200723" % "container",
//  "org.eclipse.jetty" % "jetty-webapp" % "9.4.20.v20190813" % "container",
//  "org.eclipse.jetty" % "jetty-webapp" % "10.0.0.beta1" % "container",
  "org.eclipse.jetty" % "jetty-webapp" % "11.0.0.beta1" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
  , "org.scalatra" %% "scalatra-json" % "2.7.0"
  , "org.json4s"   %% "json4s-jackson" % "3.6.7"
//  , "org.seleniumhq.selenium" % "selenium-common" % "latest.integration"
//  , "org.seleniumhq.selenium" % "selenium-common" % "4.0.0-alpha-2"   //jbrowserdriver-ben 4.0.0-alpha-2 van ... de a selenium-common -nak nincs ilyen verziója
//  , "org.seleniumhq.selenium" % "selenium-remote-driver" % "4.0.0-alpha-2"
//  , "com.machinepublishers" % "jbrowserdriver" % "1.1.1"
//  , "org.openjfx" % "javafx-maven-plugin" % "0.0.4"  //jbrowserdriver-nek kell  ...de mégsem, vagy nem elég
//  , "org.seleniumhq.selenium" % "selenium-firefox-driver" % "3.141.59"
  , "org.seleniumhq.selenium" % "selenium-firefox-driver" % "4.0.0-alpha-2"
)

enablePlugins(SbtTwirl)

enablePlugins(ScalatraPlugin)

//enablePlugins(JettyPlugin)

containerPort in Jetty := 8090
