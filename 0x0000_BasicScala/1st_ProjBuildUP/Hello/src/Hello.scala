package Hello

object Hello { /* Main对象及入口函数 */
    def main(args: Array[String]): Unit = {
        println(hello())
    }
    def hello(): String = "Hello World!"
}