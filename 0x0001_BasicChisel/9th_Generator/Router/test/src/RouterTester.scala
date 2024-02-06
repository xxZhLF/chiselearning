import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import Router._

class Wrapper4Test [T <: Data](datyp: T, n: Int) extends Module{
    val io = IO (new Bundle{
        val iPort =  Input(Vec(n, datyp))
        val oPort = Output(Vec(n, datyp))
    })
    val router = Module(new Router(datyp, n))
    io <> router.io
}

class RouterTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "Router"
    it should "Pass" in {
        test(new Wrapper4Test(new Port(new Payload), 4)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            for (i <- 0 until dut.io.iPort.size){
                dut.io.iPort(i).addr.poke(0.U)
                dut.io.iPort(i).data.flag.poke(true.B)
                dut.io.iPort(i).data.data.poke(1234.U)
                dut.io.iPort(i).data.cs.poke(i.U)
            }
            for (i <- 0 until dut.io.oPort.size){
                dut.io.oPort(i).addr.expect(0.U)
                dut.io.oPort(i).data.flag.expect(true.B)
                dut.io.oPort(i).data.data.expect(1234.U)
                dut.io.oPort(i).data.cs.expect((dut.io.oPort.size-i-1).U)
            }
        }
    }
}