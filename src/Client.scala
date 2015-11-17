import java.awt.Dimension
import java.awt.Graphics2D
import java.rmi.Naming
import java.rmi.server.UnicastRemoteObject

import scala.swing.MainFrame
import scala.swing.MenuBar
import scala.swing.Panel
import scala.swing.Swing
import scala.swing.event.Key
import scala.swing.event.KeyPressed
import scala.swing.event.MouseClicked

import javax.swing.Timer

import scala.collection.mutable

// NEED TO SYNC UP CLIENTS AND ADD PLAYER

@remote trait RemoteClient {
  def updateLevel(l:PassableLevel)
  //def addClientToServer
  //def issueCMD
}

object Client extends UnicastRemoteObject with RemoteClient {
  val server = Naming.lookup("rmi://localhost/GameServer") match {
    case s: RemoteServer => s
    case _ => throw new RuntimeException("That's no server!")
  }
  //val player = server.joinGame(this)
  var level: PassableLevel = null
  def updateLevel(l: PassableLevel) {
    level = l
    drawPanel.repaint()
  }

  var clients = List[Player]()
  def players = clients
  //def addClientToServer = for (i <- ServerMain.players.indices) clients ::= ServerMain.players(i)

  val width = 600
  val height = 400
  val renderer= new Renderer
  val drawPanel: Panel = new Panel {
    override def paint(g: Graphics2D): Unit = {
      renderer.render(g, ServerMain.level, width, height)
    }
    preferredSize = new Dimension(width, height)

    listenTo(keys, mouse.clicks)
    reactions += {
      case e: KeyPressed =>
        println("KeyPressed")
        if (e.key == Key.Left) { clients.foreach(_.lPress) }
        if (e.key == Key.Right) { clients.foreach(_.rPress) }
        if (e.key == Key.Up) { clients.foreach(_.upPress) }
        if (e.key == Key.Down) { clients.foreach(_.dPress) }

        clients.foreach(_.update())
        //enemies.foreach(_.update)
        repaint
      case e: MouseClicked =>
        requestFocus()
    }
  }

  val frame = new MainFrame {
    title = "Don't Fall in the Holes. They are bad. Falling is bad."
    contents = drawPanel
  }

  def main(args: Array[String]) {
    //val playerNumber = 
    //addClientToServer
    server.joinGame(this)
    frame.open()
  }
}