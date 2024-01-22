import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import Connector._

class ConnectorTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "Connector"
    it should "Pass" in {
        test(new Connector) { conn =>
            conn.io.in.poke(0.U)
            conn.io.out.expect(0.U)
            conn.io.in.poke(1.U)
            conn.io.out.expect(1.U)
            println("Test Complete.")
        }
    }
}