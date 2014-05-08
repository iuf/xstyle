package xstyle.libxstyle

object Rider {
  var currentId = 0
  def nextAutoId() = {currentId += 1; currentId}
  def apply(name:String):Rider = Rider(nextAutoId(), name)
}
case class Rider(id:Int, name:String)

