/*
 * Copyright (c) 2020-2023.
 * OOON.ME ALL RIGHTS RESERVED.
 * Licensed under the Mozilla Public License, version 2.0
 * Please visit <http://ooon.me> or mail to zhaihao@ooon.me
 */

package me.ooon

import play.api.libs.json.Json

/**
  * package
  *
  * @author zhaihao
  * @version 1.0
  * @since 2023/1/17 15:15
  */
package object scalaia {
  lazy private val JSON = Json.parse(os.read(os.home / os.RelPath("Library/Mobile Documents/com~apple~CloudDocs/key")))

  lazy val TOKEN = (JSON \ "token").as[String]
}
