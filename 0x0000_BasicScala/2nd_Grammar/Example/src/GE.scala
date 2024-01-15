package Fibo

trait NSIF {
    var buff: Array[Int]
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
            (arr, idx) => arr(idx-1) + arr(idx-2)
        }){
            print(s"${n} ")
        }
        println()
    }
}

object Example { 
    def main(args: Array[String]): Unit = {
        val fibo = new Fibo(10)
        fibo.show()
    }
}