package xstyle.gui

import xstyle.libxstyle.Helpers._
import xstyle.libxstyle._
import xstyle.simulation._

object Main extends {
  def main(args: Array[String]) {
    for (file <- if (args.nonEmpty) args else Array("riders")) {
      println(s"\n----- file: $file -----\n")
      val riders = readRiders(file)
      val round = Round(riders)
      for (group <- round.randomStartingGroups) {
        println(s"Starting group ${group.id + 1} (judged by groups ${round.judgingAssignment(group.id).map(_ + 1).mkString(",")}):")
        println(group.riders.map(_.name).mkString("  ", "\n  ", ""))
        println()
      }

      println(s"Judging:\n${round.revJudgingAssignment.zipWithIndex.map { case (judges, group) => s"  Group ${group + 1} judging groups ${judges.map(_ + 1).mkString(",")}" }.mkString("\n")}\n")
      println(s"${round.judgingAssignment.zipWithIndex.map { case (judges, group) => s"  Group ${group + 1} judged by groups ${judges.map(_ + 1).mkString(",")}" }.mkString("\n")}\n")

      val s = new Simulation(riders.size, parallel = false)
      println(s.toString)
    }
  }
}
