import adventofcode.*

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
    var grid = Grid(input.subList(2, input.size)).createExpandedGrid(50, 50, '.')

    fun generation(i: Int) {
        grid.forEach {
            val c = if (i % 2 == 1) line[0] else '.'
            val t = it.getNeighbors(diagonal = true, countOutOfBoundsNeighborsAs = c).copyOfListWithNewElement(it).sortedByYThenX()
            val n = t.map { if (it eq '#') '1' else '0' }.charArrayToString().binaryStringToInt()
            it.next(line[n])
        }
        grid.update()
    }

    override fun part1(): Number? {
        for (i in 0 until 2) {
            generation(i)
        }
        return grid.count { it eq '#' }
    }

    override fun part2(): Number? {
        for (i in 0 until 48) {
            generation(i)
        }
        return grid.count { it eq '#' }
    }
}
