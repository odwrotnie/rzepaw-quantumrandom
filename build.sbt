name := "quantum-random"

organization := "it.wext"

version := "1.1.5"

scalaVersion := "2.12.6"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5"

publishTo := Some("releases" at "http://archiva.fairga.me/repository/internal")
credentials += Credentials("Repository Archiva Managed internal Repository", "archiva.fairga.me", "admin", "Dworcowa13")
