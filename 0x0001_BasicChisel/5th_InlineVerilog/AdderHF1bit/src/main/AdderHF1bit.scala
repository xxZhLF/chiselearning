package AdderHF1bit

import chisel3._
import chisel3.util.HasBlackBoxInline

class AdderPorts extends Bundle {
    val a =  Input(UInt(1.W))
    val b =  Input(UInt(1.W))
    val s = Output(UInt(1.W))
}

class InlineAdderHF1bit extends HasBlackBoxInline {
    val io = IO(new AdderPorts())
    setInline("InlineVerilog.v", s"""
        |module InlineAdderHF1bit (
        |   input  wire a,
        |   input  wire b,
        |   output wire s 
        |);
        |   assign s = a ^ b;
        |endmodule
    """.stripMargin)
}

class AdderHF1bit extends RawModule {
    val io = IO(new AdderPorts())
    val adderHF1bit = Module(new InlineAdderHF1bit())
    io <> adderHF1bit.io
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new AdderHF1bit(), Array("--target-dir", "HDL"))
}