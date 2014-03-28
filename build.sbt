name := "worldcup-madness"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "spray nightlies" at "http://nightlies.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka"       %% "akka-actor"         % "2.2.0",
  "com.typesafe.akka"       %% "akka-slf4j"         % "2.2.0",
  "ch.qos.logback"          % "logback-classic"     % "1.0.13",
  "io.spray"                % "spray-can"           % "1.2-20130712",
  "io.spray"                % "spray-routing"       % "1.2-20130712",
  "io.spray"                %% "spray-json"         % "1.2.3",
  "io.spray"                % "spray-testkit"       % "1.2-20130712"    % "test",
  "com.typesafe.akka"       %% "akka-testkit"       % "2.2.0"           % "test",
  "com.novocode"            % "junit-interface"	    % "0.7"             % "test->default",
  "org.scalatest"           %% "scalatest"          % "2.1.0"           % "test"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)
