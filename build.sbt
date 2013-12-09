name := "Auction"

organization := "com.meekrab"

version := "0.0.1"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test",
  "org.mongodb" %% "casbah" % "2.6.3"
)

initialCommands := "import com.meekrab.auction._"

