import mill._, scalalib._

/* 对象名称需要与文件夹（包）名称一致之后的 
   编译，运行，测试等一系列操作也依赖该名称 */
object Hello extends ScalaModule {
    def scalaVersion = "2.13.12" /* Scala 版本号 */
    object test extends ScalaTests { /* 测试模块 */
        def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.7.11") /* 测试模块依赖 */
        def testFramework = "utest.runner.Framework"      /* 测试模块框架 */
    }
}