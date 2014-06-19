package com.zuehlke.worldcup

import com.zuehlke.worldcup.core.BootedCore
import com.zuehlke.worldcup.http.api.Api
import com.zuehlke.worldcup.http.HttpServer
import com.zuehlke.worldcup.config.LocalConfiguration
import com.zuehlke.worldcup.config.HerokuConfiguration

object Heroku extends App with HerokuConfiguration with BootedCore with HttpServer
