libraryDependencies ++= {
  Seq(
    "com.github.pureconfig" %% "pureconfig" % Versions.pureconfig,
    "com.typesafe.slick" %% "slick-codegen" % Versions.slick,

    // supported databases
    "com.h2database" % "h2" % Versions.h2,
  )
}
