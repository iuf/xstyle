name := "xstyle"

version := "0.1"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.1.7" % "test"
  //"org.scala-lang" % "scala-swing" % "2.10.4"
)

//resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

//libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.0.0-SNAPSHOT"

scalacOptions ++= Seq(
  "-unchecked", 
  "-deprecation", 
  "-feature", 
  "-Yinline", "-Yinline-warnings",
  "-language:_",
  "-Xlint"
  //,"-Xdisable-assertions", "-optimise"
)

// faster incremental compilation
incOptions := incOptions.value.withNameHashing(true)

initialCommands in console := """
import xstyle.libxstyle.Helpers._
import xstyle.libxstyle._
import xstyle.simulation._
"""
