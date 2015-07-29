package xstyle.libxstyle

import scala.concurrent.duration._


object Constants {
  val timeBeforeCompetition = Duration(0, MINUTES)
  val timeBeforeRound = Duration(10, MINUTES)
  val timeBeforeGroup = Duration(5, MINUTES)
  val timeBeforeRun = Duration(0, MINUTES)

  def lengthOfRun: (AbstractRound) => FiniteDuration = {
    case round if round.isFinalRound => Duration(2, MINUTES)
    case round if round.isIntermediateRound => Duration(1, MINUTES) + Duration(30, SECONDS)
    case round if round.isFirstRound => Duration(1, MINUTES)
  }

  val timeAfterRun = Duration(1, MINUTES)
  val timeAfterGroup = Duration(5, MINUTES)
  val timeAfterRound = Duration(15, MINUTES)
  val timeAfterCompetition = Duration(20, MINUTES)

  val timePerCompetition = timeBeforeCompetition + timeAfterCompetition
  val timePerRound = timeBeforeRound + timeAfterRound
  val timePerGroup = timeBeforeGroup + timeAfterGroup
  val timePerRun = { round: AbstractRound => timeBeforeRun + lengthOfRun(round) + timeAfterRun }

  val maxGroupSize = 10
  val advancingRiderCount = 3

  val judgingGroupCount = 2
}
