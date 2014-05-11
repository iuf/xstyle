package xstyle.libxstyle

object Helpers {
  def rotate[T](xs:Iterable[T], delta:Int) = xs.drop(delta % xs.size) ++ xs.take(delta % xs.size)
  def repeat[T](xs:Iterable[T], count:Int):Iterable[T] = if(count == 1) xs else xs ++ repeat(xs, count-1)
}
