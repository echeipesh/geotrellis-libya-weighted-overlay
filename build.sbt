import scala.util.Properties

scalaVersion := "2.11.8"

lazy val commonSettings = Seq(
  organization := "com.azavea.geotrellis",
  version := "0.1.0",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-Yinline-warnings",
    "-language:implicitConversions",
    "-language:reflectiveCalls",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:existentials",
    "-feature"),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
  test in assembly := {},
  ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
  resolvers += "LocationTech GeoTrellis Releases" at "https://repo.locationtech.org/content/repositories/geotrellis-releases",
  libraryDependencies ++= Seq(
    "org.apache.spark"  %% "spark-core"    % "2.0.1",
    "io.spray"          %% "spray-routing" % "1.3.3",
    "io.spray"          %% "spray-can"     % "1.3.3",
    "org.apache.hadoop"  % "hadoop-client" % "2.7.1",
    "org.locationtech.geotrellis" %% "geotrellis-spark" % Version.gtVersion,
    "com.google.guava" % "guava" % "16.0.1"
  ) map  { _ exclude("com.google.guava", "guava") },
  assemblyMergeStrategy in assembly := {
    case str if str.toUpperCase.contains("LICENSE") => MergeStrategy.discard
    case "reference.conf" => MergeStrategy.concat
    case "application.conf" => MergeStrategy.concat
    case "META-INF/LICENSE" => MergeStrategy.discard
    case "META-INF/LICENSE.txt" => MergeStrategy.discard
    case "META-INF/MANIFEST.MF" => MergeStrategy.discard
    case "META-INF\\MANIFEST.MF" => MergeStrategy.discard
    case "META-INF/ECLIPSEF.RSA" => MergeStrategy.discard
    case "META-INF/ECLIPSEF.SF" => MergeStrategy.discard
    case _ => MergeStrategy.first
  }
)

lazy val root = Project("root", file("."))
  .aggregate(etl, server)

lazy val etl = (project in file("etl"))
  .settings(commonSettings: _*)

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka"           %% "akka-actor"    % "2.4.14",
      "org.locationtech.geotrellis" %% "geotrellis-s3" % Version.gtVersion
    )
  )
