import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import EnDeCoder._

class Wrapper4Test extends Module {
    val io = IO(new Bundle{
        val in  =  Input(UInt(4.W))
        val out = Output(UInt(4.W))
    })

    val endecoder = Module(new EnDeCoder())
    io <> endecoder.io
}

class EnDeCoderTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "EnDeCoder"
    it should "Pass" in {
        test(new Wrapper4Test).withAnnotations(Seq(WriteVcdAnnotation)) { dut => 
            dut.io.in.poke("b0001".U)
            dut.io.out.expect("b0001".U)
            dut.io.in.poke("b0010".U)
            dut.io.out.expect("b0010".U)
            dut.io.in.poke("b0100".U)
            dut.io.out.expect("b0100".U)
            dut.io.in.poke("b1000".U)
            dut.io.out.expect("b1000".U)
            println("Test Complete.")
        }
    }
}