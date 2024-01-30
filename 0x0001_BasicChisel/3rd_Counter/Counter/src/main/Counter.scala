package Counter

import chisel3._

class Counter extends Module {
    val io = IO(new Bundle {
        val init =  Input(UInt(8.W))
        val  out = Output(UInt(8.W))
    })

    val cnt = RegInit(io.init)

    cnt := cnt + 1.U
    io.out := cnt
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new Counter(), Array("--target-dir", "HDL"))
}