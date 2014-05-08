package xstyle.libxstyle

object Helpers {
  def rotate[T](xs:Iterable[T], delta:Int) = xs.drop(delta % xs.size) ++ xs.take(delta % xs.size)
}
