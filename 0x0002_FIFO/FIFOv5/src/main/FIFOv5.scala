package FIFOv5

import chisel3._
import chisel3.util.{log2Ceil}

import FIFOv2.{UnifiedIF}

class FIFOv5 [T <: Data](datyp: T, depth: Int) extends UnifiedIF (datyp, depth){
    def counter (depth: Int, inc: Bool): (UInt, UInt) = {
        val cnt = RegInit(0.U(log2Ceil(depth).W))
        val nxt = Mux(cnt === (depth - 1).U, 0.U, cnt + 1.U)
        when (inc){
            cnt := nxt
        }.otherwise {
        }
        (cnt, nxt)
    }

    val mem = SyncReadMem(depth, datyp, SyncReadMem.WriteFirst)

    val incR = WireDefault(false.B)
    val incW = WireDefault(false.B)
    val (ptrR, nxtR) = counter(depth, incR)
    val (ptrW, nxtW) = counter(depth, incW)

    val isEmpty = RegInit(true.B)
    val isFull = RegInit(false.B)

    /**
      * dataFromMem:   Output Register
      * dataFromMemOK: True when Output Register has not been read out, False otherwise
      * readFlg:       (!dataFromMemOK && ((ptrR =/= ptrW) || isFull)) ? true : false
      *                is equivalent to (!dataFromMemOK && ((ptrR =/= ptrW) || isFull))
      */
    val dataFromMem = Reg(datyp) 
    val dataFromMemOK = RegInit(false.B)
    val readFlg = WireDefault(false.B)

    val commit = WireDefault(false.B)

    val data = Wire(datyp)
    data := mem.read(ptrR)

    /**
      * !dataFromMemOK: Output Register has been read out,
      *                 data is no longer valid.
      *  ptrR === ptrW: 1) ptrR has caught up with ptrW, means dequeue the last data from FIFO. (EMPTY)
      *                 2) ptrW has caught up with ptrR, means failure to enqueue, as the FIFO is FULL.
      * isFull:         Eliminate the case 1)
      * WHY not use !isEmpty instead of ((ptrR =/= ptrW) || isFull)? I don't understand.
      */
    when (!dataFromMemOK && ((ptrR =/= ptrW) || isFull)){
        readFlg := true.B
        incR := true.B
        dataFromMem := data
        dataFromMemOK := true.B
        isEmpty := nxtR === ptrW
        isFull  := false.B
    }

    /** Reading
      * fire = ready && valid */
    when (io.deq.fire){ /* Both 
    ready and valid are asserted 
    data in output register has
    been read out, clear flag to
    prepare for next operation. */
        dataFromMemOK := false.B
    } 

    /**
      * FIFO is full after this enqueue, it 
      * will really full, if don't dequeue.
      * 
      * WHY not do next line here directly?
      * mem.write(ptrW, io.enq.bits)
      * I don't understand.
      */
    when (io.enq.fire){ /* Writing */
        isEmpty := false.B
        isFull  := (nxtW === ptrR) && !readFlg
        incW := true.B
        commit := true.B
    }

    when (commit){
        mem.write(ptrW, io.enq.bits)
    }

    /** io.deq.bits looks like it's driven by multiple signals
      *                                                +-----+
      *   +-------------+             Path1            |  M  |
      * <-+ io.deq.bits +--<--+--------------------+-<-+  E  |
      *   +-------------+     |       Path2        |   |  M  |
      *                       |   +----------+     |   +-----+
      *                       |   | Output   |     |          
      *                       +-<-+          +-<-/-+          
      *                           | Register |                
      *                           +----------+                
      * Path1 doesn't not exist in the generated Verilog code.
      * WHY does Path1 is needed in Chisel, I don't understand 
      */
    io.deq.bits  := data /* Path1 */
    io.deq.bits  := dataFromMem /* Path2 */
    io.deq.valid := dataFromMemOK
    io.enq.ready := !isFull
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new FIFOv5(UInt(16.W), 4), Array("--target-dir", "HDL"))
}