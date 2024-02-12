import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import Uart._

class Wrapper4Tester extends Module {
/**
  *    +-----------------+       +------------------+  
  *    |                 |       |                  |  
  *    |      UART1      |       |       UART2      |  
  *    |                 |       |                  |  
  *    | iPort12      tx +-------+ rx       oPort12 |  
  *    |--------+        |       |         +--------|  
  *  <-+ ready  |        |       |         |  ready +<-
  *    |        |        |       |         |        |  
  *  ->+ valid  |        |       |         |  valid +->
  *    |        |        |       |         |        |  
  *  ->+ bit[0] |        |       |         | bit[0] +->
  *  ->+ bit[1] |        |       |         | bit[1] +->
  *    ~        |        |       |         |        ~  
  *  ->+ bit[7] |        |       |         | bit[7] +->
  *    |--------+        |       |         +--------|  
  *    |                 |       |                  |  
  *    +-----------------+       +------------------+  
  **/
    val io = IO(new Bundle{
        val iPort12 = Flipped(new UartIO)
        val oPort12 = new UartIO
        val iPort21 = Flipped(new UartIO)
        val oPort21 = new UartIO
    })

    val Uart1 = Module(new Uart(1000000, 9600))
    val Uart2 = Module(new Uart(1000000, 9600))

    Uart1.io.txI <> io.iPort12
    Uart2.io.rxO <> io.oPort12

    Uart2.io.txI <> io.iPort21
    Uart1.io.rxO <> io.oPort21

    Uart2.io.rxI := Uart1.io.txO
    Uart1.io.rxI := Uart2.io.txO
}

class UartTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "Uart"
    it should "Pass" in {
        val msg  = "Hello World!"
        val buff = new StringBuilder
        test(new Wrapper4Tester).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            dut.clock.setTimeout(0)
            for (ch <- msg.map(ch => ch.U)){
                while (dut.io.iPort12.ready.peek().litValue == 0){
                    dut.clock.step()
                }
                dut.io.iPort12.bits.poke(ch)
                dut.io.iPort12.valid.poke(true.B)
                dut.io.oPort12.ready.poke(true.B)
                while (dut.io.oPort12.valid.peek().litValue == 0){
                    dut.clock.step()
                }
                buff += dut.io.oPort12.bits.peek().litValue.toChar
            }
        }
        println(buff.toString)
    }
}