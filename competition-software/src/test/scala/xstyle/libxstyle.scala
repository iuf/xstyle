package xstyle.libxstyle

import org.scalatest.FunSuite

class LibXstyle extends FunSuite {

  val ra = Rider(1,"a")
  val rb = Rider(2,"b")
  val rc = Rider(3,"c")
  val rd = Rider(4,"d")
  val re = Rider(5,"e")
    
  val comp = Competition(riders = List(ra, rb, rc, rd, re))

  test("advancing") {
    val group = StartingGroup(comp.riders)
    val advanced = group.advancingRiders(
      judges = List("x"),
      judgingSheets=List(
        JudgingSheet("x",
          List(
            Placement(ra,rank=2),
            Placement(rb,rank=3),
            Placement(rc,rank=4),
            Placement(rd,rank=1),
            Placement(re,rank=5)
    ))))

    assert( advanced.toSet == Set(rd, ra, rb) )
  }

  test("ties") {
    val group = StartingGroup(comp.riders)
    val advanced = group.advancingRiders(
      judges = List("x", "y"),
      List(
        JudgingSheet("x",
          List(
            Placement(ra,1),
            Placement(rb,2),
            Placement(rc,3),
            Placement(rd,4),
            Placement(re,5)
          )),
        JudgingSheet("y",
          List(
            Placement(ra,1),
            Placement(rb,2),
            Placement(rc,4),
            Placement(rd,3),
            Placement(re,5)
          ))
      ))

    assert( advanced.toSet == Set(ra, rb, rc, rd) )
  }
}
