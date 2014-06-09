package xstyle.libxstyle

import org.scalatest.FunSuite
import Helpers._

class LibXstyle extends FunSuite {
  val riders = fakeRiders(100)
  
  val ra = riders(0)
  val rb = riders(1)
  val rc = riders(2)
  val rd = riders(3)
  val re = riders(4)

  test("Round: generate StartingGroups 5") {
    val groups = Round(riders.take(5)).randomStartingGroups
    assert(groups.map(_.riders.size).sorted === List(5))
  }

  test("Round: generate StartingGroups 20") {
    val groups = Round(riders.take(20)).randomStartingGroups
    assert(groups.map(_.riders.size).sorted === List(10,10))
  }
  
  test("Round: generate StartingGroups 11") {
    val groups = Round(riders.take(11)).randomStartingGroups
    assert(groups.map(_.riders.size).sorted === List(5,6))
  }

  test("Round: generate StartingGroups 21") {
    val groups = Round(riders.take(21)).randomStartingGroups
    assert(groups.map(_.riders.size).sorted === List(7,7,7))
  }

  test("StartingGroup: advancing") {
    val group = StartingGroup(riders.take(5))
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

    assert( advanced.toSet === Set(rd, ra, rb) )
  }

  test("StartingGroup: ties") {
    val group = StartingGroup(riders.take(5))
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

    assert( advanced.toSet === Set(ra, rb, rc, rd) )
  }

  test("Round: assign judges") {
    val round = new AbstractRound(90)
      //assert(false)
  }
}
