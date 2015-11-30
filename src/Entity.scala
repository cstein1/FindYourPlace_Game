trait Entity extends Serializable {
  protected var x:Double
  protected var y:Double
  
  private var mLevel:Level = null
  
  def getx = x
  
  def gety = y
  
  def level = mLevel
  
  def level_=(l:Level):Unit = mLevel = l
  
  def update():Unit
  
  def makePassable:PassableEntity
}

object Entity {
  val enemyValue = 0
  val playerValue = 1
}