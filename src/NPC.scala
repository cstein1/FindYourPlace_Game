class NPC(ex: Double, ey: Double) extends Entity {
  protected var x = ex
  protected var y = ey
  override def update(): Unit = {
    level.npcs
    if (!level.pList.isEmpty) {
      var g = List[Int]()
      for (p <- level.pList) {
        val a = level.breadthFirstShortestPath(x.toInt, (y - 1).toInt, p.getX.toInt, p.getY.toInt)
        val b = level.breadthFirstShortestPath(x.toInt, (y + 1).toInt, p.getX.toInt, p.getY.toInt)
        val c = level.breadthFirstShortestPath((x + 1).toInt, y.toInt, p.getX.toInt, p.getY.toInt)
        val d = level.breadthFirstShortestPath((x - 1).toInt, y.toInt, p.getX.toInt, p.getY.toInt)
        g ::= a 
        g ::= b 
        g ::= c 
        g ::= d
      }
      println(g.mkString(" "))
      (g.indexOf(g.min) % 4) match {
        case 0 => mvLf
        case 1 => mvRt
        case 2 => mvDn
        case 3 => mvUp
      }
    }
  }

  def makePassable: PassableEntity = new PassableEntity(x, y, Entity.enemyValue)

  def randWalk {
    val nx = x + util.Random.nextInt(3) - 1
    val ny = y + util.Random.nextInt(3) - 1
    if (nx >= 0 && nx < level.maze.size && ny >= 0 && ny < level.maze(0).size && level.maze(ny.toInt)(nx.toInt).canPass) {
      x = nx
      y = ny
    }
  }

  def mvUp = if (x >= 0 && x < level.maze.size && y - 1 >= 0 && y < level.maze(0).size && level.maze((y - 1).toInt)(x.toInt).canPass) y -= 1
  def mvDn = if (x >= 0 && x < level.maze.size && y >= 0 && y + 1 < level.maze(0).size && level.maze((y + 1).toInt)(x.toInt).canPass) y += 1
  def mvLf = if (x - 1 >= 0 && x < level.maze.size && y >= 0 && y < level.maze(0).size && level.maze(y.toInt)((x - 1).toInt).canPass) x -= 1
  def mvRt = if (x >= 0 && x + 1 < level.maze.size && y >= 0 && y < level.maze(0).size && level.maze(y.toInt)((x + 1).toInt).canPass) x += 1
}