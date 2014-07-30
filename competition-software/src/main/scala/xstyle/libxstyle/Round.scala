package xstyle.libxstyle

import collection.mutable
import scala.concurrent.duration._

import Constants._


import Helpers._

case class Round(riders:List[Rider]) extends AbstractRound(riders.size) {
  def randomStartingGroups:List[StartingGroup] = {
    def randomRiders = util.Random.shuffle(riders)
    val abstractRound = AbstractRound(riders.size)
    //TODO: StartingGroups sorted by age
    var ridersLeft = randomRiders
    var groups:List[StartingGroup] = Nil
    for( (groupSize,i) <- abstractRound.groupSizes.zipWithIndex ) {
      groups :+= StartingGroup(i, ridersLeft take groupSize)
      ridersLeft = ridersLeft drop groupSize
    }
    assert(ridersLeft == Nil)
    assert(abstractRound.groupSizes.sorted == groups.map(_.riders.size).sorted)
    groups
  }

  override def toString = s"Round(${riders.size} riders, ${groupCount} groups)"
}

object AbstractRound {
  def apply(n:Int) = new AbstractRound(n)
}
class AbstractRound(val competitorCount:Int) {
  require( advancingRiderCount <= maxGroupSize/2 )
  require( competitorCount >= advancingRiderCount, s"Not enough competitors. Need at least $advancingRiderCount." )

  def groupCount = divCeil(competitorCount, maxGroupSize)
  def runCount = competitorCount

  def isFinalRound = groupCount == 1

  def freePlaces = maxGroupSize - (competitorCount % maxGroupSize)
  def smallGroupCount = freePlaces % groupCount
  def bigGroupCount = groupCount - smallGroupCount

  def smallGroupSize = divFloor(competitorCount, groupCount)
  def bigGroupSize = divCeil(competitorCount, groupCount)

  def groupSizes = List.fill(bigGroupCount)(bigGroupSize) ::: List.fill(smallGroupCount)(smallGroupSize)

  assert(groupSizes.sum == competitorCount, s"$this: $groupSizes")
  assert( (competitorCount > maxGroupSize) == (groupCount > 1) )
  assert( if( groupCount > 1 ) smallGroupSize >= maxGroupSize / 2 else true,  this)

  def next = {
    require(groupSizes.forall( _ >= advancingRiderCount))
    AbstractRound(groupCount * advancingRiderCount)
  }

 def judgingAssignment = groupCount match {
   case 2 => List(List(1), List(0))
   case _ => maxDistancePairJudgingSlidingRotate(0 until groupCount)
 }
 def revJudgingAssignment = (0 until groupCount).map(gi => judgingAssignment.zipWithIndex.flatMap{case (g,i) if g contains gi => Some(i); case _ => None})

  //def canBeRunInParallel = competitorCount >= (judgingGroupShift + 1) * 2

  def totalRoundCount:Int = 1 + (if( groupCount > 1 ) next.totalRoundCount else 0)
  def totalRunCount:Int = competitorCount + (if( groupCount > 1 ) next.totalRunCount else 0)
  def totalGroupCount:Int = groupCount + (if( groupCount > 1 ) next.totalGroupCount else 0)

  def totalTimeNeeded = timePerCompetition +
    timePerRound * totalRoundCount +
    timePerGroup * totalGroupCount +
    timePerRun * totalRunCount

  def timeNeeded = timePerRound +
    timePerGroup * groupCount +
    timePerRun * runCount
}
