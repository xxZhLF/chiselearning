package EnDeCoder

import chisel3._
import chisel3.util._

class Decoder extends RawModule {
    val io = IO(new Bundle {
        val  in =  Input(UInt(2.W))
        val out = Output(UInt(4.W))
    })

    io.out := "b1111".U
    switch (io.in) {
        is (0.U) { io.out := "b0001".U }
        is (1.U) { io.out := "b0010".U }
        is (2.U) { io.out := "b0100".U }
        is (3.U) { io.out := "b1000".U }
    }
}