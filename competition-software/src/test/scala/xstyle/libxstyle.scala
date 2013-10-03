package xstyle.lib

import org.scalatest.FunSuite

class CuboidTest extends FunSuite {

  test("advancing") {
    val c = Competition(List(
      Rider(1,"a"),
      Rider(2,"b"),
      Rider(3,"c"),
      Rider(4,"d")
    ))
    val r = c.toRound
    val groups = r.randomStartingGroups
    val advanced = groups.head.advancingRiders(
      List("x"),
      List(
        JudgingSheet("x",
          List(
            Placement(c.rider(2),2),
            Placement(c.rider(4),3),
            Placement(c.rider(1),4),
            Placement(c.rider(3),1)
    ))))

    assert( advanced.toSet == Set(c.rider(2), c.rider(3), c.rider(4)) )
  }

  test("ties") {
    val c = Competition(List(
      Rider(1,"a"),
      Rider(2,"b"),
      Rider(3,"c"),
      Rider(4,"d"),
      Rider(5,"e")
    ))
    println(c)
    val r = c.toRound
    val groups = r.randomStartingGroups
    val advanced = groups.head.advancingRiders(
      judges = List("x", "y"),
      List(
        JudgingSheet("x",
          List(
            Placement(c.rider(1),1),
            Placement(c.rider(2),2),
            Placement(c.rider(3),3),
            Placement(c.rider(4),4),
            Placement(c.rider(5),5)
          )),
        JudgingSheet("y",
          List(
            Placement(c.rider(1),1),
            Placement(c.rider(2),2),
            Placement(c.rider(3),4),
            Placement(c.rider(4),3),
            Placement(c.rider(5),5)
          ))
      ))

    assert( advanced.toSet == Set(c.rider(1), c.rider(2), c.rider(3), c.rider(4)) )
  }
}
