import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.constructor.{Future}

import Uart._

class UartTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "Uart"
    it should "Pass" in {
        test(new Uart(1000000, 9600)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            
        }
    }
}