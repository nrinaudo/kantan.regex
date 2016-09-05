import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import UnidocKeys._
import de.heikoseeberger.sbtheader.license.Apache2_0
import scala.xml.transform.{RewriteRule, RuleTransformer}



// - Dependency versions -----------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
val kantanCodecsVersion  = "0.1.8-SNAPSHOT"
val kindProjectorVersion = "0.8.1"
val macroParadiseVersion = "2.1.0"
val scalatestVersion     = "3.0.0-M9"



// - Common settings ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// Basic build settings.
lazy val buildSettings = Seq(
  organization       := "com.nrinaudo",
  scalaVersion       := "2.11.8",
  crossScalaVersions := Seq("2.10.6", "2.11.8"),
  autoAPIMappings    := true
)

// Minimum set of compiler flags for sane development.
lazy val compilerOptions = Seq(
  "-deprecation",
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

// Settings that should be enabled in all modules.
lazy val baseSettings = Seq(
  // Version-specific compiler options.
  scalacOptions ++= compilerOptions ++ (
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 11)) ⇒ Seq("-Ywarn-unused-import")
      case Some((2, 10)) ⇒ Seq("-Xdivergence211")
      case _             ⇒ Nil
    }
  ),

  // Disable -Ywarn-unused-imports in the console.
  scalacOptions in (Compile, console) ~= { _.filterNot(Set("-Ywarn-unused-import")) },

  // Standard resolvers.
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),

  // Copyright header.
  headers := Map("scala" → Apache2_0("2016", "Nicolas Rinaudo")),

  // Common dependencies.
  libraryDependencies ++= macroDependencies(scalaVersion.value),
  libraryDependencies += compilerPlugin("org.spire-math" % "kind-projector" % kindProjectorVersion cross CrossVersion.binary),

    // Don't include scoverage as a dependency in the pom
  // This code was copied from https://github.com/mongodb/mongo-spark
  pomPostProcess := { (node: xml.Node) ⇒
    new RuleTransformer(
      new RewriteRule {
        override def transform(node: xml.Node): Seq[xml.Node] = node match {
          case e: xml.Elem
              if e.label == "dependency" && e.child.exists(child ⇒ child.label == "groupId" && child.text == "org.scoverage") ⇒ Nil
          case _ ⇒ Seq(node)
        }
      }).transform(node).head
  },

  // Don't include scoverage as a dependency in the pom
  // This code was copied from https://github.com/mongodb/mongo-spark
  pomPostProcess := { (node: xml.Node) ⇒
    new RuleTransformer(
      new RewriteRule {
        override def transform(node: xml.Node): Seq[xml.Node] = node match {
          case e: xml.Elem
              if e.label == "dependency" && e.child.exists(child ⇒ child.label == "groupId" && child.text == "org.scoverage") ⇒ Nil
          case _ ⇒ Seq(node)
        }
      }).transform(node).head
  },

  // Exclude laws from code coverage.
  ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "kantan\\.regex\\.laws\\..*",

  // Speeds compilation up.
  incOptions := incOptions.value.withNameHashing(true)
)

// Settings for all modules that won't be published.
lazy val noPublishSettings = Seq(
  publish         := (),
  publishLocal    := (),
  publishArtifact := false
)

// Settings for all modules that will be published.
lazy val publishSettings = Seq(
  homepage := Some(url("https://nrinaudo.github.io/kantan.regex")),
  licenses := Seq("Apache-2.0" → url("https://www.apache.org/licenses/LICENSE-2.0.html")),
  apiURL := Some(url("https://nrinaudo.github.io/kantan.regex/api/")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/nrinaudo/kantan.regex"),
      "scm:git:git@github.com:nrinaudo/kantan.regex.git"
    )
  ),
  pomExtra := <developers>
    <developer>
      <id>nrinaudo</id>
      <name>Nicolas Rinaudo</name>
      <url>http://nrinaudo.github.io</url>
    </developer>
  </developers>
)

// Base settings for all modules.
// Modules that shouldn't be published must also use noPublishSettings.
lazy val allSettings = buildSettings ++ baseSettings ++ publishSettings

// Platform specific list of dependencies for macros.
def macroDependencies(v: String): List[ModuleID] =
  ("org.scala-lang" % "scala-reflect" % v % "provided") :: {
    if(v.startsWith("2.10")) List(compilerPlugin("org.scalamacros" % "paradise" % macroParadiseVersion cross CrossVersion.full))
    else Nil
  }


// Custom settings required by sbt.site.
lazy val tutSiteDir = settingKey[String]("Website tutorial directory")
lazy val apiSiteDir = settingKey[String]("Unidoc API directory")



// - root projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val root = Project(id = "kantan-regex", base = file("."))
  .settings(moduleName := "root")
  .settings(allSettings)
  .settings(noPublishSettings)
  .settings(
    initialCommands in console :=
    """
      |import kantan.regex._
      |import kantan.regex.implicits._
      |import kantan.regex.generic._
    """.stripMargin
  )
  .enablePlugins(AutomateHeaderPlugin)
  .aggregate(core, docs, laws, tests, cats, scalaz, jodaTime, generic)
  .dependsOn(core, generic)

lazy val tests = project
  .settings(allSettings)
  .settings(noPublishSettings)
  .enablePlugins(AutomateHeaderPlugin)
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
  .settings(allSettings)
  .enablePlugins(PreprocessPlugin)
  .settings(ghpages.settings)
  .settings(unidocSettings)
  .settings(
    apiURL := Some(url("http://nrinaudo.github.io/kantan.regex/api/")),
    scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
      "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
      "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
    )
  )
  .settings(tutSettings)
  .settings(tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import"))))
  .settings(
    tutSiteDir := "_tut",
    apiSiteDir := "api",
    addMappingsToSiteDir(tut, tutSiteDir),
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), apiSiteDir),
    git.remoteRepo := "git@github.com:nrinaudo/kantan.regex.git",
    ghpagesNoJekyll := false,
    includeFilter in makeSite := "*.yml" | "*.md" | "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" |
                                 "*.eot" | "*.svg" | "*.ttf" | "*.woff" | "*.woff2" | "*.otf"
  )
  .settings(noPublishSettings:_*)
  .dependsOn(core, jodaTime, generic, cats, scalaz)



// - core projects ------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val core = project
  .settings(
    moduleName := "kantan.regex",
    name       := "core"
  )
  .settings(allSettings)
  .enablePlugins(AutomateHeaderPlugin)
  .enablePlugins(spray.boilerplate.BoilerplatePlugin)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs" % kantanCodecsVersion)

lazy val laws = project
  .settings(
    moduleName := "kantan.regex-laws",
    name       := "laws"
  )
  .settings(allSettings)
  .enablePlugins(AutomateHeaderPlugin)
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
  .settings(allSettings)
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-joda-time" % kantanCodecsVersion)



// - cats projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val cats = project
  .settings(
    moduleName := "kantan.regex-cats",
    name       := "cats"
  )
  .settings(allSettings)
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-cats" % kantanCodecsVersion)



// - scalaz projects ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val scalaz = project
  .settings(
    moduleName := "kantan.regex-scalaz",
    name       := "scalaz"
  )
  .settings(allSettings)
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-scalaz" % kantanCodecsVersion)



// - shapeless projects ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val generic = project
  .settings(
    moduleName := "kantan.regex-generic",
    name       := "generic"
  )
  .settings(allSettings)
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs-shapeless" % kantanCodecsVersion)



// - Command alisases --------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
addCommandAlias("validate", ";clean;scalastyle;test:scalastyle;coverage;test;coverageReport;coverageAggregate;docs/makeSite")
