abstract class Entity(private var x:Double, private var y:Double) extends Serializable {
  private var mLevel:Level = null
  println(x + " " + y)
  def getx = x
  
  def gety = y
  
  def level = mLevel
  
  def level_=(l:Level):Unit = mLevel = l
  
  def update():Unit = {
    val nx = x + util.Random.nextInt(3)-1
    val ny = y + util.Random.nextInt(3)-1
    if(nx>=0 && nx<level.maze.size && ny>=0 && ny<level.maze(0).size && level.maze(nx.toInt)(ny.toInt).canPass) {
      x = nx
      y = ny
    }
  }
  
  def toPassableEntity:PassableEntity = {
    new PassableEntity(x,y,0)
  }
}