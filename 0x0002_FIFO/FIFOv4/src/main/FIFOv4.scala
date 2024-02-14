package FIFOv4

import chisel3._
import chisel3.util.{switch, is, log2Ceil}

import FIFOv2.{FIFOv2IO => FIFOv4IO, UnifiedIF}

class FIFOv4 [T <: Data](datyp: T, depth: Int) extends UnifiedIF (datyp, depth){
    def counter (depth: Int, inc: Bool): (UInt, UInt) = {
        val cnt = RegInit(0.U(log2Ceil(depth).W))
        val nxt = Mux(cnt === (depth - 1).U, 0.U, cnt + 1.U)
        when (inc){
            cnt := nxt
        }.otherwise {
        }
        (cnt, nxt)
    }

    val mem = Reg(Vec(depth, datyp))

    val incR = WireDefault(false.B)
    val incW = WireDefault(false.B)
    val (ptrR, nxtR) = counter(depth, incR)
    val (ptrW, nxtW) = counter(depth, incW)

    val isEmpty = RegInit(true.B)
    val isFull = RegInit(false.B)

    val commit = WireDefault(false.B)

    switch (io.enq.valid ## io.deq.ready){
        is ("b00".U){
            /** No Reading or Writing 
              * ready is for deq reading
              * valid is for enq wreting 
              **/
        }
        is ("b01".U){ /* Reading */
            when (!isEmpty){
                isFull  := false.B
                isEmpty := nxtR === ptrW
                incR := true.B
            }.otherwise {
                /* FIFO is empty */
            }
        }
        is ("b10".U){ /* Writing */
            when (!isFull){
                commit := true.B
                isEmpty := false.B
                isFull := nxtW === ptrR
                incW := true.B
            }.otherwise {
                /* FIFO is full */
            }
        }
        is ("b11".U){ /* Reading and Writing */
            when (!isEmpty){ /* For Reading */
                isFull := false.B
                when (isFull) {
                    isEmpty := false.B
                }.otherwise {
                    isEmpty := nxtR === nxtW
                }
                incR := true.B
            }.otherwise {
                /* FIFO is Empty */
            }
            when (!isFull){ /* For Writing */
                commit := true.B
                isEmpty := false.B
                when (isEmpty){
                    isFull := false.B
                }.otherwise {
                    isFull := nxtW === nxtR
                }
                incW := true.B
            }.otherwise{
                /* FIFO is FULL */
            }
        }
    }

    when (commit){
        mem(ptrW) := io.enq.bits
    }

    io.deq.bits  := mem(ptrR)
    io.enq.ready := !isFull
    io.deq.valid := !isEmpty
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new FIFOv4(UInt(16.W), 4), Array("--target-dir", "HDL"))
}