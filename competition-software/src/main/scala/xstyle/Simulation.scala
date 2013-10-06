package xstyle.simulation

import xstyle.libxstyle._
import Constants._
import scala.concurrent.duration._


object Main extends App {
  val competitorCount = 70

  def occurrences[T](list:Iterable[T]) = list.groupBy(identity).mapValues(_.size)
  def prettyDuration(d:Duration) = (if(d.toHours > 0) s"${d.toHours}h " else "") + s"${(d - Duration(d.toHours, HOURS)).toMinutes}min"

  var round = AbstractRound(competitorCount)


  println("X-Style competition\n")

  println(s"competitors:           ${round.competitorCount}")
  println(s"total number of runs:  ${round.totalRunCount}")
  println(s"total time needed:     ${prettyDuration(round.totalTimeNeeded)}")


  println()
  var i = 1
  while( round.competitorCount > advancingRiderCount ) {
  println(s"round $i")
  println(s"  competitors: ${round.competitorCount}")
  println(s"  groups:      ${round.groupCount}")
  println(s"  groupSizes:  ${occurrences(round.groupSizes).map{ case (number, count) => s"${count}x${number} riders"}.mkString(", ")}")
  println(s"  time:        ${prettyDuration(round.timeNeeded)}")
  println()

  round = round.next
  i += 1
  }
}
