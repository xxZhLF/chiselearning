import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import FIFO._

class CounterTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "FIFO"
    it should "Pass" in {
        test(new FIFO(16, 4)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            val base = 10
            for (i <- 0 to 4){
                if (dut.io.enq.isFull.peek().litValue == 1){
                    dut.io.enq.writing.poke(false.B)
                    println("FIFO Full.")
                } else {
                    dut.io.enq.writing.poke(true.B)
                    dut.io.enq.content.poke((base+i).U)
                    if (dut.io.deq.isEmpty.peek().litValue == 1){
                        println("FIFO ReaderIO Preparing")
                    } else {
                        println("FIFO ReaderIO Ready")
                    }
                }
                dut.clock.step(2)
            }
            for (i <- 0 to 4){
                if (dut.io.deq.isEmpty.peek().litValue == 1){
                    dut.io.deq.reading.poke(false.B)
                    println("FIFO Empty.")
                } else {
                    dut.io.deq.reading.poke(true.B)
                    dut.io.deq.content.expect((base+i).U)
                }
                dut.clock.step(2)
            }
        }
        test(new FIFO(16, 4)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            val base = 8
            for (i <- 0 to 16){
                if (dut.io.enq.isFull.peek().litValue == 1){
                    dut.io.enq.writing.poke(false.B)
                    println(f"[${i}%2d] FIFO Full.")
                } else {
                    dut.io.enq.writing.poke(true.B)
                    dut.io.enq.content.poke((base+i).U)
                }
                dut.clock.step(2)
                if (dut.io.deq.isEmpty.peek().litValue == 1){
                    dut.io.deq.reading.poke(false.B)
                    println(f"[${i}%2d] FIFO Empty.")
                } else {
                    dut.io.deq.reading.poke(true.B)
                    println(f"[${i}%2d] ReaderIO ${dut.io.deq.content.peek().litValue}%2d")
                }
            }
        }
    }
}