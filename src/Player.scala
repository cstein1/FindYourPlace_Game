import java.rmi.server.UnicastRemoteObject

@remote trait RemotePlayer {
  def upPressed:Unit
  def downPressed:Unit
  def leftPressed:Unit
  def rightPressed:Unit
  def upReleased:Unit
  def downReleased:Unit
  def leftReleased:Unit
  def rightReleased:Unit
  def getx:Double
  def gety:Double
}

class Player(ex:Double, ey:Double, val client:RemoteClient) extends UnicastRemoteObject with Entity with RemotePlayer {
  protected var x = ex
  protected var y = ey
  def getX = x
  def getY = y
  private var up = false
  private var down = false
  private var left = false
  private var right = false
  
  def upPressed:Unit = up = true
  def downPressed:Unit = down = true
  def leftPressed:Unit = left = true
  def rightPressed:Unit = right = true
  def upReleased:Unit = up = false
  def downReleased:Unit = down = false
  def leftReleased:Unit = left = false
  def rightReleased:Unit = right = false

  override def update():Unit = {
    var nx = x
    var ny = y
    if(up) ny -= 1
    if(down) ny += 1
    if(left) nx -= 1
    if(right) nx += 1
    if(nx>=0 && nx<level.maze.size && ny>=0 && ny<level.maze(0).size && level.maze(ny.toInt)(nx.toInt).canPass) {
      x = nx
      y = ny
    }
  }

  def makePassable:PassableEntity = new PassableEntity(x, y, Entity.playerValue)
}