import mill._, scalalib._

object Fibo extends ScalaModule {
    def scalaVersion = "2.13.12"
    def scalacOptions = Seq(
        "-feature",
        "-language:reflectiveCalls"
    )
}