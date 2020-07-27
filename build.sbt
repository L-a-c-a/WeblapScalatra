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
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.28.v20200408" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
  , "org.scalatra" %% "scalatra-json" % "2.7.0"
  , "org.json4s"   %% "json4s-jackson" % "3.6.7"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)

//enablePlugins(JettyPlugin)

containerPort in Jetty := 8090
