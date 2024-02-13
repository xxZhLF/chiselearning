package FIFOv2

import chisel3._
import chisel3.util.{DecoupledIO}

abstract class FIFOv2 [T <: Data](datyp: T, depth: Int) extends Module {
    val io = IO(new FFIO(datyp))

    assert(depth > 0, "Depth of FIFO must to larger then 0")
}

class FFIO [T <: Data](datyp: T) extends Bundle {
    val enq = Flipped(new DecoupledIO(datyp))
    val deq = new DecoupledIO(datyp)
}

class FIFO [T <: Data](datyp: T, depth: Int) extends FIFOv2 (datyp, depth){
    private class ____FIFO extends Module {
        val io = IO(new FFIO(datyp))

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
        Module(new ____FIFO)
    }
    
    for (i <- 1 until depth){
        buff(i).io.enq <> buff(i-1).io.deq
    }

    io.enq <> buff(0).io.enq
    io.deq <> buff(depth-1).io.deq
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new FIFO(UInt(16.W), 4), Array("--target-dir", "HDL"))
}