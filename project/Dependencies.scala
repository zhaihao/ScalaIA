/*
 * Copyright (c) 2020-2022.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */
import sbt._

/**
  * Dependencies
  *
  * @author zhaihao
  * @version 1.0
  * @since 2022/8/18 19:58
  */
object Dependencies extends AutoPlugin {
  override def requires = empty
  override def trigger  = allRequirements

  object autoImport {

    lazy val ORISON          = "me.ooon"                %% "orison"                     % "1.0.17"
    lazy val NSCALA          = "com.github.nscala-time" %% "nscala-time"                % "2.34.0"
    lazy val OS_LIB          = "com.lihaoyi"            %% "os-lib"                     % "0.11.3"
    lazy val SQUANTS         = "org.typelevel"          %% "squants"                    % "1.7.4"
    lazy val TYPESAFE_CONFIG = "com.typesafe"            % "config"                     % "1.4.3"
    lazy val PLAY_JSON       = "org.playframework"      %% "play-json"                  % "3.0.4"
    lazy val PAR             = "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
    lazy val CK              = "com.github.housepower"   % "clickhouse-native-jdbc"     % "2.7.1"
    lazy val ARGON2          = "de.mkammerer"            % "argon2-jvm"                 % "2.11"
    lazy val REQUESTS        = "com.lihaoyi"            %% "requests"                   % "0.9.0"
    lazy val SCRAPER         = "net.ruippeixotog"       %% "scala-scraper"              % "3.1.2"

    lazy val LOG = Seq(
      "org.slf4j"                   % "log4j-over-slf4j" % "2.0.16",
      "com.typesafe.scala-logging" %% "scala-logging"    % "3.9.5",
      "ch.qos.logback"              % "logback-classic"  % "1.5.12"
    )

    lazy val SCALA_TEST = Seq(
      "org.scalatest" %% "scalatest-core"           % "3.2.19",
      "org.scalatest"  % "scalatest-compatible"     % "3.2.19",
      "org.scalatest" %% "scalatest-diagrams"       % "3.2.19",
      "org.scalatest" %% "scalatest-matchers-core"  % "3.2.19",
      "org.scalatest" %% "scalatest-shouldmatchers" % "3.2.19",
      "org.scalatest" %% "scalatest-freespec"       % "3.2.19"
    )
  }
}
