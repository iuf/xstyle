package xstyle.libxstyle

import scala.concurrent.duration._

object Constants {
  val timeBeforeCompetition = Duration(0, MINUTES)
  val timeBeforeRound = Duration(10, MINUTES)
  val timeBeforeGroup = Duration(5, MINUTES)
  val timeBeforeRun = Duration(0, MINUTES)

  def lengthOfRun(currentRound: AbstractRound, totalRoundCount: Int) = {
    if (totalRoundCount >= 3) currentRound match {
      case round if round.isFinalRound => 2 minutes
      case round if round.isIntermediateRound => (1 minutes) + (30 seconds)
      case round if round.isFirstRound => 1 minutes
    }
    else if (totalRoundCount == 2) currentRound match {
      case round if round.isFinalRound => 2 minutes
      case round if round.isFirstRound => (1 minutes) + (30 seconds)
    }
    else { // totalRoundCount == 1
      2 minutes
    }
  }

  val timeAfterRun = Duration(1, MINUTES)
  val timeAfterGroup = Duration(5, MINUTES)
  val timeAfterRound = Duration(15, MINUTES)
  val timeAfterCompetition = Duration(20, MINUTES)

  val timePerCompetition = timeBeforeCompetition + timeAfterCompetition
  val timePerRound = timeBeforeRound + timeAfterRound
  val timePerGroup = timeBeforeGroup + timeAfterGroup
  def timePerRun(currentRound: AbstractRound, totalRoundCount: Int) = {
    timeBeforeRun + lengthOfRun(currentRound, totalRoundCount) + timeAfterRun
  }

  val maxGroupSize = 10
  val advancingRiderCount = 3

  val judgingGroupCount = 2
}
