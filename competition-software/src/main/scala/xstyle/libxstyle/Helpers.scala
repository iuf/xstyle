package xstyle.libxstyle

import scala.io._

object Helpers {
  def divFloor(a:Int, b:Int) = a / b
  def divCeil(a:Int, b:Int) = if(a % b == 0) a / b else (a + ( b - (a % b) )) / b

  def rotate[T](xs:Iterable[T], delta:Int) = if(xs.size == 0) xs else xs.drop(delta % xs.size) ++ xs.take(delta % xs.size)
  def repeat[T](xs:Iterable[T], count:Int):Iterable[T] = if(count <= 1 || count == 0) xs else xs ++ repeat(xs, count-1)
  def slidingRotate[T](xs:Iterable[T], delta:Int, windowSize:Int) = repeat(rotate(xs,delta),2).toList.sliding(windowSize).take(xs.size).toList


  def minDist(xs:List[List[Int]]):Int = xs.zipWithIndex.map{case (win,i) => win.map(w => (i-w).abs).min}.min

  def maxDistanceDeltaForPairJudgingBRUTEFORCE(elements:Int, windowSize:Int):Int = {
    val res = (0 until elements).toList.combinations(windowSize).toList.combinations(elements).flatMap(_.permutations).map(minDist)
    if(res.nonEmpty) res.max else 0
  }

  def maxDistanceDeltaForPairJudging(elements:Int):Int = {
    val windowSize = 2
    (elements-1)/2
  }


  def maxDistancePairJudgingSlidingRotate[T](xs:Iterable[T]) = {
    val delta = maxDistanceDeltaForPairJudging(xs.size)
    slidingRotate(xs, delta, 2)
  }

  def fakeRiders(n:Int) = List.tabulate(n)(i => Rider(i, s"R$i"))

  def readRiders(fileName:String) = Source.fromFile(fileName).getLines.filter(_.nonEmpty).zipWithIndex.map{case (name,id) => Rider(id,name)}.toList

}
