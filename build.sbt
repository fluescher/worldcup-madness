import AssemblyKeys._

import com.typesafe.sbt.SbtStartScript

seq(SbtStartScript.startScriptForClassesSettings: _*)

name := "worldcup-madness"

version := "1.0"

scalaVersion := "2.11.1"

mainClass in (Compile, run) := Some("com.zuehlke.worldcup.Main")

mainClass in assembly := Some("com.zuehlke.worldcup.Main")

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "com.typesafe.akka"       %% "akka-actor"         % "2.3.2",
  "com.typesafe.akka"       %% "akka-persistence-experimental" % "2.3.2",
  "com.typesafe.akka"       %% "akka-slf4j"         % "2.3.2",
  "ch.qos.logback"          % "logback-classic"     % "1.0.13",
  "io.spray"                %% "spray-can"          % "1.3.1-20140423",
  "io.spray"                %% "spray-routing"      % "1.3.1-20140423",
  "io.spray"                %% "spray-json"         % "1.2.6",
  "io.spray"                %% "spray-client"       % "1.3.1-20140423",
  "com.github.nscala-time"  %% "nscala-time"        % "1.2.0",
  "com.github.ddevore"      %% "akka-persistence-mongo-casbah"  % "0.7.2-SNAPSHOT" % "compile",
  "io.spray"                %% "spray-testkit"      % "1.3.1-20140423"  % "test",
  "com.typesafe.akka"       %% "akka-testkit"       % "2.3.2"           % "test",
  "com.novocode"            % "junit-interface"	    % "0.7"             % "test->default",
  "org.scalatest"           %% "scalatest"          % "2.1.6"           % "test"
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

//EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

jarName in assembly := "worldcup-madness.jar"

resourceDirectory in stage := baseDirectory.value / "src" / "main" / "heroku-resources"
