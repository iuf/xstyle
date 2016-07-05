name := "xstyle"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= (
  "org.scalatest" %% "scalatest" % "2.1.7" % "test" ::
  Nil
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Yinline", "-Yinline-warnings",
  "-language:_",
  "-Xlint"
//,"-Xdisable-assertions", "-optimise"
)

initialCommands in console := """
import xstyle.libxstyle.Helpers._
import xstyle.libxstyle._
import xstyle.simulation._
"""
