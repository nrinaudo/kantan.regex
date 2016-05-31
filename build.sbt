import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import com.typesafe.sbt.SbtSite.SiteKeys._
import UnidocKeys._
import de.heikoseeberger.sbtheader.license.Apache2_0

val jodaVersion          = "2.9.4"
val jodaConvertVersion   = "1.8.1"
val kantanCodecsVersion  = "0.1.6-SNAPSHOT"
val macroParadiseVersion = "2.1.0"
val scalatestVersion     = "3.0.0-M9"

lazy val buildSettings = Seq(
  organization       := "com.nrinaudo",
  scalaVersion       := "2.11.8",
  crossScalaVersions := Seq("2.10.6", "2.11.8"),
  autoAPIMappings    := true
)

lazy val compilerOptions = Seq("-deprecation",
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
  "-Xfuture")

lazy val baseSettings = Seq(
  scalacOptions ++= compilerOptions ++ (
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 11)) => Seq("-Ywarn-unused-import")
      case Some((2, 10)) => Seq("-Xdivergence211")
      case _ => Nil
    }
  ),
  scalacOptions in (Compile, console) ~= {
    _.filterNot(Set("-Ywarn-unused-import"))
  },
  headers := Map("scala" -> Apache2_0("2016", "Nicolas Rinaudo")),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= macroDependencies(scalaVersion.value),
  ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "kantan\\.regex\\.laws\\..*",
  incOptions  := incOptions.value.withNameHashing(true)
)

lazy val noPublishSettings = Seq(
  publish         := (),
  publishLocal    := (),
  publishArtifact := false
)

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

lazy val allSettings = buildSettings ++ baseSettings ++ publishSettings

lazy val root = Project(id = "kantan-regex", base = file("."))
  .settings(moduleName := "root")
  .settings(allSettings)
  .settings(noPublishSettings)
  .aggregate(core, docs, laws, tests, cats, scalaz, jodaTime, generic)
  .dependsOn(core, generic)
  .settings(
    initialCommands in console :=
    """
      |import kantan.regex._
      |import kantan.regex.implicits._
      |import kantan.regex.generic._
    """.stripMargin
  )
  .enablePlugins(AutomateHeaderPlugin)

lazy val core = project
  .settings(
    moduleName := "kantan.regex",
    name       := "core"
  )
  .settings(allSettings: _*)
  .enablePlugins(spray.boilerplate.BoilerplatePlugin)
  .settings(libraryDependencies += "com.nrinaudo" %% "kantan.codecs" % kantanCodecsVersion)
  .enablePlugins(AutomateHeaderPlugin)


lazy val jodaTime = Project(id = "joda-time", base = file("joda-time"))
  .settings(
    moduleName := "kantan.regex-joda-time",
    name       := "joda-time"
  )
  .settings(libraryDependencies ++= Seq(
    "com.nrinaudo"  %% "kantan.codecs-joda-time"      % kantanCodecsVersion,
    "com.nrinaudo"  %% "kantan.codecs-joda-time-laws" % kantanCodecsVersion % "test",
    "org.scalatest" %% "scalatest"                    % scalatestVersion    % "test"
  ))
  .settings(allSettings: _*)
  .dependsOn(core, laws % "test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val cats = project
  .settings(
    moduleName := "kantan.regex-cats",
    name       := "cats"
  )
  .settings(libraryDependencies ++= Seq(
    "com.nrinaudo"  %% "kantan.codecs-cats"      % kantanCodecsVersion,
    "com.nrinaudo"  %% "kantan.codecs-cats-laws" % kantanCodecsVersion  % "test",
    "org.scalatest" %% "scalatest"               % scalatestVersion     % "test"
  ))
  .settings(allSettings: _*)
  .dependsOn(core, laws % "test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val scalaz = project
  .settings(
    moduleName := "kantan.regex-scalaz",
    name       := "scalaz"
  )
  .settings(allSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "com.nrinaudo"  %% "kantan.codecs-scalaz"      % kantanCodecsVersion,
    "com.nrinaudo"  %% "kantan.codecs-scalaz-laws" % kantanCodecsVersion % "test",
    "org.scalatest" %% "scalatest"                 % scalatestVersion    % "test"
  ))
  .dependsOn(core, laws % "test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val generic = project
  .settings(
    moduleName := "kantan.regex-generic",
    name       := "generic"
  )
  .settings(allSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "com.nrinaudo"  %% "kantan.codecs-shapeless"      % kantanCodecsVersion,
    "com.nrinaudo"  %% "kantan.codecs-shapeless-laws" % kantanCodecsVersion  % "test",
    "org.scalatest" %% "scalatest"                    % scalatestVersion     % "test"
  ))
  .dependsOn(core, laws % "test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val laws = project
  .settings(
    moduleName := "kantan.regex-laws",
    name       := "laws"
  )
  .settings(libraryDependencies ++= Seq(
    "com.nrinaudo" %% "kantan.codecs-laws" % kantanCodecsVersion
  ))
  .enablePlugins(spray.boilerplate.BoilerplatePlugin)
  .settings(allSettings: _*)
  .dependsOn(core)
  .enablePlugins(AutomateHeaderPlugin)

lazy val tests = project
  .enablePlugins(spray.boilerplate.BoilerplatePlugin)
  .settings(allSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % "test")
  .dependsOn(core, cats, laws % "test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val docs = project
  .settings(allSettings: _*)
  .settings(site.settings: _*)
  .settings(ghpages.settings: _*)
  .settings(unidocSettings: _*)
  .settings(site.preprocessSite())
  .settings(libraryDependencies ++= Seq(
    "joda-time" % "joda-time"    % jodaVersion,
    "org.joda"  % "joda-convert" % jodaConvertVersion
  ))
  .settings(
    apiURL := Some(url("http://nrinaudo.github.io/kantan.regex/api/")),
    scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
      "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
      "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
    )
  )
  .settings(tutSettings: _*)
  .settings(tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import"))))
  .settings(
    site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "api"),
    site.addMappingsToSiteDir(tut, "_tut"),
    git.remoteRepo := "git@github.com:nrinaudo/kantan.regex.git",
    ghpagesNoJekyll := false,
    includeFilter in makeSite := "*.yml" | "*.md" | "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" |
                                 "*.eot" | "*.svg" | "*.ttf" | "*.woff" | "*.woff2" | "*.otf"
  )
  .settings(noPublishSettings:_*)
  .dependsOn(core, jodaTime, generic)

def macroDependencies(v: String): List[ModuleID] =
  ("org.scala-lang" % "scala-reflect" % v % "provided") :: {
    if(v.startsWith("2.10")) List(compilerPlugin("org.scalamacros" % "paradise" % macroParadiseVersion cross CrossVersion.full))
    else Nil
  }

addCommandAlias("validate", "; clean; scalastyle; test:scalastyle; coverage; test; coverageReport; coverageAggregate; docs/makeSite")
