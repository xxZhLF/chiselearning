package Container

object Container {
    def main(args: Array[String]): Unit = {
        UsageOfBuilder()
        UsageOfFM()
        UsageOfVector()
        UsageOfSet()
        UsageOfMap()
    }
    def UsageOfBuilder(): Any = {
        val arr = Array.newBuilder[Int]
        for (i <- Range(0, 10)){
            arr += i
        }
        print("Usage of Builder: ")
        for (ele <- arr.result()) {
            print(s"${ele} ")
        }   
        println()
    }
    def UsageOfFM(): Any = {
        val arr1 = Array.fill(9)(7)
        val arr2 = Array.tabulate(7)(n => n * n)
        val arrs = arr1.map(n => n * 2).take(8).drop(4) ++ arr2.filter(n => n % 2 == 0)
        print("Usage of Factory Method: ")
        arrs.foreach(n => print(s"${n} "))
        println()
        println(s"    take(6):     ${arrs.take(6).mkString(" ")}")
        println(s"    drop(2):     ${arrs.drop(2).mkString(" ")}")
        println(s"    slice(1, 7): ${arrs.slice(1, 7).mkString(" ")}")
        println(s"    foldLeft(0)((x, y) => x + y): ${arrs.foldLeft(0)((x, y) => x + y)}")
        val grps = arrs.groupBy(n => n == 14)
        println(s"    group(0): ${grps( true).mkString(" ")}")
        println(s"    group(1): ${grps(false).mkString(" ")}")
    }
    def UsageOfVector(): Any = {
        var vec = Vector.fill(1)(0)
        for (i <- 1 to 9 by  2){
            vec = vec :+ i
        }
        for (i <- 9 to 1 by -2){
            vec = i +: vec
        }
        println(s"Usage of Immutable Vector: ${(vec ++ Vector(0, 0, 0, 0)).mkString(" ")}")
        println(s"   updated(5, 777): ${vec.updated(5, 777).mkString(" ")}")
        println(s"   head: ${vec.head}")
        println(s"   tail: ${vec.tail.mkString(" ")}")
    }
    def UsageOfSet(): Any = {
        val set = Set(1, 2, 3)
        println(s"Usage of Immutable Set: ${(set ++ Set(4, 5, 6)).mkString(" ")}")
        println(s"   set + 3: ${(set + 3).mkString(" ")}")
        println(s"   set + 4: ${(set + 4).mkString(" ")}")
        println(s"   set - 1: ${(set - 1).mkString(" ")}")
        println(s"   set - 0: ${(set - 0).mkString(" ")}")
    }
    def UsageOfMap(): Any = {
        val map1 = Map("one" -> 1, "two" -> 2, "three" -> 3)
        print("Usage of Map: ")
        for ((k, v) <- map1) print(s"(${k}, ${v}) ")
        println()
        val map2 = collection.mutable.Map.newBuilder[String, Int]
        map2 += ("four" -> 4)
        map2 += ("five" -> 5)
        map2 += ("six" -> 6)
        val map3 = (map1 ++ map2.result()).to(collection.mutable.Map)
        map3 += ("seven" -> 7)
        print(s"    Combination & Append: ")
        for ((k, v) <- map3) print(s"(${k}, ${v}) ")
        println()
    }
}