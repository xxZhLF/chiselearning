package PWM

import chisel3._

class PWM extends Module {
    val io = IO(new Bundle{
        val period =  Input(UInt(8.W))
        val   duty =  Input(UInt(8.W))
        val driver = Output(UInt(1.W))
    })

    def pwm(period: UInt, duty: UInt): Bool = {
        val cnt = RegInit(0.U(8.W))
        when(cnt === period) {
            cnt := 0.U
        }.otherwise {
            cnt := cnt + 1.U
        }
        cnt > duty
    }

    io.driver := pwm(io.period, io.duty).asUInt
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new PWM(), Array("--target-dir", "HDL"))
}