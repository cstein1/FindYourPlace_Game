import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import java.awt.geom.Ellipse2D
import java.awt.Dimension
import java.rmi._

class Renderer extends Serializable {
  def render(g: Graphics2D, maze: PassableLevel, width: Int, height: Int): Unit = {
    var boxWidth: Double = width.toDouble / maze.maze.length
    var boxHeight: Double = height.toDouble / maze.maze(0).length
    for (i <- maze.maze.indices; j <- maze.maze(i).indices) {
      maze.maze(i)(j) match {
        case Floor  => g.setPaint(Color.white)
        case Wall => g.setPaint(Color.black)
      }
      g.fill(new Rectangle2D.Double(j * boxWidth, i * boxHeight, boxWidth * 10, boxHeight * 10))
    }
    for (e <- maze.entities) {
      e.etype match {
        case Entity.enemyValue => g.setPaint(Color.yellow)
        case Entity.playerValue => g.setPaint(Color.red)
      }
      g.fill(new Ellipse2D.Double(e.getx * boxWidth, e.gety * boxHeight, boxWidth, boxHeight))

    }
  }
}