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
  def updateLevel
  def addClientToServer
  //def issueCMD
}

object Client extends UnicastRemoteObject with RemoteClient {
  var cLevel: Level = null
  override def updateLevel {
    ServerMain.level.updateAll
    drawPanel.repaint()
  }

  var clients = List[Player]()
  def players = clients
  def addClientToServer = for (i <- ServerMain.players.indices) clients ::= ServerMain.players(i)

  val width = 600
  val height = 400

  val drawPanel: Panel = new Panel {
    override def paint(g: Graphics2D): Unit = {
      Renderer.render(g, ServerMain.level, width, height)
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
    menuBar = new MenuBar {
      /*contents += new Menu("File") {
          contents += new MenuItem(Action("Pause")(timer.stop()))
          contents += new MenuItem(Action("Resume")(timer.start()))
          contents += new MenuItem(Action("Save")(save))
          contents += new MenuItem(Action("Load")(load))

        }*/
    }
  }

  def main(args: Array[String]) {
    val server: RemoteServer = Naming.lookup("rmi://localhost/GameServer") match {
      case s: RemoteServer => s
      case _ => throw new RuntimeException("Server was not server :( :( :(")
    }
    //val playerNumber = 
    addClientToServer
    server.joinGame(this)
    frame.open()
  }
  
 /*
      def save: Unit = {
      val chooser = new FileChooser
      if (chooser.showSaveDialog(drawPanel) == FileChooser.Result.Approve) {
        val oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(chooser.selectedFile)))
        oos.writeObject(level)
        oos.close()
      }
    }

    def load: Unit = {
      val chooser = new FileChooser
      if (chooser.showSaveDialog(drawPanel) == FileChooser.Result.Approve) {
        val ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(chooser.selectedFile)))
        ois.readObject match {
          case l: Level =>
            level = l
            drawPanel.repaint
          case _ => println("That's not a level.")
        }
        ois.close()))))))))
      }
    }
  } */
}