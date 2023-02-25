/*
 * Copyright (c) 2020-2023.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon.scalaia
package ck

import com.typesafe.scalalogging.StrictLogging
import test.BaseSpec

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.Random
import closeable._

import java.sql.DriverManager
import syntax._


/**
  * GenData
  * log_time    DATETIME64,
  * application String,
  * url         String,
  * referrer    String,
  * ip          String,
  * user_mail   String,
  * lang        String,
  * agent       String,
  * timing      UInt32,
  * sign        Int8 default 1
  *
  * @author zhaihao
  * @version 1.0
  * @since 2023/2/25 23:15
  */
object GenData extends StrictLogging {
  def main(args: Array[String]): Unit = {
    logger.info("gen data")
    val batch = 100
    val itr   = 50000
    val bar   = pb.Bar(batch)
    using(DriverManager.getConnection("jdbc:clickhouse://127.0.0.1:19000")) { conn =>
      using(conn.createStatement()) { st =>
        bar.meter { up =>
          cfor(1 to batch) { _ =>
            val builder = new java.lang.StringBuilder("insert into console_web_performance values ")
            cfor(1 to itr) { i =>
              val t = rndURLAndTime
              builder.append(s"('$rndLogTime','console','${t._1}','google','$rndIP','$rndUserMail','$rndLang','$rndAgent',${t._2},1)")
              if (i!=itr) builder.append(",")
            }
            val sql = builder.toString
            st.executeUpdate(sql)
            up.update()
          }
        }
      }
    }
  }
  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  val now       = LocalDateTime.parse("2023-03-01T00:00:00")
  def rndLogTime = now.minusSeconds(Random.nextInt(3456000)).format(formatter)

  val urls = Array(
    "https://console.zenlayer.com/dashboard",
    "https://console.zenlayer.com/bmc/server/create",
    "https://console.zenlayer.com/bmc/server?query=pageNum%3D1%26pageSize%3D20%26total%3D0",
    "https://console.zenlayer.com/bmc/elasticip",
    "https://console.zenlayer.com/bmc/cidrBlock?query=pageNum%3D1%26pageSize%3D20"
  )
  def rndURLAndTime = {
    val a = Random.nextInt(5)
    (urls(a),Random.nextInt((a+1)*1000))
  }

  val ips = Array(
    "115.197.103.108",
    "129.45.17.12",
    "173.194.112.139",
    "77.88.55.66",
    "2.28.228.0",
    "95.47.254.1",
    "62.35.172.0"
  )
  def rndIP = ips(Random.nextInt(7))

  def rndUserMail = s"user${Random.nextInt(5)}@zenlayer.com"

  val lang = Array("zh-CN", "en-US")
  def rndLang = lang(Random.nextInt(2))

  val agents = Array(
    "Windows IE",
    "Mac OSX Safari",
    "Windows Chrome"
  )
  def rndAgent = agents(Random.nextInt(3))

}
