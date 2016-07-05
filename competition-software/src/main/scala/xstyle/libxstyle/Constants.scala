package xstyle.libxstyle

import scala.concurrent.duration._

object Constants {
  val timeBeforeCompetition = 0 minutes
  val timeBeforeRound = 10 minutes
  val timeBeforeGroup = 5 minutes
  val timeBeforeRun = 0 minutes

  val timeAfterRun = 1 minutes
  val timeAfterGroup = 5 minutes
  val timeAfterRound = 15 minutes
  val timeAfterCompetition = 20 minutes

  val timePerCompetition = timeBeforeCompetition + timeAfterCompetition
  val timePerRound = timeBeforeRound + timeAfterRound
  val timePerGroup = timeBeforeGroup + timeAfterGroup
  def timePerRun(currentRound: AbstractRound, totalRoundCount: Int) = {
    timeBeforeRun + lengthOfRun(currentRound, totalRoundCount) + timeAfterRun
  }

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

  val maxGroupSize = 10
  val advancingRiderCount = 3

  val judgingGroupCount = 2
}
