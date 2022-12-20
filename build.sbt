ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val circeVersion = "0.14.1"

val circeDependencies = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val root = (project in file("."))
  .settings(
    Compile / mainClass := Some("io.scandal.Main"),
    run / mainClass := Some("io.scandal.Main"),
    libraryDependencies ++= List(
      "com.lihaoyi" %% "cask" % "0.8.3",
      "org.tpolecat" %% "skunk-core" % "0.3.2",
      "org.typelevel" %% "cats-effect" % "3.4.2"
    ) ++ circeDependencies,
    name := "Scandal"
  )
