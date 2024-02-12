package Uart

import chisel3._
import chisel3.util.{DecoupledIO} /** // The defination of "DecoupledIO" is similar to the following
import javax.swing.InputMap
* class DecoupledIO[T <: Data](gen: T) extends Bundle { // Type parameter "T", upper bound is "Data".
*     val ready =  Input(Bool()) // All derived classes of Data can be used as parameter. "gen" is 
*     val valid = Output(Bool()) // used as parameter of the constructor and type is T. Since "Data" 
*     val bits  = Output(gen) // is the ancestor class of all type in Chisel. "gen" can be any type 
* } // in Chisel. "DecoupledIO" is designed from sender's viewpoint. Therefor, the receiver need to 
* // flip the IO direction with function Flipped.
*/

class UartIO extends DecoupledIO(UInt(8.W)){
    /* Rename DecoupledIO to UartIO */
}

trait CalcBaud { /* Number of clock cycles, required 
    to transmit one symble (one bit for this Uart) */
    val clkPerSym = (f: Int, b: Int) => (f + (b / 2)) / b /* clk4Start = */
    val clk4Start = (f: Int, b: Int) => (((3 * f) / 2) + (b / 2)) / b /* 1.5 * clkPerSym */
}

class Tx (freq: Int, baud: Int) extends Module with CalcBaud{
    val io = IO(new Bundle{
        val tx =  Output(UInt(1.W))
        val ch = Flipped(new UartIO)
    })  /* Tx receive date via "ch", 
        and sends serially via "tx",
        so "ch" is receiver(Flipped) */
    
    val sftReg = RegInit("b11_11111111_1".U)
    val clkCnt = RegInit(0.U(32.W))
    val bitCnt = RegInit(0.U(16.W))

    when (clkCnt =/= 0.U){
        clkCnt := clkCnt - 1.U
    }.otherwise{
        clkCnt := (clkPerSym(freq, baud) - 1).U
        when (bitCnt =/= 0.U){
            sftReg := "b1".U ## sftReg(10, 1)
            bitCnt := bitCnt - 1.U
        }.otherwise{
            when (io.ch.valid){
                sftReg := "b11".U ## io.ch.bits ## "b0".U
                bitCnt := (2 + 8 + 1).U
            }.otherwise{
                sftReg := "b11_11111111_1".U
            }
        }
    }

    io.ch.ready := (clkCnt === 0.U) && (bitCnt === 0.U)
    io.tx := sftReg(0)
}

class Rx (freq: Int, baud: Int) extends Module with CalcBaud{
    val io = IO(new Bundle{
        val rx = Input(UInt(1.W))
        val ch = new UartIO
    })

    val stabRx = RegNext(RegNext(io.rx, 1.U), 1.U)

    val sftReg = RegInit(0.U(8.W))
    val clkCnt = RegInit(0.U(32.W))
    val bitCnt = RegInit(0.U(16.W))
    val isByte = RegInit(false.B)

    when (clkCnt =/= 0.U){
        clkCnt := clkCnt - 1.U
    }.elsewhen (bitCnt =/= 0.U){
        clkCnt := (clkPerSym(freq, baud) - 1).U
        sftReg := stabRx ## (sftReg >> 1)
        bitCnt := bitCnt - 1.U
        when (bitCnt === 1.U){
            isByte := true.B
        }.otherwise{
        }
    }.elsewhen (stabRx === 0.U){
        clkCnt := (clk4Start(freq, baud) - 1).U
        bitCnt := 8.U
    }

    when (isByte && io.ch.ready){
        isByte := false.B
    }

    io.ch.bits  := sftReg
    io.ch.valid := isByte
}

class Uart (freq: Int, baud: Int) extends Module{
    val io = IO(new Bundle{
        val txI = Flipped(new UartIO)
        val txO = Output(UInt(1.W))
        val rxI =  Input(UInt(1.W))
        val rxO = new UartIO
    })

    val txPart = Module(new Tx(freq, baud))
    val rxPart = Module(new Rx(freq, baud))

    io.txI <> txPart.io.ch
    io.txO := txPart.io.tx
    rxPart.io.rx := io.rxI
    rxPart.io.ch <> io.rxO
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new Uart(1000000, 9600), Array("--target-dir", "HDL"))
}