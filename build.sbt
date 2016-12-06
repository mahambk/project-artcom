name := """artcom"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc41",
  "org.avaje" % "ebean" % "2.8.1",
  "javax.persistence" % "persistence-api" % "1.0.2"
)