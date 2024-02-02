import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import TSL._
import TSL.namespace._

class TSLtester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "TSL"
    it should "Pass" in {
        test(new TSL).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            dut.io.timeR.poke(7.U)
            dut.io.timeG.poke(7.U)
            dut.io.timeY.poke(4.U)
            dut.io.state.expect(rStr.U)
            for (i <- Range(0, 4)){
                dut.clock.step(7)
                dut.io.state.expect(yStr.U)
                dut.clock.step(4)
                dut.io.state.expect(gStr.U)
                dut.clock.step(7)
                dut.io.state.expect(yStr.U)
                dut.clock.step(4)
                dut.io.state.expect(rStr.U)
            }
        }
    }
}