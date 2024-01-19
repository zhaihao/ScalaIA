/*
 * Copyright (c) 2020-2023.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.scalaia

import com.typesafe.scalalogging.StrictLogging
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import play.api.libs.json.Json

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.io.StdIn

object DouBanMovie extends StrictLogging {
  val browser = JsoupBrowser()
  val isWrite = true
  def main(args: Array[String]): Unit = {
    logger.info("输入URL：")
    val link = StdIn.readLine()
    val doc     = browser.get(link)
    val df  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val now = LocalDateTime.now()

    val watch = now.toLocalDate
    val ctime = now.format(df)
    val mtime = now.format(df)

    // json
    val jsonText   = (doc >> "head" >> "script").filter(i => i >> attr("type") == "application/ld+json").head.innerHtml.replace("\n", "")
    val js         = Json.parse(jsonText)
    val rating     = (js \ "aggregateRating" \ "ratingValue").asOpt[String].getOrElse("999")
    if(rating.isBlank) {
      logger.error("rating is blank!")
    }
    val name       = (js \ "name").as[String]
    val publish    = (js \ "datePublished").as[String]
    val genreArray = (js \ "genre").as[Array[String]]
    val genre      = genreArray.mkString("[", ", ", "]")
    val typeString = (js \ "@type").as[String]
    var tags     = ""
    var duration = ""
    if (typeString == "Movie") {
      if (genreArray.contains("动画")) {
        tags = "动漫"
      } else {
        tags = "电影"
      }
      duration = (doc >> "span[property=v:runtime]").head.attr("content") + " 分钟"
    } else if (typeString == "TVSeries") {
      if (genreArray.contains("动画")) {
        tags = "动漫"
      } else if (genreArray.contains("真人秀")) {
        tags = "综艺"
      } else {
        tags = "电视剧"
      }
      val reg = """.*集数:</span> (\d+)""".r

      (doc >> "div[id=info]").head.innerHtml.split("\n").filter(_.contains("集数")).head match {
        case reg(a) => duration = a + " 集"
        case _      => logger.error("集数解析错误!")
      }
    } else {
      tags = "UNKNOWN"
    }

    val reg1 = """.*制片国家/地区:</span> (.+)""".r
    var country = "UNKNOWN"
    (doc >> "div[id=info]").head.innerHtml.split("\n").filter(_.contains("制片国家/地区")).head match {
      case reg1(a) => country = a
      case _       => logger.error("国家解析错误")
    }

    val reg2 = """.*又名:</span> (.+)""".r
    var aliasesArray = Array.empty[String]

    val aliasesContent = (doc >> "div[id=info]").head.innerHtml.split("\n").filter(_.contains("又名"))
    if (aliasesContent.nonEmpty) {
      aliasesContent.head match {
        case reg2(a) => aliasesArray = a.split("/").map(s => '"' + s.trim + '"')
        case _       => logger.error("别名解析错误")
      }
    }

    if (aliasesArray.length > 3) aliasesArray.take(3)
    val aliases = aliasesArray.mkString("[", ", ", "]")

    val url = doc >> attr("content")("meta[property=og:url]")

    var actorsArray = (doc >> "meta[property=video:actor]").map(_.attr("content"))
    if (actorsArray.size > 6) actorsArray = actorsArray.take(6)
    val actors = actorsArray.mkString("[", ", ", "]")

    var directorsArray = (doc >> "meta[property=video:director]").map(_.attr("content"))
    if (directorsArray.size > 6) directorsArray = directorsArray.take(6)
    val directors = directorsArray.mkString("[", ", ", "]")

    val description = (doc >> "span[property=v:summary]").head.innerHtml.trim
      .split("\n")
      .map(_.trim.replace("　", "").replace("<br>", "\n>"))
      .filter(_ != "")
      .mkString(">", "\n> ", "")

    val folder = os.Path("/Users/zhaihao/Documents/ORISON/02-Watched/")
    val content =
      s"""|---
          |name: $name
          |aliases: $aliases
          |tags: [$tags]
          |rating: $rating
          |url: $url
          |publish: $publish
          |country: [$country]
          |genre: $genre
          |watch: $watch
          |duration: $duration
          |directors: $directors
          |actors: $actors
          |ctime: $ctime
          |mtime: $mtime
          |---
          |
          |$description
          |
          |""".stripMargin

    logger.info(content)
    if (isWrite) {
      os.write.over(folder / s"$name.md", content)
    }
  }

}
