import scala.concurrent.duration._

val competitorCount      = 70

val lengthOfRun          = Duration( 2, MINUTES)
val timePerRun           = Duration( 1, MINUTES)
val timePerGroup         = Duration(15, MINUTES)
val timePerRound         = Duration(20, MINUTES)
val timePerCompetition   = Duration(20, MINUTES)

val maxGroupSize         = 10
val advancingCompetitors = 3

case class Round(competitorCount:Int) {
  require( advancingCompetitors <= maxGroupSize/2 )
  require( competitorCount >= advancingCompetitors, s"Not enough competitors. Need at least $advancingCompetitors." )
  
  private def divFloor(a:Int, b:Int) = a / b
  private def divCeil(a:Int, b:Int) = if(a % b == 0) a / b else (a + ( b - (a % b) )) / b
  
  def groupCount = divCeil(competitorCount, maxGroupSize)
  def runCount = competitorCount
  
  def freePlaces = maxGroupSize - (competitorCount % maxGroupSize)
  def smallGroupCount = freePlaces % groupCount
  def bigGroupCount = groupCount - smallGroupCount

  def smallGroupSize = divFloor(competitorCount, groupCount)
  def bigGroupSize = divCeil(competitorCount, groupCount)
  
  def groupSizes = List.fill(bigGroupCount)(bigGroupSize) ::: List.fill(smallGroupCount)(smallGroupSize)
  
  assert(groupSizes.sum == competitorCount, s"$this: $groupSizes")
  assert( competitorCount > maxGroupSize == groupCount > 1 )
  assert( if( groupCount > 1 ) smallGroupSize >= maxGroupSize / 2 else true,  this)
  
  def next = {
    require(groupSizes.forall( _ >= advancingCompetitors))
    Round(groupCount * advancingCompetitors)
  }
  
  def totalRoundCount:Int = 1 + (if( groupCount > 1 ) next.totalRoundCount else 0)
  def totalRunCount:Int = competitorCount + (if( groupCount > 1 ) next.totalRunCount else 0)
  def totalGroupCount:Int = groupCount + (if( groupCount > 1 ) next.totalGroupCount else 0)
  
  def totalTimeNeeded = timePerCompetition +
                   timePerRound * totalRoundCount +
                   timePerGroup * totalGroupCount +
                   (timePerRun + lengthOfRun) * totalRunCount
  def timeNeeded = timePerRound +
                   timePerGroup * groupCount +
                   (timePerRun + lengthOfRun) * runCount
}

def occurrences(list:Iterable[_]) = list.groupBy(identity).mapValues(_.size)
def prettyDuration(d:Duration) = (if(d.toHours > 0) s"${d.toHours}h " else "") + s"${(d - Duration(d.toHours, HOURS)).toMinutes}min"

var round = Round(competitorCount)


println("X-Style competition\n")

println(s"competitors:           ${round.competitorCount}")
println(s"total number of runs:  ${round.totalRunCount}")
println(s"total time needed:     ${prettyDuration(round.totalTimeNeeded)}")


println()
var i = 1
while( round.competitorCount > advancingCompetitors ) {
  println(s"round $i")  
  println(s"  competitors: ${round.competitorCount}")
  println(s"  groups:      ${round.groupCount}")
  println(s"  groupSizes:  ${occurrences(round.groupSizes).map{ case (number, count) => s"${count}x${number} riders"}.mkString(", ")}")
  println(s"  time:        ${prettyDuration(round.timeNeeded)}")
  println()
  
  round = round.next
  i += 1
}
