import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import Multiplexer._

class Wrapper4Test extends Module {
    val io = IO(new Bundle {
        val sel =  Input(Bool())
        val in1 =  Input(UInt(8.W))
        val in2 =  Input(UInt(8.W))
        val out = Output(UInt(8.W))
    })
    val multiplexer = Module(new Multiplexer)
    if (true) {
        io <> multiplexer.io
    } else {
        multiplexer.io.sel := io.sel
        multiplexer.io.in1 := io.in1
        multiplexer.io.in2 := io.in2
        io.out := multiplexer.io.out
    }
}

class MultiplexerTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "Multiplexer"
    it should "Pass" in {
        test(new Wrapper4Test).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            dut.io.in1.poke(4.U)
            dut.io.in2.poke(7.U)
            dut.io.sel.poke(true.B)
            dut.io.out.expect(4.U)
            dut.clock.step()
            dut.io.sel.poke(false.B)
            dut.io.out.expect(7.U)
            dut.clock.step()
            println("Test Complete.")
        }
    }
}