package com.zuehlke.worldcup.config

trait RuntimeConfiguration {
  val ip: String
  val port: Int
}

trait LocalConfiguration extends RuntimeConfiguration {
  override val ip = "0.0.0.0"
  override val port = 8080
}

trait OpenshiftConfiguration extends RuntimeConfiguration {
  override val ip = sys.env("OPENSHIFT_DIY_IP")
  override val port = sys.env("OPENSHIFT_DIY_PORT").toInt
}