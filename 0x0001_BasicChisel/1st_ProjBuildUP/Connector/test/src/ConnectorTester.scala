import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import Connector._

/* Since RawModule cannot be tested directly, a 
 * Wrapper of Module type is added for testing. */
class Wrapper4Test extends Module {
    val io = IO(new Bundle {
        val  in =  Input(UInt(1.W))
        val out = Output(UInt(1.W))
    })
    val conn = Module(new Connector)
    conn.io.in := io.in
    io.out := conn.io.out
}

class ConnectorTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "Connector"
    it should "Pass" in {
        test(new Wrapper4Test).withAnnotations(Seq(WriteVcdAnnotation))  { dut =>
            dut.io.in.poke(0.U)
            dut.io.out.expect(0.U)
            dut.clock.step()
            dut.io.in.poke(1.U)
            dut.io.out.expect(1.U)
            dut.clock.step()
            println("Test Complete.")
        }
    }
}