package Fibo

trait NSIF { /* NonSense InterFace */

    var buff: Array[Int]

    /**
      * Define a function with functional style
      */
    val printWrapper = (n: Int, bufsz: Int) => {
        print(s"${n}")
        print(if (n == buff(bufsz-1)) "." else ",")
    }

    /**
      * Define a function with dual Parameter List (PL).
      * + First list contains two parameters, the start
      *   and end of the for loop.
      * + Second list can only catain one parameter,
      *   when the parameter type is function value.
      *   + The function value takes an Array and an
      *     index as parameters and return an Array.
      * @param begin Start of for loop
      * @param end   End of for loop
      * @param HowTo Callback function
      * @return buff Array
      */
    def Run /* 1st Parameter List */ (begin: Int, end: Int) 
            /* 2nd Parameter List */ (HowTo: (Array[Int], Int) => Int): Array[Int] = {
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
        print("Fibo: ") /* Call the dual PL */
        for (n <- Run(0, stop_at){ /* function */
            (AnythingIsOk4buff, AnythingIsOk4i) => /* buff and index as PL */
            AnythingIsOk4buff(AnythingIsOk4i-1) +  /*   buff(i-1) */
            AnythingIsOk4buff(AnythingIsOk4i-2)    /* + buff(i-2) */
        } /* The function value defined in {} */ ){
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
        } /* Expend member and method of Fibo Class by {} */
        fibo.show()
        fibo.testFun()
    }
}