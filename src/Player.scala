import java.rmi.server.UnicastRemoteObject
@remote trait RemotePlayer {
  def upPress
  def dPress
  def lPress
  def rPress
  def getPx
  def getPy
}

class Player(private val c:RemoteClient, var px: Int, var py: Int) extends UnicastRemoteObject with Entity with RemotePlayer {
  def client = c
  private var playerx = px.toDouble
  private var playery = py.toDouble
  override def getPy = playery
  override def getPx = playerx
  //override def update(): Unit = {}

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