import swing._
import event._
import javax.swing.Timer
import java.io.FileOutputStream
import java.io.BufferedOutputStream
import java.io.ObjectOutputStream
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.BufferedInputStream
import java.rmi.server.UnicastRemoteObject
import java.rmi.registry.LocateRegistry
import java.rmi.Naming

@remote trait RemoteServer {
  def joinGame(client:RemoteClient):RemotePlayer
}

object ServerMain extends UnicastRemoteObject with RemoteServer {
  println("Making a server")
  LocateRegistry.createRegistry(1099)
  Naming.rebind("tacos", this)
  val maze = Array(Array(0, 1, 0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 1, 0, 0, 0, 1, 0, 0, 0, 0),
    Array(0, 0, 0, 0, 0, 1, 0, 0, 0, 0),
    Array(0, 1, 0, 1, 1, 1, 1, 1, 0, 0),
    Array(0, 1, 0, 1, 0, 0, 0, 0, 0, 0),
    Array(0, 1, 1, 1, 0, 0, 1, 1, 1, 1),
    Array(0, 1, 0, 0, 0, 0, 0, 0, 0, 0),
    Array(0, 1, 0, 0, 0, 0, 1, 1, 0, 0),
    Array(0, 1, 0, 0, 1, 0, 0, 1, 0, 0),
    Array(0, 0, 0, 0, 1, 0, 0, 1, 0, 0))
  private var level = new Level(maze, List[Entity](new NPC(4, 4)))//, new NPC(9, 9), new NPC(8, 8)))
  val lev = level
  private var players = List[Player]()
  //println(level.depthFirstShortestPath(0, 0, 9, 9))
  //println(level.breadthFirstShortestPath(0, 0, 9, 9))
  val timer = new Timer(100, Swing.ActionListener { ae =>
    level.updateAll
    players.foreach(_.client.updateLevel(level.buildPassable))
  })
  
  def joinGame(client:RemoteClient):RemotePlayer = {
    val p = new Player(0, 0, client)
    players ::= p
    level.addEntity(p)
    p:RemotePlayer
  }
  
  def main(args: Array[String]): Unit = {
		  timer.start()
  }
}