import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import FIFOv4._

class FIFOv4Tester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "FIFOv4"
    it should "Pass" in {
        test(new FIFOv4(UInt(16.W), 4)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            val base = 10
            for (i <- 0 to 4){
                if (dut.io.enq.ready.peek().litValue == 0){
                    dut.io.enq.valid.poke(false.B)
                    println("FIFO Full.")
                } else {
                    dut.io.enq.valid.poke(true.B)
                    dut.io.enq.bits.poke((base+i).U)
                    if (dut.io.deq.valid.peek().litValue == 0){
                        println("FIFO ReaderIO Preparing")
                    } else {
                        println("FIFO ReaderIO Ready")
                    }
                }
                dut.clock.step()
            }
            for (i <- 0 to 4){
                if (dut.io.deq.valid.peek().litValue == 0){
                    dut.io.deq.ready.poke(false.B)
                    println("FIFO Empty.")
                } else {
                    dut.io.deq.ready.poke(true.B)
                    dut.io.deq.bits.expect((base+i).U)
                }
                dut.clock.step()
            }
        }
        test(new FIFOv4(UInt(16.W), 4)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            val base = 8
            for (i <- 0 to 16){
                if (dut.io.enq.ready.peek().litValue == 0){
                    dut.io.enq.valid.poke(false.B)
                    println(f"[${i}%2d] FIFO Full.")
                } else {
                    dut.io.enq.valid.poke(true.B)
                    dut.io.enq.bits.poke((base+i).U)
                }
                dut.clock.step()
                if (dut.io.deq.valid.peek().litValue == 0){
                    dut.io.deq.ready.poke(false.B)
                    println(f"[${i}%2d] FIFO Empty.")
                } else {
                    dut.io.deq.ready.poke(true.B)
                    println(f"[${i}%2d] ReaderIO ${dut.io.deq.bits.peek().litValue}%2d")
                }
            }
        }
    }
}