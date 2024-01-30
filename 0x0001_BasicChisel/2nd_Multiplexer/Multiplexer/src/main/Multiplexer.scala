package Multiplexer

import chisel3._

class Multiplexer extends RawModule {
    val io = IO(new Bundle {
        val sel =  Input(Bool())
        val in1 =  Input(UInt(8.W))
        val in2 =  Input(UInt(8.W))
        val out = Output(UInt(8.W))
    })

    io.out := Mux(io.sel, io.in1, io.in2)
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new Multiplexer(), Array("--target-dir", "HDL"))
}