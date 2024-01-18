import mill._, scalalib._

trait ScalaVer extends ScalaModule {
    override def scalaVersion = "2.13.10" /* Why is it 2.13.10 here? ->
    https://mvnrepository.com/artifact/edu.berkeley.cs/chisel3-plugin */
}

trait ChiselVer extends ScalaModule {
    override def ivyDeps = Agg(
        ivy"edu.berkeley.cs::chisel3:3.6.0",
        ivy"edu.berkeley.cs::chiseltest:0.6.2",
        /* Why is this verison of chiseltest? 
           Reaseon is on the release page at version 0.6.2. */
    )
    override def scalacPluginIvyDeps = Agg(
        ivy"edu.berkeley.cs:::chisel3-plugin:3.6.0",
    )
}

object NoProc extends ScalaModule with ScalaVer with ChiselVer {
    override def scalacOptions = Seq(
        "-feature",
        "-deprecation"
    )
    // object test extends ScalaTests with ChiselVer {
    //     override def ivyDeps = super.ivyDeps() ++ Agg(
    //         ivy"com.lihaoyi::utest:0.7.11"
    //     )
    //     override def testFramework = "utest.runner.Framework"
    // }
}