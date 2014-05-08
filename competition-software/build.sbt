name := "XStyle"

version := "0.1"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  //"org.scala-lang" % "scala-swing" % "2.10.4"
)

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

//libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.0.0-SNAPSHOT"

scalacOptions ++= Seq(
  "-unchecked", 
  "-deprecation", 
  "-feature", 
  "-Yinline", "-Yinline-warnings",
  "-language:_"
  //,"-Xdisable-assertions", "-optimize"
)

