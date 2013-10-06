package xstyle.simulation

import xstyle.libxstyle._
import Constants._
import scala.concurrent.duration._


object Main extends App {
  val competitorCount = 70

  def occurrences[T](list:Iterable[T]) = list.groupBy(identity).mapValues(_.size)
  def prettyDuration(d:Duration) = (if(d.toHours > 0) s"${d.toHours}h " else "") + s"${(d - Duration(d.toHours, HOURS)).toMinutes}min"

  val initialRound = AbstractRound(competitorCount)


  println("X-Style competition\n")

  println(s"competitors:           ${initialRound.competitorCount}")
  println(s"total number of runs:  ${initialRound.totalRunCount}")
  println(s"total time needed:     ${prettyDuration(initialRound.totalTimeNeeded)}")


  println()
  var round = initialRound
  var roundId = 1
  var time = Duration(0, MINUTES)
  def currentTime = s"[${prettyDuration(time)}]"
  
  time += timePerCompetition
  while( round.competitorCount > advancingRiderCount ) {
    time += timeBeforeRound

    println(s"${currentTime} round $roundId")
    println(s"  competitors: ${round.competitorCount}")
    println(s"  groups:      ${round.groupCount}")
    println(s"  group sizes: ${occurrences(round.groupSizes).map{ case (number, count) => s"${count}x${number} riders"}.mkString(", ")}")
    println(s"  total time:  ${prettyDuration(round.timeNeeded)}")

    for( (groupSize, group) <- round.groupSizes zipWithIndex ) {
      time += timeBeforeGroup
      println(s"    ${currentTime} Starting Group $group ($groupSize riders)")
      for( run <- 1 to groupSize ) {
        time += timeBeforeRun
        println(s"      ${currentTime} Run $run")
        time += lengthOfRun
        time += timeAfterRun
      }
      time += timeAfterGroup
    }

    time += timeAfterRound

    println()
    round = round.next
    roundId += 1
  }
  
  assert(time == initialRound.totalTimeNeeded, s"$time != ${initialRound.totalTimeNeeded}")
}
