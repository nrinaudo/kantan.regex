ThisBuild / kantanProject := "regex"
ThisBuild / startYear     := Some(2016)

lazy val jsModules: Seq[ProjectReference] = Seq(
  catsJS,
  coreJS,
  enumeratumJS,
  genericJS,
  lawsJS,
  refinedJS,
  scalazJS
)

lazy val jvmModules: Seq[ProjectReference] = Seq(
  catsJVM,
  coreJVM,
  enumeratumJVM,
  genericJVM,
  java8,
  lawsJVM,
  libra,
  refinedJVM,
  scalazJVM
)

// - root projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val root = Project(id = "kantan-regex", base = file("."))
  .settings(moduleName := "root")
  .enablePlugins(UnpublishedPlugin)
  .settings(
    console / initialCommands :=
      """
      |import kantan.regex._
      |import kantan.regex.implicits._
      |import kantan.regex.generic._
      |import kantan.regex.refined._
    """.stripMargin
  )
  .aggregate((jsModules ++ jvmModules :+ (docs: ProjectReference)): _*)
  .dependsOn(coreJVM, genericJVM, refinedJVM)

lazy val docs = project
  .settings(
    ScalaUnidoc / unidoc / unidocProjectFilter :=
      inAnyProject -- inProjects(jsModules: _*)
  )
  .enablePlugins(DocumentationPlugin)
  .settings(name := "docs")
  .settings(libraryDependencies += "joda-time" % "joda-time" % Versions.jodaTime)
  .dependsOn(catsJVM, coreJVM, enumeratumJVM, genericJVM, java8, libra, refinedJVM, scalazJVM)

// - core projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val core = kantanCrossProject("core")
  .settings(moduleName := "kantan.regex")
  .enablePlugins(PublishedPlugin, BoilerplatePlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %%% "kantan.codecs" % Versions.kantanCodecs
    )
  )
  .laws("laws")

lazy val coreJVM = core.jvm
lazy val coreJS  = core.js

lazy val laws = kantanCrossProject("laws")
  .settings(moduleName := "kantan.regex-laws")
  .enablePlugins(PublishedPlugin, BoilerplatePlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %%% "kantan.codecs-laws" % Versions.kantanCodecs)

lazy val lawsJVM = laws.jvm
lazy val lawsJS  = laws.js

// - java8 projects ----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val java8 = project
  .settings(
    moduleName := "kantan.regex-java8",
    name       := "java8"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(coreJVM, lawsJVM % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-java8"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-java8-laws" % Versions.kantanCodecs % "test"
    )
  )

// - cats projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val cats = kantanCrossProject("cats")
  .settings(moduleName := "kantan.regex-cats")
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %%% "kantan.codecs-cats"      % Versions.kantanCodecs,
      "com.nrinaudo" %%% "kantan.codecs-cats-laws" % Versions.kantanCodecs % "test"
    )
  )

lazy val catsJVM = cats.jvm
lazy val catsJS  = cats.js

// - scalaz projects ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val scalaz = kantanCrossProject("scalaz")
  .settings(moduleName := "kantan.regex-scalaz")
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %%% "kantan.codecs-scalaz"      % Versions.kantanCodecs,
      "com.nrinaudo" %%% "kantan.codecs-scalaz-laws" % Versions.kantanCodecs % "test"
    )
  )

lazy val scalazJVM = scalaz.jvm
lazy val scalazJS  = scalaz.js

// - shapeless projects ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val generic = kantanCrossProject("generic")
  .settings(moduleName := "kantan.regex-generic")
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %%% "kantan.codecs-shapeless"      % Versions.kantanCodecs,
      "com.nrinaudo" %%% "kantan.codecs-shapeless-laws" % Versions.kantanCodecs % "test"
    )
  )

lazy val genericJVM = generic.jvm
lazy val genericJS  = generic.js

// - refined project ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val refined = kantanCrossProject("refined")
  .settings(moduleName := "kantan.regex-refined")
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %%% "kantan.codecs-refined"      % Versions.kantanCodecs,
      "com.nrinaudo" %%% "kantan.codecs-refined-laws" % Versions.kantanCodecs % "test"
    )
  )

lazy val refinedJVM = refined.jvm
lazy val refinedJS  = refined.js

// - Enumeratum project ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val enumeratum = kantanCrossProject("enumeratum")
  .settings(moduleName := "kantan.regex-enumeratum")
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %%% "kantan.codecs-enumeratum"      % Versions.kantanCodecs,
      "com.nrinaudo" %%% "kantan.codecs-enumeratum-laws" % Versions.kantanCodecs % "test"
    )
  )

lazy val enumeratumJVM = enumeratum.jvm
lazy val enumeratumJS  = enumeratum.js

// - Libra project -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val libra = project
  .settings(
    moduleName := "kantan.regex-libra",
    name       := "libra"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(coreJVM, lawsJVM % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo" %% "kantan.codecs-libra"      % Versions.kantanCodecs,
      "com.nrinaudo" %% "kantan.codecs-libra-laws" % Versions.kantanCodecs % "test"
    )
  )
