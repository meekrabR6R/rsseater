case class Point(x:Int, y:Int){

  def check(that:Point):Boolean = {
    that.x == x ||
    that.y == y ||
    sameDiagonal(that)
  }

  def sameDiagonal(that:Point):Boolean = {
    (that.x - x).abs == (that.y - y).abs
  }
}

class Solver(val size:Int) {

  def solve:List[List[Point]] = {
    possibilityAtColumn(size - 1 )
  }
  
  /*
    The error occurred on line 24 b/c using map on a list applies a function to each member of the list. 
    In this case, the function passed to map to be used on each item of the list returns a list of lists
    of points. This resulted in a list of lists of lists of points which was at odds with the return type
    required. Replacing map with flatMap solved this issue since flatMap actually applies a function to each
    member of a list, but then concatenates or 'flattens' the results.
  */
  def possibilityAtColumn(c:Int):List[List[Point]] = c match {
    case 0 => possiblePoints(0).map(List(_))
    case _ => possibilityAtColumn(c-1).flatMap(possibleAdditions(_, c)).dropWhile(_.isEmpty) 
  }

  def possibleAdditions(existingPoints:List[Point], column:Int) : List[List[Point]] = {
    possiblePoints(column).filter( point => existingPoints.forall(!point.check(_))).map(_ :: existingPoints)
  }

  def possiblePoints(column:Int) = {
    (0 until size).toList.map(row => Point(column, row))
  }
}