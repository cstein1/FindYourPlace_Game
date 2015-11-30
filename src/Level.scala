class Level(intMaze:Array[Array[Int]],private var chars:List[Entity]) extends Serializable {
  val maze = intMaze.map(row => row.map(_ match {
    case 0 => Floor
    case 1 => Wall
  }))
  chars.foreach { e => e.level = this }
  
  def characters = chars
  
  def addEntity(e:Entity):Unit = {
    chars ::= e
    e.level = this
  }
  
  def removeEntity(victim:Entity):Unit = {
    chars = chars.filter(_ != victim)
  }
  
  def updateAll:Unit = chars.foreach { _.update }
  
  def buildPassable:PassableLevel = {
    new PassableLevel(maze,chars.map(_.makePassable).toArray)
  }
  
  def depthFirstShortestPath(x:Int, y:Int, ex:Int, ey:Int):Int = {
    if(x==ex && y==ey) 0
    else if(x<0 || y<0 || x>=maze.length || y>=maze(x).length || !maze(x)(y).canPass) 1000000000
    else {
      val tmp = maze(x)(y)
      maze(x)(y) = Wall
      val ret = (depthFirstShortestPath(x-1,y,ex,ey) min
      depthFirstShortestPath(x+1,y,ex,ey) min
      depthFirstShortestPath(x,y-1,ex,ey) min
      depthFirstShortestPath(x,y+1,ex,ey))+1
      maze(x)(y) = tmp
      ret
    }
  }
  
  def breadthFirstShortestPath(x:Int, y:Int, ex:Int, ey:Int):Int = {
    val queue = scala.collection.mutable.Queue[(Int,Int,Int)]()
    val visited = scala.collection.mutable.Set[(Int,Int)]()
    
    def happySpot(cx:Int, cy:Int):Boolean = {
      cx>=0 && cy>=0 && cx<maze.length && cy<maze(x).length && maze(cx)(cy).canPass && 
      !visited((cx,cy))
    }
    
    queue.enqueue((x,y,0))
    visited.add((x,y))
    while(queue.nonEmpty) {
      val (cx, cy, steps) = queue.dequeue()
      if(cx==ex && cy==ey) return steps
      if(happySpot(cx+1,cy)) { queue.enqueue((cx+1,cy,steps+1)); visited += ((cx+1,cy)) }
      if(happySpot(cx-1,cy)) { queue.enqueue((cx-1,cy,steps+1)); visited += ((cx-1,cy)) }
      if(happySpot(cx,cy+1)) { queue.enqueue((cx,cy+1,steps+1)); visited += ((cx,cy+1)) }
      if(happySpot(cx,cy-1)) { queue.enqueue((cx,cy-1,steps+1)); visited += ((cx,cy-1)) }
    }
    -1
  }
}