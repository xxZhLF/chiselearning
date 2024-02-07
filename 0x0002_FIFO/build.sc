import mill._, scalalib._

object FIFO extends ScalaModule {

    override def scalaVersion = "2.13.10" 

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

    object test extends ScalaTests with TestModule.ScalaTest{

        override def sources = T.sources{
            super.sources() ++ Seq(
                PathRef(millSourcePath / "src" / "main" / "FIFO.scala"),
                PathRef(millSourcePath / "test" / "src" / "FIFOtester.scala"),
            )
        }

        override def ivyDeps = super.ivyDeps() ++ Agg(
            ivy"org.scalatest::scalatest:3.2.17",
            ivy"edu.berkeley.cs::chiseltest:0.6.2"
        )

    }

}