version      := "0.1.0"
scalaVersion := "2.13.12"
name         := "scalaia"
organization := "me.ooon"
target       := studioTarget.value

Global / excludeLintKeys := Set(idePackagePrefix)

idePackagePrefix := Some("me.ooon.scalaia")

libraryDependencies ++= Seq(NSCALA, OS_LIB, SQUANTS, ORISON, TYPESAFE_CONFIG, PLAY_JSON, NSCALA, CK, ARGON2, REQUESTS)
libraryDependencies ++= Seq(SCALA_TEST, LOG).flatten

excludeDependencies ++= Seq(
  ExclusionRule("org.slf4j", "slf4j-log4j12"),
  ExclusionRule("log4j", "log4j")
)
