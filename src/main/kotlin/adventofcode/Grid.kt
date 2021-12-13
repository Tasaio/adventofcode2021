package adventofcode

import java.lang.Integer.max
import java.math.BigInteger
import java.util.*
import kotlin.math.absoluteValue

class GridNode(val parent: Grid, var posX: Int, var posY: Int, var content: Char) {
    var nextContent: Char? = null
    var storedValue: BigInteger? = null

    fun coordinate(): Coordinate {
        return Coordinate(posX, posY)
    }

    fun getNeighbors(distance: Int = 1, diagonal: Boolean = false, straight: Boolean = true, wrap: Boolean = false): List<GridNode> {
        val list = arrayListOf<GridNode>()
        for (x in posX - distance..posX + distance) {
            for (y in posY - distance..posY + distance) {
                var _x = x
                var _y = y
                if (!straight && (x == posX || y == posY))
                    continue
                else if (!diagonal && x != posX && y != posY) {
                    continue
                } else if (x == posX && y == posY) {
                    continue
                } else if (x < 0 || y < 0 || y >= parent.grid.size || x >= parent.grid[y].size) {
                    if (wrap) {
                        if (x < 0) {
                            _x = parent.grid[0].size + x
                        }
                        if (y < 0) {
                            _y = parent.grid.size + y
                        }
                        if (x >= parent.grid[0].size) {
                            _x = 0 + (parent.grid[y].size - x)
                        }
                        if (y >= parent.grid.size) {
                            _y = 0 + (parent.grid.size - y)
                        }
                    } else {
                        continue
                    }
                }
                list.add(parent.get(_x, _y))
            }
        }
        return list
    }

    fun contentAsNum(): Int {
        if (!content.isDigit()) {
            throw RuntimeException("Unexpected character" + content)
        }
        return content.digitToInt()
    }

    fun get(dir: Direction): GridNode? {
        return parent.get(coordinate().moveBy(dir.coordinateForDirection(1)))
    }

    fun search(filter: (GridNode) -> Boolean, changeX: Int, changeY: Int, count: Int = 1): List<GridNode> {
        val list = arrayListOf<GridNode>()
        var x = posX + changeX
        var y = posY + changeY
        var found = 0
        while (x >= 0 && y >= 0 && x < parent.grid.size && y < parent.grid[posX].size) {
            if (filter.invoke(parent.get(x, y))) {
                list.add(parent.get(x, y))
                if (++found >= count) {
                    break
                }
            }
            x += changeX
            y += changeY
        }
        return list
    }

    fun searchInEachDirection(filter: (GridNode) -> Boolean): List<GridNode> {
        return arrayListOf(
            search(filter, -1, 1),
            search(filter, 0, 1),
            search(filter, 1, 1),
            search(filter, 1, 0),
            search(filter, 1, -1),
            search(filter, 0, -1),
            search(filter, -1, -1),
            search(filter, -1, 0)
        ).flatten()
    }

    fun move(changeX: Int, changeY: Int) {
        parent.moveNode(this, changeX, changeY)
    }

    fun next(next: Char) {
        this.nextContent = next;
    }

    fun isZero(): Boolean {
        return posX == 0 && posY == 0
    }

    fun update() {
        if (nextContent != null) {
            content = nextContent!!
            nextContent = null
        }
    }

    val cachedDistances = hashMapOf<Coordinate, BigInteger>()

    fun manhattanDistanceTo(node: GridNode): BigInteger {
        return cachedDistances.computeIfAbsent(node.coordinate()) { node ->
            val distanceX = (posX.toBigInteger() - node.x).abs()
            val distanceY = (posY.toBigInteger() - node.y).abs()
            distanceX + distanceY
        }
    }

    infix fun eq(c: Char): Boolean {
        return content == c
    }

    infix fun eqAny(c: Collection<Char>): Boolean {
        return c.contains(content)
    }

    infix fun neq(c: Char): Boolean {
        return content != c
    }

    override fun toString(): String {
        return "GridNode(posX=$posX, posY=$posY, content=$content)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GridNode

        if (posX != other.posX) return false
        if (posY != other.posY) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = posX
        result = 31 * result + posY
        result = 31 * result + content.hashCode()
        return result
    }
}

fun Iterable<GridNode>.sortedByDistanceTo(node: GridNode): List<GridNode> {
    return sortedBy { node.manhattanDistanceTo(it) }
}

fun Iterable<GridNode>.sortedByXThenY(): List<GridNode> {
    return sortedBy { it.posX + it.posY * it.parent.sizeX() }
}

open class Grid(val input: List<String>?) : Iterable<GridNode> {
    var grid = LinkedList<LinkedList<GridNode>>()

    init {
        reset()
    }

    constructor (sizeX: Int, sizeY: Int, defaultContent: Char) : this(null) {
        for (y in 0 until sizeY) {
            grid.add(LinkedList())
            for (x in 0 until sizeX) {
                grid[y].add(GridNode(this, x, y, defaultContent))
            }
        }
    }

    constructor (sizeX: BigInteger, sizeY: BigInteger, defaultContent: Char) : this(
        sizeX.toInt(),
        sizeY.toInt(),
        defaultContent
    )

    fun reset() {
        grid = LinkedList<LinkedList<GridNode>>()
        if (input != null) {
            for (y in input.indices) {
                val list = LinkedList<GridNode>()
                for (x in input[y].indices) {
                    list.add(GridNode(this, x, y, input[y][x]))
                }
                grid.add(list)
            }
        }
    }

    open fun moveNode(node: GridNode, changeX: Int, changeY: Int) {
        val oldX = node.posX
        val oldY = node.posY
        val newX = oldX + changeX
        val newY = oldY + changeY
        val temp = get(newX, newY)

        node.posX = newX
        node.posY = newY

        temp.posX = oldX
        temp.posY = oldY

        grid[newY][newX] = node
        grid[oldY][oldX] = temp
    }

    fun get(x: Int, y: Int): GridNode {
        return grid[y][x]
    }

    fun size(): Int {
        return sizeX() * sizeY()
    }

    fun sizeX(): Int {
        return grid[0].size
    }

    fun sizeY(): Int {
        return grid.size
    }

    fun get(c: Coordinate): GridNode? {
        val x = c.x.toInt()
        val y = c.y.toInt()
        if (x >= sizeX() || y >= sizeY() || x < 0 || y < 0) {
            return null
        }
        return grid[y][x]
    }

    fun findOne(filter: (GridNode) -> Boolean): GridNode {
        val list = filter(filter);
        if (list.size > 1) {
            throw Exception("Matches multiple")
        } else if (list.size == 0) {
            throw Exception("Matches none")
        }
        return list[0]
    }

    fun findSequences(toFind: List<Char>, expectedResults: Int? = null): List<List<GridNode>> {
        val findFirstNode = filter { it eq toFind[0] }

        val searchRight = findFirstNode.filter {
            for (x in 1 until toFind.size) {
                val c = get(it.coordinate().move(x, 0))
                if (c == null || !(c eq toFind[x])) {
                    return@filter false
                }
            }
            true
        }

        val searchDown = findFirstNode.filter {
            for (y in 1 until toFind.size) {
                val c = get(it.coordinate().move(0, y))
                if (c == null || !(c eq toFind[y])) {
                    return@filter false
                }
            }
            true
        }

        val found = searchRight.size + searchDown.size
        if (found == 0) {
            throw Error("Could not find $toFind")
        }
        if (expectedResults != null && found != expectedResults) {
            throw Error("Expected $expectedResults, but found $found matches: $toFind")
        }

        val result = arrayListOf<List<GridNode>>()
        result.addAll(searchRight.map {
            val c = it.coordinate()
            var count = 0
            toFind.map {
                get(c.move(count++, 0))!!
            }
        })

        result.addAll(searchDown.map {
            val c = it.coordinate()
            var count = 0
            toFind.map {
                get(c.move(0, count++))!!
            }
        })

        return result
    }

    fun getAll(): List<GridNode> {
        return grid.flatten()
    }

    fun flipX() {
        grid.forEach { it.reverse() }
    }

    fun flipY() {
        grid.reverse()
    }

    fun getBorderTiles(direction: Direction? = null): List<GridNode> {
        val borderTiles = arrayListOf<GridNode>()
        if (direction == null || direction == Direction.UP) {
            for (x in 0 until sizeX()) {
                borderTiles.add(get(x, 0))
            }
        }
        if (direction == null || direction == Direction.RIGHT) {
            for (y in 0 until sizeY()) {
                borderTiles.add(get(sizeX() - 1, y))
            }
        }
        if (direction == null || direction == Direction.DOWN) {
            for (x in 0 until sizeX()) {
                borderTiles.add(get(x, sizeY() - 1))
            }
        }
        if (direction == null || direction == Direction.LEFT) {
            for (y in 0 until sizeY()) {
                borderTiles.add(get(0, y))
            }
        }
        return borderTiles
    }

    fun rotate(): Grid {
        val newInput = arrayListOf<String>()
        for (y in 0 until sizeX()) {
            var newLine = ""
            for (x in 0 until sizeY()) {
                newLine = get(Coordinate(y, x))!!.content + newLine
            }
            newInput += newLine
        }
        return Grid(newInput)
    }

    fun fastestWayToEachNode(
        canStepRule: (GridNode) -> Boolean,
        from: GridNode,
        to: Collection<GridNode>,
        diagonal: Boolean = false
    ): Map<GridNode, Set<List<GridNode>>> {

        val fastestToNode = hashMapOf<GridNode, HashSet<List<GridNode>>>()

        val q = LinkedList<List<GridNode>>()

        from.getNeighbors(diagonal = diagonal)
            .filter { canStepRule.invoke(it) }
            .forEach { q.add(arrayListOf(it)) }

        while (!q.isEmpty()) {
            val cur = q.poll()
            val last = cur.last()
            val size = cur.size

            if (fastestToNode.containsKey(last)) {
                val savedSize = fastestToNode[last]!!.first().size
                if (savedSize > size) {
                    fastestToNode[last] = hashSetOf(ArrayList(cur))
                } else if (savedSize == size) {
                    fastestToNode[last]!!.add(ArrayList(cur))
                    continue
                } else {
                    continue
                }
            } else {
                fastestToNode[last] = hashSetOf(ArrayList(cur))
            }

            last.getNeighbors(diagonal = diagonal)
                .filter { canStepRule.invoke(it) }
                .forEach { q.add(cur.copyOfListWithNewElement(it)) }
        }

        return fastestToNode.filter { to.contains(it.key) }
    }

    fun merge(
        otherGrid: Grid, calculation: (GridNode?, GridNode?) -> Char,
        fromX: Int = 0, fromY: Int = 0, toX: Int = max(sizeX(), otherGrid.sizeX()), toY: Int = max(sizeY(), otherGrid.sizeY())
    ): Grid {
        val list = arrayListOf<String>()
        for (y in fromY until toY) {
            var row = ""
            for (x in fromX until toX) {
                val c = Coordinate(x, y)
                row += calculation.invoke(get(c), otherGrid.get(c))
            }
            list.add(row)
        }
        return Grid(list)
    }

    fun update() {
        forEach { it.update() }
    }

    fun clone(): Grid {
        val newGrid = Grid(null)
        for (y in 0 until sizeY()) {
            val list = LinkedList<GridNode>()
            for (x in 0 until sizeX()) {
                list.add(GridNode(newGrid, x, y, grid[y][x].content))
            }
            newGrid.grid.add(list)
        }
        return newGrid
    }

    fun partOfGrid(fromY: Int = 0, toY: Int = sizeY() - 1, fromX: Int = 0, toX: Int = sizeX() - 1): Grid {
        val list = arrayListOf<String>()
        for (y in fromY..toY) {
            var row = ""
            for (x in fromX..toX) {
                row += get(Coordinate(x, y))!!.content
            }
            list.add(row)
        }
        return Grid(list)
    }

    fun printDebug(fromY: Int? = null, toY: Int? = null) {
        println("--- Grid ---")
        var finalRowDigits = (sizeY() - 1).toString().length
        var row = 0
        grid.forEach {
            val rowTwoDigits = row++.toString().padStart(finalRowDigits, '0') + ": "
            if ((fromY == null || row >= fromY) && (toY == null || row <= toY)) {
                rowTwoDigits.forEach { print(it) }
                it.forEach {
                    print(it.content)
                }
                println()
            }
        }
        println("------------")
    }

    fun printReadable(fromY: Int? = null, toY: Int? = null) {
        println("--- Grid ---")
        var row = 0
        grid.forEach {
            if ((fromY == null || row >= fromY) && (toY == null || row <= toY)) {
                it.forEach {
                    print(readable(it.content))
                }
                println()
            }
        }
        println("------------")
    }

    private fun readable(c: Char): Char {
        if (c == '#') {
            return '█'
        } else if (c == '.') {
            return ' '
        }
        return c
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grid

        if (grid != other.grid) return false

        return true
    }

    override fun hashCode(): Int {
        return grid.hashCode()
    }

    override fun iterator(): Iterator<GridNode> {
        return getAll().iterator()
    }
}

fun coordinatesToGrid(map: Map<Coordinate, Char>, defaultChar: Char = ' '): Grid {
    val minMax = map.keys.getMinMaxValues()

    val list = arrayListOf<String>()
    for (y in minMax.minY.toLong()..minMax.maxY.toLong()) {
        list.add("")
        for (x in minMax.minX.toLong()..minMax.maxX.toLong()) {
            list[list.lastIndex] = list[list.lastIndex] + map.getOrDefault(Coordinate(x, y), defaultChar)
        }
    }
    return Grid(list)
}

class ExpandingGrid(val defaultChar: Char = ' ') : Grid(null) {
    var originMoveX = 0
    var originMoveY = 0

    init {
        grid.add(LinkedList())
        grid.first.add(GridNode(this, 0, 0, defaultChar))
    }

    fun origin(): GridNode {
        return get(originMoveX, originMoveY)
    }

    fun expand(changeX: Int, changeY: Int) {
        if (changeX > 0) {
            for (i in 0..changeX) {
                grid.addLast(LinkedList())
                for (j in 0 until grid.first.size) grid.last.add(GridNode(this, grid.lastIndex, j, defaultChar))
            }
        } else if (changeX < 0) {
            originMoveX += changeX.absoluteValue
            for (i in 0 until changeX.absoluteValue) {
                forEach { it.posX++ }
                grid.addFirst(LinkedList())
                for (j in 0 until grid.last.size) grid.first.add(GridNode(this, 0, j, defaultChar))
            }
        }
        if (changeY > 0) {
            for (i in 0..changeY) {
                for (j in 0 until grid.size) {
                    grid[j].addLast(GridNode(this, j, grid.first.lastIndex + 1, defaultChar))
                }
            }
        } else if (changeY < 0) {
            originMoveY += changeY.absoluteValue
            for (i in 0 until changeY.absoluteValue) {
                forEach { it.posY++ }
                for (j in 0 until grid.size) {
                    grid[j].addFirst(GridNode(this, j, 0, defaultChar))
                }
            }
        }
    }

    override fun moveNode(node: GridNode, changeX: Int, changeY: Int) {
        if (node.posX + changeX >= grid.size) expand((node.posX + changeX) - grid.size + 1, 0)
        if (node.posX + changeX < 0) expand(node.posX + changeX, 0)
        if (node.posY + changeY >= grid[node.posX].size) expand(0, (node.posY + changeY) - grid[node.posX].size + 1)
        if (node.posY + changeY < 0) expand(0, node.posY + changeY)
        super.moveNode(node, changeX, changeY)
    }
}
