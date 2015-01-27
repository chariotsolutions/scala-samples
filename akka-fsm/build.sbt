name := "akka-fsm-sample"

version := "1.0"

organization := "com.chariotsolutions"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.9" % "test",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)
