import scala.concurrent.duration._

case class Round(competitorCount:Int, maxGroupSize:Int, winnerCount:Int) {
  require( winnerCount <= maxGroupSize/2 )
  require( competitorCount >= winnerCount )
  
  def divFloor(a:Int, b:Int) = a / b
  def divCeil(a:Int, b:Int) = if(a % b == 0) a / b else (a + ( b - (a % b) )) / b
  
  def groupCount = divCeil(competitorCount, maxGroupSize)
  
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
    require(groupSizes.forall( _ >= winnerCount))
    Round(groupCount * winnerCount, maxGroupSize, winnerCount)
  }
  
  def roundCount:Int = 1 + (if( groupCount > 1 ) next.roundCount else 0)
  def routineCount:Int = competitorCount + (if( groupCount > 1 ) next.routineCount else 0)
}

def occurrences(list:Iterable[_]) = list.groupBy(identity).mapValues(_.size)

// tests for the assertions
/*for( n <- 1 until 100;
     m <- 1 until 100;
     w <- 1 until 10;
     if(w < m/2);
     if(n >= w)
   ) {
  val a = Round(n,m,w)
}*/


var c = Round(competitorCount = 73, maxGroupSize = 10, winnerCount = 3)
val routineDuration = Duration(3, MINUTES)
val timeBetweenRounds = Duration(5, MINUTES)

println("X-Style competition\n")
println("configuration:")
println(s"  max group size:      ${c.maxGroupSize}")
println(s"  winners per group:   ${c.winnerCount}")
println(s"  time per routine:    ${routineDuration}")
println(s"  time between rounds: ${timeBetweenRounds}")
println(s"  competitors:         ${c.competitorCount}")
println()

println("total:")
println(s"  competitors:         ${c.competitorCount}")
println(s"  number of routines:  ${c.routineCount}")
println(s"  time needed:         ${routineDuration * c.routineCount + timeBetweenRounds * c.roundCount}")


println()
var i = 1
while( c.competitorCount > 3 ) {
  println(s"round $i")  
  println(s"  competitors: ${c.competitorCount}")
  println(s"  groups:      ${c.groupCount}")
  println(s"  groupSizes:  ${occurrences(c.groupSizes).map{ case (number, count) => s"${count}x${number} riders"}.mkString(", ")}")
  println(s"  time:        ${routineDuration * c.competitorCount + timeBetweenRounds}")
  println(s"  the best ${c.winnerCount} of each group continue.")
  println()
  c = c.next
  i += 1
}
