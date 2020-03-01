val scalaExercisesV = "0.6.0-SNAPSHOT"

def dep(artifactId: String) = "org.scala-exercises" %% artifactId % scalaExercisesV

lazy val template = (project in file("."))
  .enablePlugins(ExerciseCompilerPlugin)
  .settings(
    organization := "org.scala-exercises",
    name := "exercises-pureconfig",
    scalaVersion := "2.13.1",
    version := "0.0.1",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases")
    ),
    libraryDependencies ++= Seq(
      dep("exercise-compiler"),
      dep("definitions"),
      %%("shapeless", "2.3.3"),
      %%("scalatest", "3.1.1"),
      %%("scalacheck", "1.14.3"),
      "com.github.pureconfig"     %% "pureconfig"            % "0.12.2",
      "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.4",
      "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
  )