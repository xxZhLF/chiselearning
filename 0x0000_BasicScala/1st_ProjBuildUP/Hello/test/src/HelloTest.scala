package Hello
import utest._

object HelloTests extends TestSuite {
    def tests = Tests {
        test("test1") {
            val res = Hello.hello()
            assert(res == "Hello World!")
            res
        }
        test("test2") {
            val res = Hello.hello()
            assert(res.endsWith("World!"))
            res
        }
    }
}