import java.rmi.server.UnicastRemoteObject
import java.rmi._
import scala.actors.Actor
import collection.mutable

@remote trait RemoteServer {
  def joinGame(client: RemoteClient)
}

object ServerMain extends UnicastRemoteObject with RemoteServer {
  private var numPlayers = 0
  private val clients = mutable.Buffer[Player]()
  def main(args: Array[String]) {
    connect
    println("Running")
    //.(GameMap(testLevel))
  }

  def connect {
	  registry.LocateRegistry.createRegistry(1099)
	  Naming.rebind("GameServer", this)
  }
  
  def joinGame(client: RemoteClient): Unit = {
    clients synchronized {
      clients += new Player(null, 8, 8)
      println("SOMEONE JOINED!")
      numPlayers += 1
      Actor.actor {
        clients.foreach(_.client.gameStart)
      }
    }
  }

}