package TSL /* Traffic Signal Lights */

import chisel3._
import chisel3.util.{switch, is}

object namespace {
    val gStr = "b001"
    val yStr = "b010"
    val rStr = "b100"
}; import namespace._

class TSL extends Module {
    val io = IO(new Bundle{
        val timeR =  Input(UInt(8.W))
        val timeY =  Input(UInt(8.W))
        val timeG =  Input(UInt(8.W))
        val state = Output(UInt(3.W))
    })

    object State extends ChiselEnum {
        val GREEN  = Value(gStr.U)
        val YELLOW = Value(yStr.U)
        val    RED = Value(rStr.U)
    }
    import State._

    val statOld = RegInit(RED)
    val statNew = RegInit(RED)

    val cnt = RegInit(0.U(8.W))
    cnt := cnt + 1.U

    switch(statNew) {
        is (RED) {
            when (cnt === (io.timeR - 1.U)) {
                statOld := statNew
                statNew := YELLOW
                cnt := 0.U
            }
        }
        is (GREEN) {
            when (cnt === (io.timeG - 1.U)) {
                statOld := statNew
                statNew := YELLOW
                cnt := 0.U
            }
        }
        is (YELLOW) {
            when (cnt === (io.timeY - 1.U)) {
                when (statOld.asUInt === GREEN.asUInt) {
                    statOld := statNew
                    statNew := RED
                }.elsewhen (statOld.asUInt === RED.asUInt) {
                    statOld := statNew
                    statNew := GREEN
                }
                cnt := 0.U
            }
        }
    }

    io.state := statNew.asUInt
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new TSL(), Array("--target-dir", "HDL"))
}