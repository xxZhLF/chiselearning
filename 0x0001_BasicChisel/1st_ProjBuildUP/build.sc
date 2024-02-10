import mill._, scalalib._

object Connector extends ScalaModule {

    override def scalaVersion = "2.13.10" /* Why is it 2.13.10 here? ->
    https://mvnrepository.com/artifact/edu.berkeley.cs/chisel3-plugin */

    override def scalacOptions = Seq(
        "-feature",
        "-deprecation",
        "-language:reflectiveCalls"
    )

    override def ivyDeps = super.ivyDeps() ++ Agg(
        ivy"edu.berkeley.cs::chisel3:3.6.0",
    )

    override def scalacPluginIvyDeps = Agg(
        ivy"edu.berkeley.cs:::chisel3-plugin:3.6.0",
    )

    println("xxZh: You can do anything here using Scala.")

    override def sources = T.sources{
        super.sources() ++ Seq(
            PathRef(millSourcePath / "src" / "main" / "ConnectorTester.scala"),
        )/* Refer from https://mill-build.com/mill/Scala_Build_Examples.html */
    }/* millSourcePath is [~/chiselearning/0x0001_BasicChisel/1st_ProjBuildUP/Connector] */

    object test extends ScalaTests with TestModule.ScalaTest{
    /* ChiselTest built on ScalaTest. How 2 Using ScalaTest with mill?
     * https://www.scalatest.org/user_guide/using_scalatest_with_mill. */

        override def sources = T.sources{
            super.sources() ++ Seq(
                PathRef(millSourcePath / "src" / "ConnectorTester.scala"),
            )/* Refer from https://mill-build.com/mill/Scala_Build_Examples.html */
        }/* millSourcePath is [~/chiselearning/0x0001_BasicChisel/1st_ProjBuildUP/Connector/test] */

        override def ivyDeps = super.ivyDeps() ++ Agg(
            ivy"org.scalatest::scalatest:3.2.17",
            ivy"edu.berkeley.cs::chiseltest:0.6.2"
            /* Why is this verison of chiseltest? 
             * Reaseon is on the release page at version 0.6.2. */
        )

    }

}