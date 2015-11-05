class Player(private val c:RemoteClient, var px: Int, var py: Int) extends Entity(px, py) {
  def client = c
  private var playerx = px.toDouble
  private var playery = py.toDouble
  override def gety = playery
  override def getx = playerx
  //private var incrementx = ((playerx / level.maze.length) * 800)
  //private var incrementy = ((playery / level.maze.length) * 600)

  override def update(): Unit = {
    
  }

  def upPress() = {
    println("Up " + playerx + " " + playery)
    if (level.element(playerx.toInt, playery.toInt - 1).canPass) {
      playery -= 1
    }
  }

  def dPress() = {
    println("Down")
    if (playery > (playery / level.maze.length) && level.element(playerx.toInt, playery.toInt + 1).canPass) {
      playery += 1
    }
  }

  def lPress() = {
    println("Left")
    if (playerx > (playerx / level.maze.length) && level.element(playerx.toInt - 1, playery.toInt).canPass) {
      playerx -= 1
    }
  }

  def rPress() = {
    println("Right")
    if (playerx < level.maze.size && level.element(playerx.toInt + 1, playery.toInt).canPass) {
      playerx += 1
    }
  }
}