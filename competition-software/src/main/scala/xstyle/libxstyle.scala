package xstyle.libxstyle

import collection.mutable
import scala.concurrent.duration._

object Constants {
  val timeBeforeCompetition  = Duration( 0, MINUTES)
  val timeBeforeRound        = Duration(10, MINUTES)
  val timeBeforeGroup        = Duration( 5, MINUTES)
  val timeBeforeRun          = Duration( 0, MINUTES)
  val lengthOfRun            = Duration( 2, MINUTES)
  val timeAfterRun           = Duration( 1, MINUTES)
  val timeAfterGroup         = Duration( 5, MINUTES)
  val timeAfterRound         = Duration(15, MINUTES)
  val timeAfterCompetition   = Duration(20, MINUTES)

  val timePerCompetition = timeBeforeCompetition       + timeAfterCompetition
  val timePerRound       = timeBeforeRound             + timeAfterRound
  val timePerGroup       = timeBeforeGroup             + timeAfterGroup
  val timePerRun         = timeBeforeRun + lengthOfRun + timeAfterRun

  val maxGroupSize         = 10
  val advancingRiderCount  = 3
  
  val judgingGroupCount    = 2
}

import Constants._

object Helpers {
  def rotate[T](xs:Iterable[T], delta:Int) = xs.drop(delta % xs.size) ++ xs.take(delta % xs.size)
}

import Helpers._

object Rider {
  var currentId = 0
  def nextAutoId() = {currentId += 1; currentId}
  def apply(name:String):Rider = Rider(nextAutoId(), name)
}
case class Rider(id:Int, name:String)

case class Competition(riders:List[Rider]) {
  require(riders.map(_.id).toSet.size == riders.size, "Some riders have duplicate IDs")
  val riderLookup:Map[Int,Rider] = riders.map{case r@Rider(id, name) => (id -> r)}.toMap
  def rider(id:Int) = riderLookup(id)

  def toRound = Round(riders)
}

case class StartingGroup(riders:List[Rider]) {
  def advancingRiders(judges:List[String], judgingSheets:List[JudgingSheet]):List[Rider] = {
    judgingSheets.foreach{ sheet =>
      require(judges contains sheet.judge)
      require(riders.size == sheet.placements.size)
      require(riders.toSet == sheet.placements.map(_.rider).toSet)
    }

    val scores = new mutable.Map.WithDefault( new mutable.HashMap[Rider,Int], (k:Rider) => 0 )
    for( sheet <- judgingSheets )
      for( placement <- sheet.placements )
        scores(placement.rider) += placement.rank

    var advancing = scores.toList.sortBy(_._2).take(advancingRiderCount)
    val ties = scores.toList.filter(advancing.map(_._2) contains _._2)
    (advancing ::: ties).map(_._1)
  }
}

case class Placement(rider:Rider, rank:Int)

case class JudgingSheet(judge:String, placements:List[Placement]) {
  require( placements.map(_.rank).toSet == Range.inclusive(1,placements.size).toSet )
}

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

  def next = {
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
