import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import FIFOv2.UnifiedIF
import FIFOv3._
import FIFOv5._

class Wrapper4Tester[T <: Data](datyp: T, depth: Int) extends UnifiedIF (datyp, depth){
    val memfifo = Module(new FIFOv5(datyp, depth))
    val dbufifo = Module(new FIFOv3(datyp, 2))

    memfifo.io.enq <> io.enq
    memfifo.io.deq <> dbufifo.io.enq  
    io.deq <> dbufifo.io.deq
}

class FIFOv5Tester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "FIFOv5"
    it should "Pass" in {
        test(new Wrapper4Tester(UInt(16.W), 4)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            val base = 10
            for (i <- 0 to 6){
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
            var i = 0
            while (i <= 6){
                if (dut.io.deq.valid.peek().litValue == 0){
                    dut.io.deq.ready.poke(false.B)
                    println("FIFO Empty.")
                } else {
                    dut.io.deq.ready.poke(true.B)
                    dut.io.deq.bits.expect((base+i).U)
                    i += 1
                }
                dut.clock.step()
            }
        }
   }
}