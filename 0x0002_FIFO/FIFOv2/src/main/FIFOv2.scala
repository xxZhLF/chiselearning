package FIFOv2

import chisel3._
import chisel3.util.{DecoupledIO}

abstract class ____FIFOv2 [T <: Data](datyp: T, depth: Int) extends Module {
    val io = IO(new FIFOv2IO(datyp))

    assert(depth > 0, "Depth of FIFO must to larger then 0")
}

class FIFOv2IO [T <: Data](datyp: T) extends Bundle {
    val enq = Flipped(new DecoupledIO(datyp))
    val deq = new DecoupledIO(datyp)
}

class FIFOv2 [T <: Data](datyp: T, depth: Int) extends ____FIFOv2 (datyp, depth){
    private class Buffer extends Module {
        val io = IO(new FIFOv2IO(datyp))

        val isFull = RegInit(false.B)
        val data = Reg(datyp)

        when (isFull){
            when (io.deq.ready){
                isFull := false.B
            }.otherwise {
            }
        }.otherwise {
            when (io.enq.valid){
                isFull := true.B
                data := io.enq.bits
            }.otherwise {
            }
        }

        io.enq.ready := !isFull
        io.deq.valid :=  isFull
        io.deq.bits  :=  data
    }

    private val buff = Array.fill(depth){
        Module(new Buffer)
    }
    
    for (i <- 1 until depth){
        buff(i).io.enq <> buff(i-1).io.deq
    }

    io.enq <> buff(0).io.enq
    io.deq <> buff(depth-1).io.deq
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new FIFOv2(UInt(16.W), 4), Array("--target-dir", "HDL"))
}