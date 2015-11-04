import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import java.awt.geom.Ellipse2D
import java.awt.Dimension
import java.rmi._

object Renderer {
  def render(g: Graphics2D, maze: Level, width: Int, height: Int): Unit = {
    var boxWidth: Double = width.toDouble / maze.arrMaze.length
    var boxHeight: Double = height.toDouble / maze.arrMaze(0).length

    for (i <- maze.arrMaze.indices; j <- maze.arrMaze(i).indices) {
      maze.maze(i)(j) match {
        case Wall => g.setPaint(Color.white)
        case Floor => g.setPaint(Color.black)
        case Hole => g.setPaint(Color.white)
        case Door => g.setPaint(Color.YELLOW)
      }
      g.fill(new Rectangle2D.Double(i * boxWidth, j * boxHeight, boxWidth * 10, boxHeight * 10))
    }
    for (e <- maze.characters) {
      e match {
        case _: NPC => g.setPaint(Color.yellow)
        case _: Player => g.setPaint(Color.red)
      }
      g.fill(new Ellipse2D.Double(e.getx * boxWidth, e.gety * boxHeight, boxWidth, boxHeight))

    }
  }
}