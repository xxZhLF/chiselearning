package FIFO

import chisel3._

class WriterIO(datsz: Int) extends Bundle {
    val writing =  Input(Bool())
    val content =  Input(UInt(datsz.W))
    val isFull  = Output(Bool())
}

class ReaderIO(datsz: Int) extends Bundle {
    val reading =  Input(Bool())
    val content = Output(UInt(datsz.W))
    val isEmpty = Output(Bool())
}

class Buffer(datsz: Int) extends Module {
    val io = IO(new Bundle{
        val enq = new WriterIO(datsz)
        val deq = new ReaderIO(datsz)
    })

    object State extends ChiselEnum {
        val EMPTY, FULL = Value
    }
    import State._

    val stat = RegInit(EMPTY)
    val data = RegInit(0.U(datsz.W))

    when (stat === EMPTY){
        when (io.enq.writing){
            stat := FULL
            data := io.enq.content
        }.otherwise{
        }
    }.elsewhen(stat === FULL){
        when (io.deq.reading){
            stat := EMPTY
            data := 0.U
        }.otherwise{
        }
    }.otherwise{
    }

    io.enq.isFull  := stat === FULL
    io.deq.isEmpty := stat === EMPTY
    io.deq.content := data
}

class FIFO(datsz: Int, depth: Int) extends Module{
    val io = IO(new Bundle{
        val enq = new WriterIO(datsz)
        val deq = new ReaderIO(datsz)
    })

    val buff = Array.fill(depth)(
        Module(new Buffer(datsz))
    )

    for (i <- 1 until depth){
        buff(i).io.enq.content   :=  buff(i-1).io.deq.content
        buff(i).io.enq.writing   := ~buff(i-1).io.deq.isEmpty
        buff(i-1).io.deq.reading := ~buff(i).io.enq.isFull 
    }

    io.enq <> buff(0).io.enq
    io.deq <> buff(depth-1).io.deq
}

object Main extends App {
    circt.stage.ChiselStage.emitSystemVerilogFile(new FIFO(16, 4), Array("--target-dir", "HDL"))
}