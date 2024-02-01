package EnDeCoder

import chisel3._

class EnDeCoder extends RawModule {
    val io = IO(new Bundle {
        val  in =  Input(UInt(4.W))
        val out = Output(UInt(4.W))
    })

    val encoder = Module(new Encoder())
    val decoder = Module(new Decoder())

    encoder.io.in := io.in
    io.out := decoder.io.out
    decoder.io.in := encoder.io.out

}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new EnDeCoder(), Array("--target-dir", "HDL"))
}