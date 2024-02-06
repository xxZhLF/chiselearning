package Router

import chisel3._

class Payload extends Bundle {
    val data = UInt(32.W)
    val flag = Bool()
    val cs = UInt(4.W)
}

class Port [T <: Data](private val datyp: T) extends Bundle {
    val addr = UInt(8.W)
    val data = datyp.cloneType
}

class Router [T <: Data](datyp: T, n: Int) extends RawModule{
    val io = IO (new Bundle{
        val iPort =  Input(Vec(n, datyp))
        val oPort = Output(Vec(n, datyp))
    })
    for (i <- 0 until n) {
        io.oPort(i) <> io.iPort(n-i-1)
    }
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new Router(new Port(new Payload), 4), Array("--target-dir", "HDL"))
}