import sbt._
import Keys._

name := "graph-core"
organization := "org.scala-graph"
version := "1.13.1-SNAPSHOT"

scalaVersion := "2.13.0"

scalacOptions ++= Seq(
  "-Yrangepos",
)

libraryDependencies ++= List(
  "org.scalacheck" %% "scalacheck"   % "1.14.0" % "optional;provided",
  "org.gephi"      % "gephi-toolkit" % "0.9.2"  % "test" classifier "all",
  "junit"          % "junit"         % "4.12"   % "test",
  "org.scalatest"  %% "scalatest"    % "3.0.8"  % "test"
)
dependencyOverrides ++= Seq(
  "org.netbeans.modules" % "org-netbeans-core"              % "RELEASE90" % "test",
  "org.netbeans.modules" % "org-netbeans-core-startup-base" % "RELEASE90" % "test",
  "org.netbeans.modules" % "org-netbeans-modules-masterfs"  % "RELEASE90" % "test",
  "org.netbeans.api"     % "org-openide-util-lookup"        % "RELEASE90" % "test",
)

ThisBuild / resolvers ++= Seq(
  "NetBeans"         at "http://bits.netbeans.org/nexus/content/groups/netbeans/",
  "gephi-thirdparty" at "https://raw.github.com/gephi/gephi/mvn-thirdparty-repo/"
)
