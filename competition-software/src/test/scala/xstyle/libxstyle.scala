package xstyle.libxstyle

import org.scalatest.FunSuite

class LibXstyle extends FunSuite {

  test("advancing") {
    val comp = Competition(riders = List(
      Rider(1,"a"),
      Rider(2,"b"),
      Rider(3,"comp"),
      Rider(4,"d")
    ))
    val round = comp.toRound
    val groups = round.randomStartingGroups
    val advanced = groups.head.advancingRiders(
      judges = List("x"),
      judgingSheets=List(
        JudgingSheet("x",
          List(
            Placement(comp.rider(2),2),
            Placement(comp.rider(4),3),
            Placement(comp.rider(1),4),
            Placement(comp.rider(3),1)
    ))))

    assert( advanced.toSet == Set(comp.rider(2), comp.rider(3), comp.rider(4)) )
  }

  test("ties") {
    val comp = Competition(List(
      Rider(1,"a"),
      Rider(2,"b"),
      Rider(3,"comp"),
      Rider(4,"d"),
      Rider(5,"e")
    ))
    val r = comp.toRound
    val groups = r.randomStartingGroups
    val advanced = groups.head.advancingRiders(
      judges = List("x", "y"),
      List(
        JudgingSheet("x",
          List(
            Placement(comp.rider(1),1),
            Placement(comp.rider(2),2),
            Placement(comp.rider(3),3),
            Placement(comp.rider(4),4),
            Placement(comp.rider(5),5)
          )),
        JudgingSheet("y",
          List(
            Placement(comp.rider(1),1),
            Placement(comp.rider(2),2),
            Placement(comp.rider(3),4),
            Placement(comp.rider(4),3),
            Placement(comp.rider(5),5)
          ))
      ))

    assert( advanced.toSet == Set(comp.rider(1), comp.rider(2), comp.rider(3), comp.rider(4)) )
  }
}
