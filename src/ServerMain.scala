import java.rmi.server.UnicastRemoteObject
import java.rmi._
import scala.actors.Actor
import collection.mutable
import scala.actors.Future
import scala.actors.Future
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.swing.Swing
import javax.swing.Timer

@remote trait RemoteServer {
  def joinGame(client: RemoteClient)
  def updateClient(client: RemoteClient)
}

object ServerMain extends UnicastRemoteObject with RemoteServer {
  def players = Client.players
  val enemy1 = new NPC(1, 1)
  val enemy2 = new NPC(3, 3)
  val enemies = List(enemy1, enemy2)
  // val players = clients.toList.foreach
  var chars = enemies ++ players.toList

  val maze1 = Array(Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
    Array(1, 0, 1, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 1, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 0, 1, 0, 0, 0, 0, 1, 0, 1),
    Array(1, 0, 1, 0, 0, 0, 0, 1, 0, 1),
    Array(1, 0, 1, 0, 0, 0, 0, 1, 0, 1),
    Array(1, 0, 1, 0, 0, 0, 0, 1, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 1, 0, 1),
    Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1),
    Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1))

  var level = new Level(maze1, chars)
  chars.foreach(_.level = level)

  private var numPlayers = 0
  def main(args: Array[String]) {
	  connect
    println("Running")
    Future {
      while (true) {
        Thread.sleep(100)
        Future {
          joinGame(_)
          //timer.start()
        }
      }
    }
    //.(GameMap(testLevel))
  }

  def connect {
    registry.LocateRegistry.createRegistry(1099)
    Naming.rebind("GameServer", this)
  }

  def joinGame(client: RemoteClient): Unit = {
    chars synchronized {
      //players += List[Player](new Player(client, 8, 8))
      level.addEntity(new Player(client, 8, 8))
      chars = level.characters
      numPlayers += 1
      println("SOMEONE JOINED! Amount of players present: " + numPlayers)
      //Actor.actor {
      players.foreach(_.client.addClientToServer)
      //}
    }
  }

  val timer = new Timer(100, Swing.ActionListener { ae =>
    level.updateAll
    Client.updateLevel
    enemies.foreach(_.update)
    //drawPanel.repaint
  }).start()

  override def updateClient(cli: RemoteClient) {
    
  }
}