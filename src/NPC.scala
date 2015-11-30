class NPC(ex:Double, ey:Double) extends Entity {
  protected var x = ex
  protected var y = ey
  override def update():Unit = {
    val nx = x+util.Random.nextInt(3)-1
    val ny = y+util.Random.nextInt(3)-1
    if(nx>=0 && nx<level.maze.size && ny>=0 && ny<level.maze(0).size && level.maze(ny.toInt)(nx.toInt).canPass) {
      x = nx
      y = ny
    }
  }

  def makePassable:PassableEntity = new PassableEntity(x, y, Entity.enemyValue)
}