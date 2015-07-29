package xstyle.libxstyle

import scala.concurrent.duration._

object Constants {
  val timeBeforeCompetition = Duration(0, MINUTES)
  val timeBeforeRound = Duration(10, MINUTES)
  val timeBeforeGroup = Duration(5, MINUTES)
  val timeBeforeRun = Duration(0, MINUTES)
  val lengthOfRun = Duration(2, MINUTES)
  val timeAfterRun = Duration(1, MINUTES)
  val timeAfterGroup = Duration(5, MINUTES)
  val timeAfterRound = Duration(15, MINUTES)
  val timeAfterCompetition = Duration(20, MINUTES)

  val timePerCompetition = timeBeforeCompetition + timeAfterCompetition
  val timePerRound = timeBeforeRound + timeAfterRound
  val timePerGroup = timeBeforeGroup + timeAfterGroup
  val timePerRun = timeBeforeRun + lengthOfRun + timeAfterRun

  val maxGroupSize = 10
  val advancingRiderCount = 3

  val judgingGroupCount = 2
}
