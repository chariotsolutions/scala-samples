name := "akka-http-sample"

version := "1.0"

organization := "com.chariotsolutions"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0-M3",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

scalaVersion := "2.11.4"
