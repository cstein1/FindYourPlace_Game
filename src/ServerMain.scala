import java.rmi.Naming
import java.rmi.registry.LocateRegistry
import java.rmi.server.UnicastRemoteObject

import scala.swing._

import javax.swing.Timer

@remote trait RemoteServer {
  def joinGame(client: RemoteClient): RemotePlayer
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
  private val npcs = List[Entity](new NPC(9, 9))
  private var level = new Level(maze, npcs) //, new NPC(9, 9), new NPC(8, 8)))
  val lev = level
  private var players = List[Player]()

  val timer = new Timer(100, Swing.ActionListener { ae =>
    level.updateAll
    players.foreach(_.client.updateLevel(level.buildPassable))
  })

  def joinGame(client: RemoteClient): RemotePlayer = {
    val p = new Player(0, 0, client)
    players ::= p
    level.addEntity(p)
    p: RemotePlayer
  }

  def main(args: Array[String]): Unit = {
    timer.start()
  }
}