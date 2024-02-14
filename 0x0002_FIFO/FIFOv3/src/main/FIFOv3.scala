package FIFOv3

import chisel3._
import chisel3.util.{switch, is}

import FIFOv2.{FIFOv2IO => FIFOv3IO, UnifiedIF}

class FIFOv3 [T <: Data](datyp: T, depth: Int) extends UnifiedIF (datyp, depth){
    private class DoubleBuffer[T <: Data](datyp: T) extends Module {
        val io = IO(new FIFOv3IO(datyp))

        object State extends ChiselEnum {
            val EMPTY, ONE, TWO = Value
        }
        import State._

        val stat = RegInit(EMPTY)
        val data = Reg(datyp)
        val shadow = Reg(datyp)

        switch (stat) {
            is (EMPTY) {
                when (io.enq.valid){
                    stat := ONE
                    data := io.enq.bits
                }.otherwise {
                }
            }
            is (ONE) {
                when (io.deq.ready && !io.enq.valid) {
                    stat := EMPTY
                }.elsewhen (io.deq.ready && io.enq.valid){
                    stat := ONE
                    data := io.enq.bits
                }.elsewhen (!io.deq.ready && io.enq.valid){
                    stat := TWO
                    shadow := io.enq.bits
                }.otherwise {
                }
            }
            is (TWO) {
                when (io.deq.ready){
                    data := shadow
                    stat := ONE
                }.otherwise{
                }
            }
        }

        io.enq.ready := (stat === EMPTY || stat === ONE)
        io.deq.valid := (stat === ONE   || stat === TWO)
        io.deq.bits  :=  data
    }

    private val dbuff = Array.fill((depth + 1) / 2) {
        Module(new DoubleBuffer(datyp))
    }

    for (i <- 1 until (depth + 1) / 2){
        dbuff(i).io.enq <> dbuff(i-1).io.deq
    }

    io.enq <> dbuff(0).io.enq
    io.deq <> dbuff(((depth + 1) / 2) - 1).io.deq
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new FIFOv3(UInt(16.W), 4), Array("--target-dir", "HDL"))
}