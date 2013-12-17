name := "RSSEater"

organization := "com.meekrab"

version := "0.0.1"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test",
  "org.scalaj" %% "scalaj-http" % "0.3.12",
  "com.typesafe.play" %% "play-json" % "2.2.1"
)