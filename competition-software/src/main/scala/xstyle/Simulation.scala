package xstyle.simulation

import xstyle.libxstyle.Constants._
import xstyle.libxstyle._

import scala.collection.mutable
import scala.concurrent.duration._

class Simulation(competitorCount: Int, parallel: Boolean = true) {

  def occurrences[T](list: Iterable[T]) = list.groupBy(identity).mapValues(_.size)

  def prettyDuration(d: Duration) = (if (d.toHours > 0) s"${d.toHours}h " else "") + s"${(d - Duration(d.toHours, HOURS)).toMinutes}min"

  val initialRound = AbstractRound(competitorCount)
  val totalRoundCount = initialRound.totalRoundCount

  override def toString = {
    val sb = new mutable.StringBuilder
    sb ++= s"competitors:           ${initialRound.competitorCount}\n"
    sb ++= s"rounds:                ${initialRound.totalRoundCount}\n"
    sb ++= s"total number of runs:  ${initialRound.totalRunCount}\n"
    sb ++= s"total time needed:     ${prettyDuration(initialRound.totalTimeNeeded(totalRoundCount))}\n"

    sb ++= "\n"
    var round = initialRound
    var time = 0 minutes
    def currentTime = s"[${prettyDuration(time)}]"

    time += timeBeforeCompetition
    while (round.competitorCount > advancingRiderCount) {
      time += timeBeforeRound

      sb ++= s"$currentTime round ${round.roundNumber}" + (if (round.isFinalRound) " (Finals)" else "") + "\n"
      sb ++= s"  run length:  ${lengthOfRun(round, initialRound.totalRoundCount)}\n"
      sb ++= s"  competitors: ${round.competitorCount}\n"
      sb ++= s"  groups:      ${round.groupCount}\n"
      sb ++= s"  group sizes: ${occurrences(round.groupSizes).map { case (number, count) => s"${count}x$number riders" }.mkString(", ")}\n"
      sb ++= s"  total time:  ${prettyDuration(round.timeNeeded(totalRoundCount))}\n"
      sb ++= s"  judging:\n${round.judgingAssignment.zipWithIndex.map { case (judges, group) => s"    Group ${group + 1} judged by groups ${judges.map(_ + 1).mkString(",")}" }.mkString("\n")}\n"

      for ((groupSize, group) <- round.groupSizes zipWithIndex) {
        time += timeBeforeGroup
        sb ++= s"    $currentTime Starting Group ${group + 1} ($groupSize riders)\n"
        //sb ++= s"      Judges: Group ${(group+1+judgingGroupShift)%round.groupCount}\n"
        sb ++= s"      Judges: Groups ${round.judgingAssignment(group).map(_ + 1).mkString(",")}\n"
        for (run <- 1 to groupSize) {
          time += timeBeforeRun
          sb ++= s"      $currentTime Run $run\n"
          time += lengthOfRun(round, totalRoundCount)
          time += timeAfterRun
        }
        time += timeAfterGroup
      }

      time += timeAfterRound

      sb ++= "\n"
      round = round.next
    }
    time += timeAfterCompetition

    assert(time == initialRound.totalTimeNeeded(totalRoundCount), s"$time != ${initialRound.totalTimeNeeded(totalRoundCount)}")

    sb.result()
  }
}
