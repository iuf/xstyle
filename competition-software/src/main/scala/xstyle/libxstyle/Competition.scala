package xstyle.libxstyle

case class Competition(riders: List[Rider]) {
  require(riders.map(_.id).toSet.size == riders.size, "Some riders have duplicate IDs")
  val riderLookup: Map[Int, Rider] = riders.map { case r@Rider(id, _) => id -> r }.toMap

  def rider(id: Int) = riderLookup(id)

  def toRound = Round(riders, 1)
}


