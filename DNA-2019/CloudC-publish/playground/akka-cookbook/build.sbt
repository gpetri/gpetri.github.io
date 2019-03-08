
lazy val commonSettings = Seq(
  version := "1.0",
  name := "akka-cookbook",
  organization := "io.github.gpetri",
  scalaVersion := "2.12.8"
)

lazy val app = (project in file("app")).
  settings(commonSettings: _*).
  settings(
    // your settings here
  )


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.19",
  "com.typesafe.akka" %% "akka-agent" % "2.5.19",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.19" % Test,
  "com.typesafe.akka" %% "akka-persistence" % "2.5.19",
  "com.typesafe.akka" %% "akka-remote" % "2.5.19",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.19",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.5.19",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.5.19",
  "com.typesafe.akka" %% "akka-distributed-data" % "2.5.19",
  "com.typesafe.akka" %% "akka-contrib" % "2.5.19"
)

libraryDependencies += "org.iq80.leveldb" % "leveldb" % "0.7"
libraryDependencies += "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
  libraryDependencies += "com.enragedginger" %% "akka-quartz-scheduler" % "1.8.0-akka-2.5.x"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

/* lagomUnmanagedServices in ThisBuild := Map("login" -> "http://127.0.0.1:4000") */

assemblyMergeStrategy in assembly := {
   case PathList("META-INF", xs @ _*) => MergeStrategy.discard
   case "reference.conf" => MergeStrategy.concat
   case x => MergeStrategy.first
}

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("io.github.gpetri.**" -> "gfp.@1").inAll
)
