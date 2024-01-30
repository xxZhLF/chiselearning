import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import Vector._

class VectorTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "Vector"
    it should "Pass" in {
        test(new Vector).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            dut.io.in1.poke(1.U)
            dut.io.in2.poke(3.U)
            dut.io.in3.poke(5.U)
            dut.io.in4.poke(7.U)
            dut.io.out.expect(0.U)
            dut.clock.step()
            dut.io.idx.poke(0.U)
            dut.io.out.expect(1.U)
            dut.io.idx.poke(1.U)
            dut.io.out.expect(3.U)
            dut.io.idx.poke(2.U)
            dut.io.out.expect(5.U)
            dut.io.idx.poke(3.U)
            dut.io.out.expect(7.U)
            println("Test Complete.")
        }
    }
}