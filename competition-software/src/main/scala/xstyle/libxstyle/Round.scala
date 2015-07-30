package xstyle.libxstyle

import xstyle.libxstyle.Constants._
import xstyle.libxstyle.Helpers._

import scala.concurrent.duration.{Duration, FiniteDuration}

case class Round(riders: List[Rider], override val roundNumber: Int = 1) extends AbstractRound(riders.size, roundNumber) {
  def randomStartingGroups: List[StartingGroup] = {
    def randomRiders = util.Random.shuffle(riders)
    val abstractRound = AbstractRound(riders.size, roundNumber)
    //TODO: StartingGroups sorted by age
    var ridersLeft = randomRiders
    var groups: List[StartingGroup] = Nil
    for ((groupSize, i) <- abstractRound.groupSizes.zipWithIndex) {
      groups :+= StartingGroup(i, ridersLeft take groupSize)
      ridersLeft = ridersLeft drop groupSize
    }
    assert(ridersLeft == Nil)
    assert(abstractRound.groupSizes.sorted == groups.map(_.riders.size).sorted)
    groups
  }

  override def toString = s"Round($roundNumber: ${riders.size} riders, $groupCount groups)"
}

object AbstractRound {
  def apply(n: Int, roundNumber: Int = 1) = new AbstractRound(n, roundNumber)
}

class AbstractRound(val competitorCount: Int, val roundNumber: Int = 1) {
  require(advancingRiderCount <= maxGroupSize / 2)
  require(competitorCount >= advancingRiderCount, s"Not enough competitors. Need at least $advancingRiderCount.")
  require(roundNumber > 0)

  def groupCount = divCeil(competitorCount, maxGroupSize)

  def runCount = competitorCount

  def isFirstRound = roundNumber == 1

  def isFinalRound = groupCount == 1

  def isIntermediateRound = !(isFirstRound || isFinalRound)

  def freePlaces = maxGroupSize - (competitorCount % maxGroupSize)

  def smallGroupCount = freePlaces % groupCount

  def bigGroupCount = groupCount - smallGroupCount

  def smallGroupSize = divFloor(competitorCount, groupCount)

  def bigGroupSize = divCeil(competitorCount, groupCount)

  def groupSizes = List.fill(bigGroupCount)(bigGroupSize) ::: List.fill(smallGroupCount)(smallGroupSize)

  assert(groupSizes.sum == competitorCount, s"$this: $groupSizes")
  assert((competitorCount > maxGroupSize) == (groupCount > 1))
  assert(if (groupCount > 1) smallGroupSize >= maxGroupSize / 2 else true, this)

  def next = {
    require(groupSizes.forall(_ >= advancingRiderCount))
    AbstractRound(groupCount * advancingRiderCount, roundNumber + 1)
  }

  def judgingAssignment = groupCount match {
    case 2 => List(List(1), List(0))
    case _ => maxDistancePairJudgingSlidingRotate(0 until groupCount)
  }

  def revJudgingAssignment = (0 until groupCount).map(gi => judgingAssignment.zipWithIndex.flatMap { case (g, i) if g contains gi => Some(i); case _ => None })

  //def canBeRunInParallel = competitorCount >= (judgingGroupShift + 1) * 2

  def totalRoundCount: Int = 1 + (if (groupCount > 1) next.totalRoundCount else 0)

  def totalRunCount: Int = runCount + (if (groupCount > 1) next.totalRunCount else 0)

  def totalGroupCount: Int = groupCount + (if (groupCount > 1) next.totalGroupCount else 0)

  def totalRunTime: FiniteDuration = runCount * timePerRun(this) + (if (groupCount > 1) next.totalRunTime else Duration.Zero)

  def totalTimeNeeded = timePerCompetition +
    timePerRound * totalRoundCount +
    timePerGroup * totalGroupCount +
    totalRunTime

  def timeNeeded = timePerRound +
    timePerGroup * groupCount +
    timePerRun(this) * runCount
}
