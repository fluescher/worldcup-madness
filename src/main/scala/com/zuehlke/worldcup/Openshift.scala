package com.zuehlke.worldcup

import com.zuehlke.worldcup.core.BootedCore
import com.zuehlke.worldcup.http.api.Api
import com.zuehlke.worldcup.http.HttpServer
import com.zuehlke.worldcup.config.OpenshiftConfiguration

object Openshift extends App with OpenshiftConfiguration with BootedCore with HttpServer
