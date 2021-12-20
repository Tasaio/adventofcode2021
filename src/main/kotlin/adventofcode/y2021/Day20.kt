import adventofcode.*
import java.util.*

fun main() {
    val testInput = """
        ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

        #..#.
        #....
        ##..#
        ..#..
        ..###
    """.trimIndent()
    runDay(
        day = Day20::class,
        testInput = testInput,
        testAnswer1 = 35,
        testAnswer2 = 3351
    )
}

open class Day20(staticInput: String? = null) : Y2021Day(20, staticInput) {
    private val input = fetchInput()

    val line = input[0]
    var grid = Grid(input.subList(2, input.size))
    val bigGrid: Grid

    val ADD = 50

    init {
        val newInput = LinkedList(input.subList(2, input.size))

        fun getLine(): String {
            var s = ""
            for (x in 0 until grid.sizeX()) {
                s += "."
            }
            return s
        }

        for (i in 0 until ADD) {
            newInput.addFirst(getLine())
            newInput.addLast(getLine())
        }
        var s = ""
        for (i in 0 until ADD) {
            s += "."
        }

        bigGrid = Grid(newInput.map { s + it + s })
    }

    override fun part1(): Number? {
        for (i in 0 until 2) {
            bigGrid.forEach {
                val c = if (i % 2 == 1) line[0] else '.'
                val t = it.getNeighbors(diagonal = true, countOutOfBoundsNeighborsAs = c).copyOfListWithNewElement(it).sortedByYThenX()
                val str = t.map { if (it eq '#') '1' else '0' }.charArrayToString()
                val n = str.binaryStringToInt()
                it.next(line[n])
            }
            bigGrid.update()
        }

        return bigGrid.count { it eq '#' }
    }

    override fun part2(): Number? {
        for (i in 0 until 48) {
            bigGrid.forEach {
                val c = if (i % 2 == 1) line[0] else '.'
                val t = it.getNeighbors(diagonal = true, countOutOfBoundsNeighborsAs = c).copyOfListWithNewElement(it).sortedByYThenX()
                val str = t.map { if (it eq '#') '1' else '0' }.charArrayToString()
                val n = str.binaryStringToInt()
                it.next(line[n])
            }
            bigGrid.update()
        }

        return bigGrid.count { it eq '#' }
    }
}
