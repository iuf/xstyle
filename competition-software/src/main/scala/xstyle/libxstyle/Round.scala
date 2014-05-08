package xstyle.libxstyle

import collection.mutable
import scala.concurrent.duration._

import Constants._


import Helpers._

case class Round(riders:List[Rider]) {
  def randomStartingGroups:List[StartingGroup] = {
    def randomRiders = util.Random.shuffle(riders)
    val abstractRound = AbstractRound(riders.size)
    //TODO: StartingGroups sorted by age
    var ridersLeft = randomRiders
    var groups:List[StartingGroup] = Nil
    for( groupSize <- abstractRound.groupSizes ) {
      groups ::= StartingGroup(ridersLeft take groupSize)
      ridersLeft = ridersLeft drop groupSize
    }
    assert(ridersLeft == Nil)
    assert(abstractRound.groupSizes.sorted == groups.map(_.riders.size).sorted)
    groups
  }
}

case class AbstractRound(competitorCount:Int) {
  require( advancingRiderCount <= maxGroupSize/2 )
  require( competitorCount >= advancingRiderCount, s"Not enough competitors. Need at least $advancingRiderCount." )

  private def divFloor(a:Int, b:Int) = a / b
  private def divCeil(a:Int, b:Int) = if(a % b == 0) a / b else (a + ( b - (a % b) )) / b

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

  def next= {
    require(groupSizes.forall( _ >= advancingRiderCount))
    AbstractRound(groupCount * advancingRiderCount)
  }
  
  //def canBeRunInParallel = competitorCount >= (judgingGroupShift + 1) * 2
  def judgesForGroup(groupId:Int, parallel:Boolean):List[Int] = {
    val optimalShift = groupCount / 2 - judgingGroupCount / 2
    if( parallel ){ ???}
    else {
      if( isFinalRound ) Nil
      ??? // find optimal group shift
    }
  }

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
