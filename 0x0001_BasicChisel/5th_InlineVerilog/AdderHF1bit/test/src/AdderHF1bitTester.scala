import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import AdderHF1bit._

class Wrapper4Test extends Module {
    val io = IO(new AdderPorts())
    val adder = Module(new AdderHF1bit())
    io <> adder.io
}

class AdderHF1bitTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "AdderHF1bit"
    it should "Pass" in {
        test(new Wrapper4Test).withAnnotations(Seq(WriteVcdAnnotation, /* Because of use of inline verilog. So need to */
                                                   VerilatorBackendAnnotation /* use verilator to compile verilog code */
                                                  )) { dut =>
            dut.io.a.poke(0.U)
            dut.io.b.poke(0.U)
            dut.io.s.expect((0^0).U)
            dut.io.a.poke(0.U)
            dut.io.b.poke(1.U)
            dut.io.s.expect((0^1).U)
            dut.io.a.poke(1.U)
            dut.io.b.poke(0.U)
            dut.io.s.expect((1^0).U)
            dut.io.a.poke(1.U)
            dut.io.b.poke(1.U)
            dut.io.s.expect((1^1).U)
            println("Test Complete.")
        }
    }
}