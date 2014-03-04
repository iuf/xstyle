package xstyle.simulation

import xstyle.libxstyle._
import Constants._
import scala.concurrent.duration._

import collection.mutable

class Simulation(competitorCount:Int, parallel:Boolean = true) {

  def occurrences[T](list:Iterable[T]) = list.groupBy(identity).mapValues(_.size)
  def prettyDuration(d:Duration) = (if(d.toHours > 0) s"${d.toHours}h " else "") + s"${(d - Duration(d.toHours, HOURS)).toMinutes}min"

  val initialRound = AbstractRound(competitorCount)

  override def toString = {
    val sb = new mutable.StringBuilder
    sb ++= "X-Style competition\n\n"

    sb ++= s"competitors:           ${initialRound.competitorCount}\n"
    sb ++= s"total number of runs:  ${initialRound.totalRunCount}\n"
    sb ++= s"total time needed:     ${prettyDuration(initialRound.totalTimeNeeded)}\n"


    sb ++= "\n"
    var round = initialRound
    var roundId = 1
    var time = Duration(0, MINUTES)
    def currentTime = s"[${prettyDuration(time)}]"
    
    time += timePerCompetition
    while( round.competitorCount > advancingRiderCount ) {
      time += timeBeforeRound

      sb ++= s"${currentTime} round $roundId\n"
      sb ++= s"  competitors: ${round.competitorCount}\n"
      sb ++= s"  groups:      ${round.groupCount}\n"
      sb ++= s"  group sizes: ${occurrences(round.groupSizes).map{ case (number, count) => s"${count}x${number} riders"}.mkString(", ")}\n"
      sb ++= s"  total time:  ${prettyDuration(round.timeNeeded)}\n"

      for( (groupSize, group) <- round.groupSizes zipWithIndex ) {
        time += timeBeforeGroup
        sb ++= s"    ${currentTime} Starting Group ${group + 1} ($groupSize riders)\n"
        //sb ++= s"      Judges: Group ${(group+1+judgingGroupShift)%round.groupCount}\n"
        sb ++= s"      Judges: Group ${(group+1)%round.groupCount}\n"
        for( run <- 1 to groupSize ) {
          time += timeBeforeRun
          sb ++= s"      ${currentTime} Run $run\n"
          time += lengthOfRun
          time += timeAfterRun
        }
        time += timeAfterGroup
      }

      time += timeAfterRound

      sb ++= "\n"
      round = round.next
      roundId += 1
    }
    
    assert(time == initialRound.totalTimeNeeded, s"$time != ${initialRound.totalTimeNeeded}")

    sb.result
  }  
}
