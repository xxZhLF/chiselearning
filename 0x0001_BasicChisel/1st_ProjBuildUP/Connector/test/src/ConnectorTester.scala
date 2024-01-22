import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ConnectorTester extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "NoProc"
    it should "pass" in {
        test(new Connector) { c =>
            c.io.in.poke(0.U)
            c.io.out.expect(0.U)
            c.clock.step()
            c.io.in.poke(1.U)
            c.io.out.expect(1.U)
            println("Test Complete.")
        }
    }
}