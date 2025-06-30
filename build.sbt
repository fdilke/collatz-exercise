import scala.collection.Seq

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.7.0"

val Http4sVersion = "1.0.0-M29"
val LogbackVersion = "1.2.6"

libraryDependencies ++= Seq(
  "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
  "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
  "org.http4s"      %% "http4s-circe"        % Http4sVersion,
  "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
  "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
)

lazy val root = (project in file("."))
  .settings(
    name := "tx-exercise"
  )
