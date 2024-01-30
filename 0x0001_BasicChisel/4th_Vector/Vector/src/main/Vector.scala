package Vector

import chisel3._

class Vector extends Module {
    val io = IO(new Bundle{
        val idx =  Input(UInt(2.W))
        val in1 =  Input(UInt(8.W))
        val in2 =  Input(UInt(8.W))
        val in3 =  Input(UInt(8.W))
        val in4 =  Input(UInt(8.W))
        val out = Output(UInt(8.W))
    })

    val vec1 = VecInit(io.in1, io.in2, io.in3, io.in4)
    val vec2 = Wire(Vec(4, UInt(8.W)))
    val vec3 =  Reg(Vec(4, UInt(8.W)))

    vec2 := vec1
    vec3 := vec2

    io.out := vec3(io.idx)
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new Vector(), Array("--target-dir", "HDL"))
}