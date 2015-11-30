import java.rmi.server.UnicastRemoteObject
import scala.swing.MainFrame
import scala.swing.event.KeyReleased
import java.awt.Graphics2D
import scala.swing.event.KeyPressed
import scala.swing.Panel
import java.awt.Dimension
import scala.swing.MenuItem
import scala.swing.Menu
import scala.swing.MenuBar
import scala.swing.Action
import scala.swing.event.Key
import java.rmi.Naming
import scala.swing.Button

@remote trait RemoteClient {
  def updateLevel(l:PassableLevel)
}

object Client extends UnicastRemoteObject with RemoteClient {
  val server = Naming.lookup("rmi://localhost/tacos") match {
    case s:RemoteServer => s
    case _ => throw new RuntimeException("That's no server.")
  }
  println(server)
  val player = server.joinGame(this)
  println(player)
  private var level:PassableLevel = null
  private val renderer = new Renderer
  val drawPanel = new Panel {
    override def paint(g: Graphics2D): Unit = {
      if(level!=null) renderer.render(g, level, size.width, size.height)
    }
    listenTo(keys)
    reactions += {
      case kp: KeyPressed =>
        println("Client key pressed")
        if (kp.key == Key.Up) player.upPressed
        else if (kp.key == Key.Down) player.downPressed
        else if (kp.key == Key.Left) player.leftPressed
        else if (kp.key == Key.Right) player.rightPressed
      case kr: KeyReleased =>
        if (kr.key == Key.Up) player.upReleased
        else if (kr.key == Key.Down) player.downReleased
        else if (kr.key == Key.Left) player.leftReleased
        else if (kr.key == Key.Right) player.rightReleased
    }
    preferredSize = new Dimension(800, 600)
  }
  def updateLevel(l:PassableLevel):Unit = {
    level = l
    if(drawPanel!=null) drawPanel.repaint()
  }

  val frame = new MainFrame {
    title = "Dont fall in the holes"
    contents = drawPanel
  }

  def main(args:Array[String]):Unit = {
    frame.open
    drawPanel.requestFocus
  }
}