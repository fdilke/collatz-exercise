import scala.collection.Seq

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.7.0"

val Http4sVersion = "1.0.0-M29"
val LogbackVersion = "1.2.6"
//val TapirVersion = "1.11.35"
// val TapirVersion = "1.10.9"

libraryDependencies ++= Seq(
  "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
  "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
  "org.http4s"      %% "http4s-circe"        % Http4sVersion,
  "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
  "ch.qos.logback"  %  "logback-classic"     % LogbackVersion
//  "org.typelevel" %% "cats-effect" % "3.5.4"
//  "com.softwaremill.sttp.tapir" %% "tapir-zio" % TapirVersion,
//  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server-zio" % TapirVersion,
//  "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % TapirVersion,
//  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % TapirVersion,
//  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion
)

lazy val root = (project in file("."))
  .settings(
    name := "collatz-exercise"
  )
