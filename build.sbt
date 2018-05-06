name := "quantum-random"

organization := "it.wext"

version := "1.1.2"

scalaVersion := "2.12.6"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0"

publishTo := Some("releases" at "http://fg:5002/repository/internal")
credentials += Credentials("Repository Archiva Managed internal Repository", "fg", "admin", "Dworcowa13")
