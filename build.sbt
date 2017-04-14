name := """artcom"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.avaje" % "ebean" % "2.8.1",
  "javax.persistence" % "persistence-api" % "1.0.2",
  "com.amazonaws" % "aws-java-sdk" % "1.11.46"
)
