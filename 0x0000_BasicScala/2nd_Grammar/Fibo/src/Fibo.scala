package Fibo

trait NSIF { /* NonSense InterFace */

    var buff: Array[Int]

    val printWrapper = (n: Int, bufsz: Int) => {
        print(s"${n}")
        print(if (n == buff(bufsz-1)) "." else ",")
    }

    def Run (begin: Int, end: Int)
            (HowTo: (Array[Int], Int) => Int): Array[Int] = {
        for (i <- Range(begin, end)) {
            if (i < 2) {
                buff(i) = 1
            } else {
                buff(i) = HowTo(buff, i)
            }
        }
        buff
    }
}

class Fibo(stop_at: Int) extends NSIF {

    override var buff: Array[Int] = new Array[Int](stop_at)

    def show(): Any = {
        print("Fibo: ")
        for (n <- Run(0, stop_at){ 
            (AnythingIsOk4buff, AnythingIsOk4i) => 
            AnythingIsOk4buff(AnythingIsOk4i-1) + 
            AnythingIsOk4buff(AnythingIsOk4i-2)
        }){
            printWrapper(n, stop_at)
        }
        println()
    }
}

object Main { 
    def main(args: Array[String]): Unit = {
        val fibo = new Fibo(23){
            val useless = "Only Valid Within THIS Object!" 
            def testFun() = {
                println(useless)
            }
        }
        fibo.show()
        fibo.testFun()
    }
}