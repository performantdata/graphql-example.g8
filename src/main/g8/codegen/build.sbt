libraryDependencies ++= {
  val slickVersion = "3.3.3"

  Seq(
    "com.github.pureconfig" %% "pureconfig" % "0.16.0",
    "com.typesafe.slick" %% "slick-codegen" % slickVersion,

    // supported databases
    "com.h2database" % "h2" % "1.4.200",
  )
}
