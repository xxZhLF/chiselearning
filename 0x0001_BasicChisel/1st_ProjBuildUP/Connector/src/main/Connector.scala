package Connector

import chisel3._

/*
 * RawMdule doesn't generate clock 
 * and reset signals automatically 
 * but cause the ChiselTest fail. */
class Connector extends RawModule {
// class Connector extends Module {
    val io = IO(new Bundle {
        val  in =  Input(UInt(1.W))
        val out = Output(UInt(1.W))
    }) /* For  = means Initialize
          For := means Update    */
    io.out := io.in
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new Connector(), Array("--target-dir", "HDL"))
}