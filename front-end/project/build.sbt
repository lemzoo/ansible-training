enablePlugins(ScalaJSPlugin)
name := "front-end"

version := "0.1.0"
organization := "edc4it"

scalaVersion := "2.12.6"
relativeSourceMaps := true
scalaJSUseMainModuleInitializer := true
//skip in packageJSDependencies := false
libraryDependencies ++= Seq(
  "com.thoughtworks.binding" %%% "futurebinding" % "11.0.1",
  "com.thoughtworks.binding" %%% "dom" % "11.0.1",
  "com.typesafe.play" %%% "play-json" % "2.6.0",
  "org.scalatest" %%% "scalatest" % "3.0.3" % Test
)
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)