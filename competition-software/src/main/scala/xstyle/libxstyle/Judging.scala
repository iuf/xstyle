package xstyle.libxstyle

case class Placement(rider:Rider, rank:Int)

case class JudgingSheet(judge:String, placements:List[Placement]) {
  require( placements.map(_.rank).toSet == Range.inclusive(1,placements.size).toSet )
}


