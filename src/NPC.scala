import java.awt.Graphics2D
import java.awt.Color
import java.awt.geom.Rectangle2D

class NPC(var nx: Int, var ny: Int) extends Entity(nx, ny) {
  
  def drawNPC(g:Graphics2D) {
    g.setPaint(Color.yellow)
    g.fill(new Rectangle2D.Double((getx/level.maze.length)*800,((gety/level.maze.length)*600),
        8*level.maze.length,6*level.maze.length))
  }
  
  def seekNdestroy: Unit = {
    
  } 
    
  def move(px:Double, py:Double): Unit = {
/*    val dx = px-nx
    val dy = py-ny
    val len = math.sqrt(dx*dx+dy*dy)
    nx += dx/len
    ny += dy/len
*/
  }
}