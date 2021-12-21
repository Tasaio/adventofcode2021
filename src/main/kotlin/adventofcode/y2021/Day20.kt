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

    private val GENERATIONS = 50

    val line = input[0]
    var grid = input.subList(2, input.size).toGrid().createExpandedGrid(GENERATIONS, GENERATIONS, '.')

    private fun generation(i: Int) {
        val c = if (i % 2 == 1) line[0] else '.'
        grid.applyToAllAndUpdate {
            val n = it.getNeighbors(includeSelf = true, diagonal = true, countOutOfBoundsNeighborsAs = c)
                .map { if (it eq '#') '1' else '0' }
                .charListToString()
                .binaryStringToInt()
            line[n]
        }
    }

    override fun part1(): Number? {
        for (i in 0 until 2) {
            generation(i)
        }
        return grid.count { it eq '#' }
    }

    override fun part2(): Number? {
        for (i in 0 until GENERATIONS - 2) {
            generation(i)
        }
        return grid.count { it eq '#' }
    }
}
