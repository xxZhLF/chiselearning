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

    override def sources = T.sources{
        super.sources() ++ Seq(
            PathRef(millSourcePath / "src" / "main" / "FIFO.scala"),
            PathRef(millSourcePath / os.up / "FIFOv2" / "src" / "main" / "FIFOv2.scala"),
            PathRef(millSourcePath / os.up / "FIFOv3" / "src" / "main" / "FIFOv3.scala"),
            PathRef(millSourcePath / os.up / "FIFOv4" / "src" / "main" / "FIFOv4.scala"),
            PathRef(millSourcePath / os.up / "FIFOv5" / "src" / "main" / "FIFOv5.scala"),
        )
    }

    // override def mainClass: mill.T[Option[String]] = Some("FIFO.Main")
    // override def mainClass: mill.T[Option[String]] = Some("FIFOv2.Main")
    // override def mainClass: mill.T[Option[String]] = Some("FIFOv3.Main")
    // override def mainClass: mill.T[Option[String]] = Some("FIFOv4.Main")
    override def mainClass: mill.T[Option[String]] = Some("FIFOv5.Main")
    
    object test extends ScalaTests with TestModule.ScalaTest{

        override def sources = T.sources{
            super.sources() ++ Seq(
                PathRef(millSourcePath / "src" / "FIFOv3Tester.scala"),
                PathRef(millSourcePath / os.up / os.up / "FIFOv2" / "test" / "src" / "FIFOv2Tester.scala"),
                PathRef(millSourcePath / os.up / os.up / "FIFOv3" / "test" / "src" / "FIFOv3Tester.scala"),
                PathRef(millSourcePath / os.up / os.up / "FIFOv4" / "test" / "src" / "FIFOv4Tester.scala"),
                PathRef(millSourcePath / os.up / os.up / "FIFOv5" / "test" / "src" / "FIFOv5Tester.scala"),
            )
        }

        override def ivyDeps = super.ivyDeps() ++ Agg(
            ivy"org.scalatest::scalatest:3.2.17",
            ivy"edu.berkeley.cs::chiseltest:0.6.2"
        )

    }

}