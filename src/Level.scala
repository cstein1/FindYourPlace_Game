class Level(private val intMaze: Array[Array[Int]], private var chars: List[Entity]) extends Serializable {
  def arrMaze = intMaze
  val maze = arrMaze.map(row => row.map(_ match {
    case 0 => Floor
    case 1 => Wall
    case 2 => Door
    case 3 => Hole
  }))

  def characters = chars
  def element(x: Int, y: Int): MapElement = { maze(x)(y) }
  def remCharacter(c: Character) = {}
  
  def addEntity(e:Entity):Unit = {
    chars ::= e
    e.level = this
    println("Player Added")
  }
  def updateAll:Unit = chars.foreach{ _.update }
}