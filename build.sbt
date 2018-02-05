kantanProject in ThisBuild := "regex"
startYear in ThisBuild     := Some(2016)

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
  jodaTime,
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
    initialCommands in console :=
      """
      |import kantan.regex._
      |import kantan.regex.implicits._
      |import kantan.regex.generic._
      |import kantan.regex.refined._
    """.stripMargin
  )
  .aggregate((jsModules ++ jvmModules :+ (docs: ProjectReference)): _*)
  .aggregateIf(java8Supported)(java8)
  .dependsOn(coreJVM, genericJVM, refinedJVM)

lazy val docs = project
  .settings(
    unidocProjectFilter in (ScalaUnidoc, unidoc) :=
      inAnyProject -- inProjectsIf(!java8Supported)(java8) -- inProjects(jsModules: _*)
  )
  .enablePlugins(DocumentationPlugin)
  .dependsOn(catsJVM, coreJVM, enumeratumJVM, genericJVM, jodaTime, libra, refinedJVM, scalazJVM)
  .dependsOnIf(java8Supported)(java8)

// - core projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val core = kantanCrossProject("core")
  .settings(moduleName := "kantan.regex")
  // TODO: disable when we upgrade to 2.12.3, which appears to fix this issue.
  // This is necessary because with scala 2.12.x, we use too many nested lambdas for deserialisation to succeed with the
  // "optimised" behaviour.
  .settings(scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((_, x)) if x == 12 ⇒ Seq("-Ydelambdafy:inline")
    case _                       ⇒ Seq.empty
  }))
  .enablePlugins(PublishedPlugin, BoilerplatePlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo"   %%% "kantan.codecs" % Versions.kantanCodecs,
      "com.propensive" %%% "contextual"    % Versions.contextual,
      "org.scalatest"  %%% "scalatest"     % Versions.scalatest % "test"
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

// - joda-time projects ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val jodaTime = Project(id = "joda-time", base = file("joda-time"))
  .settings(
    moduleName := "kantan.regex-joda-time",
    name       := "joda-time"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(coreJVM, lawsJVM % "test")
  .settings(
    libraryDependencies ++= Seq(
      "com.nrinaudo"  %% "kantan.codecs-joda-time"      % Versions.kantanCodecs,
      "com.nrinaudo"  %% "kantan.codecs-joda-time-laws" % Versions.kantanCodecs % "test",
      "org.scalatest" %% "scalatest"                    % Versions.scalatest % "test"
    )
  )

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
      "com.nrinaudo"  %% "kantan.codecs-java8"      % Versions.kantanCodecs,
      "com.nrinaudo"  %% "kantan.codecs-java8-laws" % Versions.kantanCodecs % "test",
      "org.scalatest" %% "scalatest"                % Versions.scalatest % "test"
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
      "com.nrinaudo"  %%% "kantan.codecs-cats"      % Versions.kantanCodecs,
      "com.nrinaudo"  %%% "kantan.codecs-cats-laws" % Versions.kantanCodecs % "test",
      "org.scalatest" %%% "scalatest"               % Versions.scalatest % "test"
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
      "com.nrinaudo"  %%% "kantan.codecs-scalaz"      % Versions.kantanCodecs,
      "com.nrinaudo"  %%% "kantan.codecs-scalaz-laws" % Versions.kantanCodecs % "test",
      "org.scalatest" %%% "scalatest"                 % Versions.scalatest % "test"
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
      "com.nrinaudo"  %%% "kantan.codecs-shapeless"      % Versions.kantanCodecs,
      "com.nrinaudo"  %%% "kantan.codecs-shapeless-laws" % Versions.kantanCodecs % "test",
      "org.scalatest" %%% "scalatest"                    % Versions.scalatest % "test"
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
      "com.nrinaudo"  %%% "kantan.codecs-refined"      % Versions.kantanCodecs,
      "com.nrinaudo"  %%% "kantan.codecs-refined-laws" % Versions.kantanCodecs % "test",
      "org.scalatest" %%% "scalatest"                  % Versions.scalatest % "test"
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
      "com.nrinaudo"  %%% "kantan.codecs-enumeratum"      % Versions.kantanCodecs,
      "com.nrinaudo"  %%% "kantan.codecs-enumeratum-laws" % Versions.kantanCodecs % "test",
      "org.scalatest" %%% "scalatest"                     % Versions.scalatest % "test"
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
      "com.nrinaudo"  %% "kantan.codecs-libra"      % Versions.kantanCodecs,
      "com.nrinaudo"  %% "kantan.codecs-libra-laws" % Versions.kantanCodecs % "test",
      "org.scalatest" %% "scalatest"                % Versions.scalatest % "test"
    )
  )
