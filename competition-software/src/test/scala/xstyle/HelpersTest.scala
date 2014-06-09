package xstyle.libxstyle

import org.scalatest.FunSuite
import xstyle.libxstyle.Helpers._

class HelpersTest extends FunSuite {
  val l3 = List(1,2,3)

  test("divFloor") {
    assert(divFloor(5,2) == 2)
    assert(divFloor(4,2) == 2)
  }
  test("divCeil") {
    assert(divCeil(5,2) == 3)
    assert(divCeil(4,2) == 2)
  }

  test("rotate empty list") {
    assert(rotate(Nil,23) == Nil)
  }
  test("rotate by 0") {
    assert(rotate(l3,0) == l3)
  }
  test("rotate by 1") {
    assert(rotate(l3,1) == List(2,3,1))
  }
  test("rotate by 2") {
    assert(rotate(l3,2) == List(3,1,2))
  }
  test("rotate by listsize") {
    assert(rotate(l3,l3.size) == l3)
  }
  test("rotate by more than listsize") {
    assert(rotate(l3,8) == List(3,1,2))
  }

  test("repeat empty") {
    assert(repeat(Nil,23) == Nil)
  }
  test("repeat 0") {
    assert(repeat(l3,0) == l3)
  }
  test("repeat 1") {
    assert(repeat(l3,1) == l3)
  }
  test("repeat 2") {
    assert(repeat(l3,2) == l3++l3)
  }
  test("repeat 10") {
    assert(repeat(l3,10).size == l3.size*10)
  }

  test("slidingRotate on empty list") {
    assert(slidingRotate(Nil,delta=1,windowSize=1) == Nil)
  }
  test("slidingRotate with win=1 delta=0") {
    assert(slidingRotate(l3,delta=0,windowSize=1) == List(List(1), List(2), List(3)))
  }
  test("slidingRotate with win=1 delta=1") {
    assert(slidingRotate(l3,delta=1,windowSize=1) == List(List(2), List(3), List(1)))
  }
  test("slidingRotate with win=2 delta=0") {
    assert(slidingRotate(l3,delta=0,windowSize=2) == List(List(1, 2), List(2, 3), List(3, 1)))
  }
  test("slidingRotate with win=2 delta=1") {
    assert(slidingRotate(l3,delta=1,windowSize=2) == List(List(2, 3), List(3, 1), List(1, 2)))
  }
  
  val distances = Array(-1, -1, -1, 1, 1, 2, 2, 3, 3, 4, 4)
  for(n <- 3 to 10) {
    test(s"maxDistanceDeltaPairJudging n=$n") {
      assert(maxDistanceDeltaForPairJudging(n) == distances(n))
      assert(minDist(maxDistancePairJudgingSlidingRotate(0 until n)) == distances(n),
        maxDistancePairJudgingSlidingRotate(0 until n).toString
      )
    }
  }
}
