// - Dependency versions -----------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
val kantanCodecsVersion  = "0.1.10-SNAPSHOT"
val scalatestVersion     = "3.0.1"

kantanProject in ThisBuild := "regex"



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
    """.stripMargin
  )
  .aggregate(core, docs, laws, tests, cats, scalaz, jodaTime, generic)
  .dependsOn(core, generic)

lazy val tests = project
  .enablePlugins(UnpublishedPlugin)
  .enablePlugins(spray.boilerplate.BoilerplatePlugin)
  .dependsOn(core, cats, laws, generic, jodaTime, scalaz)
  .settings(libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest"                    % scalatestVersion    % "test",
    "com.nrinaudo"  %% "kantan.codecs-cats-laws"      % kantanCodecsVersion % "test",
    "com.nrinaudo"  %% "kantan.codecs-shapeless-laws" % kantanCodecsVersion % "test",
    "com.nrinaudo"  %% "kantan.codecs-joda-time-laws" % kantanCodecsVersion % "test",
    "com.nrinaudo"  %% "kantan.codecs-scalaz-laws"    % kantanCodecsVersion % "test"
  ))

lazy val docs = project
  .enablePlugins(DocumentationPlugin)
  .dependsOn(core, jodaTime, generic, cats, scalaz)



// - core projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val core = project
  .settings(
    moduleName := "kantan.regex",
    name       := "core"
  )
  .enablePlugins(PublishedPlugin)
  .enablePlugins(spray.boilerplate.BoilerplatePlugin)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs" % kantanCodecsVersion)

lazy val laws = project
  .settings(
    moduleName := "kantan.regex-laws",
    name       := "laws"
  )
  .enablePlugins(PublishedPlugin)
  .enablePlugins(spray.boilerplate.BoilerplatePlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-laws" % kantanCodecsVersion)



// - joda-time projects ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val jodaTime = Project(id = "joda-time", base = file("joda-time"))
  .settings(
    moduleName := "kantan.regex-joda-time",
    name       := "joda-time"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-joda-time" % kantanCodecsVersion)



// - cats projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val cats = project
  .settings(
    moduleName := "kantan.regex-cats",
    name       := "cats"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-cats" % kantanCodecsVersion)



// - scalaz projects ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val scalaz = project
  .settings(
    moduleName := "kantan.regex-scalaz",
    name       := "scalaz"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-scalaz" % kantanCodecsVersion)



// - shapeless projects ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val generic = project
  .settings(
    moduleName := "kantan.regex-generic",
    name       := "generic"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-shapeless" % kantanCodecsVersion)
