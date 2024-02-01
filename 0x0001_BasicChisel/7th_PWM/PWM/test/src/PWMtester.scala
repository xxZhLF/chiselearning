import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import sys.process._
import java.io.File

import PWM._

class PWMtester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "PWM"
    it should "Pass" in {
        for (duty <- Array(0, 25, 50, 75, 100)) {
            test(new PWM).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
                dut.io.period.poke(100.U)
                dut.io.duty.poke(duty.U)
                dut.clock.step(100)
            }
            val cfg = new File("PWM.gtkw")
            val ext = if (cfg.exists()) "-a PWM.gtkw" else " "
            s"gtkwave ./test_run_dir/PWM_should_Pass/PWM.vcd ${ext}".!!
        }
    }
}