package EnDeCoder

import chisel3._
import chisel3.util._

class Encoder extends RawModule {
    val io = IO(new Bundle {
        val  in =  Input(UInt(4.W))
        val out = Output(UInt(2.W))
    })

    when (io.in === "b0001".U) {
        io.out := 0.U
    }.elsewhen (io.in === "b0010".U) {
        io.out := 1.U
    }.elsewhen (io.in === "b0100".U) {
        io.out := 2.U
    }.otherwise /* (io.in === "b1000".U) */ {
        io.out := 3.U
    }
}