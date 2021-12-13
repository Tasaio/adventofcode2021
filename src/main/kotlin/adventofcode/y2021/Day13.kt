import adventofcode.*
import java.math.BigInteger

fun main() {
    val testInput = """
        6,10
        0,14
        9,10
        0,3
        10,4
        4,11
        6,0
        6,12
        4,1
        0,13
        10,12
        3,4
        3,0
        8,4
        1,10
        2,14
        8,10
        9,0

        fold along y=7
        fold along x=5
    """.trimIndent()
    runDay(
        day = Day13::class,
        testInput = testInput,
        testAnswer1 = 17,
        testAnswer2 = null
    )
}

open class Day13(staticInput: String? = null) : Y2021Day(13, staticInput) {
    private val input = fetchInput()

    val dots = input.filter { it.contains(",") }.map { it.toPair().toCoordinate() }
    val folds = input.filter { it.startsWith("fold") }

    var grid: Grid

    init {
        val maxXY = dots.getMinMaxValues()
        grid = Grid(maxXY.maxX + BigInteger.ONE, maxXY.maxY + BigInteger.ONE, '.')
        dots.forEach {
            grid.get(it)!!.content = '#'
        }
    }

    override fun reset() {
        super.reset()
    }

    override fun part1(): Number? {
        fold(folds[0])
        return grid.count { it eq '#'}
    }

    fun fold(line: String) {
        if (line.contains('y')) {
            val num = line.parseNumbersToSingleInt()
            val top = grid.partOfGrid(toY = num - 1)
            val bottom = grid.partOfGrid(fromY = num + 1)

            for (y in 0 until bottom.sizeY()) {
                for (x in 0 until bottom.sizeX()) {
                    if (bottom.get(x, y).content == '#') {
                        top.get(x, top.sizeY() - y - 1).content = '#'
                    }
                }
            }

            grid = top
        } else if (line.contains('x')) {
            val num = line.parseNumbersToSingleInt()
            val left = grid.partOfGrid(toX = num - 1)
            val right = grid.partOfGrid(fromX = num + 1)

            for (y in 0 until right.sizeY()) {
                for (x in 0 until right.sizeX()) {
                    if (right.get(x, y).content == '#') {
                        left.get(left.sizeX() - x - 1, y).content = '#'
                    }
                }
            }

            grid = left
        }
    }

    override fun part2(): Number? {
        for (fold in folds.subList(1, folds.size)) {
            fold(fold)
        }
        grid.printReadable()
        return null
    }
}
