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
  def joinGame(client: RemoteClient):RemotePlayer
}

object ServerMain extends UnicastRemoteObject with RemoteServer {
  private var players = List[Player]()
  val enemy1 = new NPC(1, 1)
  val enemy2 = new NPC(3, 3)
  val enemies = List(enemy1, enemy2)
  var chars = enemies ++ players

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

  var level = new Level(maze1, enemies)
  chars.foreach(_.level = level)

  private var numPlayers = 0
  def main(args: Array[String]) {
	  connect
    println("Running")
    /*Future {
      while (true) {
        Thread.sleep(100)
        Future {
          joinGame(_)
          //timer.start()
        }
      }
    }*/
    //.(GameMap(testLevel))
  }

  def connect {
    registry.LocateRegistry.createRegistry(1099)
    Naming.rebind("GameServer", this)
  }

  def joinGame(client: RemoteClient): RemotePlayer = {
    val p = new Player(client, 8, 8)
    players ::= p
    //
    chars synchronized {
      //players += List[Player](new Player(client, 8, 8))
      level.addEntity(p)
      chars = level.characters
      numPlayers += 1
      println("SOMEONE JOINED! Amount of players present: " + numPlayers)
      //Actor.actor {
      players.foreach(_.client.updateLevel(level.buildPassableLevel))
      //}
    }
    p
  }

  val timer = new Timer(100, Swing.ActionListener { ae =>
    level.updateAll
    players.foreach(_.client.updateLevel(level.buildPassableLevel))
    enemies.foreach(_.update)
  }).start()
}