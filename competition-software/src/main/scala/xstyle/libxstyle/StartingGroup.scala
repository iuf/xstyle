package xstyle.libxstyle

import collection.mutable

import Constants._
object StartingGroup {
  def apply(riders:List[Rider]) = new StartingGroup(0,riders)
}

case class StartingGroup(id:Int, riders:List[Rider]) {
  def advancingRiders(judges:List[String], judgingSheets:List[JudgingSheet]):List[Rider] = {
    judgingSheets.foreach{ sheet =>
      require(judges contains sheet.judge)
      require(riders.size == sheet.placements.size)
      require(riders.toSet == sheet.placements.map(_.rider).toSet)
    }

    val scores = new mutable.Map.WithDefault( new mutable.HashMap[Rider,Int], (k:Rider) => 0 )
    for( sheet <- judgingSheets )
      for( placement <- sheet.placements )
        scores(placement.rider) += placement.rank

    var advancing = scores.toList.sortBy(_._2).take(advancingRiderCount)
    val ties = scores.toList.filter(advancing.map(_._2) contains _._2)
    (advancing ::: ties).map(_._1)
  }

  override def toString = s"""StartingGroup(id=$id: ${riders.map(_.name).mkString(",")})"""
}

