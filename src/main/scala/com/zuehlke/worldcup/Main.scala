package com.zuehlke.worldcup

import com.zuehlke.worldcup.core.BootedCore
import com.zuehlke.worldcup.http.api.Api
import com.zuehlke.worldcup.http.HttpServer

object Main extends App with BootedCore with HttpServer
