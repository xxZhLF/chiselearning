import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import Counter._

class CounterTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "Counter"
    it should "Pass" in {
        test(new Counter).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            dut.io.init.poke(0.U)
            for (i <- 1 to 512) {
                dut.clock.step(1)
                dut.io.out.expect((i%256).U)
            }
            println("Test Complete.")
        }
    }
}