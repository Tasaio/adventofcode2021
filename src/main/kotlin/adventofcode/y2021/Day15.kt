import adventofcode.*

fun main() {
    val testInput = """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent()
    runDay(
        day = Day15::class,
        testInput = testInput,
        testAnswer1 = 40,
        testAnswer2 = 315
    )
}

open class Day15(staticInput: String? = null) : Y2021Day(15, staticInput) {
    private val input = fetchInput()
    private val grid = Grid(input)

    override fun reset() {
        super.reset()
    }

    override fun part1(): Number? {
        val target = grid.bottomRightCorner()
        val fastest = grid.fastestWayToEachNode(
            canStepRule = { true },
            from = grid.topLeftCorner(),
            to = target,
            cost = { it.contentAsNum() }
        )
        return fastest[target]!!.first().cost
    }

    override fun part2(): Number? {
        val newGrid = arrayListOf<String>()

        for (x in 0..4) {
            input.forEach {
                var line = String()
                for (y in 0..4) {
                    it.forEach {
                        val next = RotatingNumber(1, 9, it.digitToInt()).plus(x + y).value.toInt()
                        line += next.digitToChar()
                    }
                }
                newGrid.add(line)
            }
        }

        val grid = Grid(newGrid)
        val target = grid.bottomRightCorner()

        val fastest = grid.fastestWayToEachNode(
            canStepRule = { true },
            from = grid.topLeftCorner(),
            to = target,
            cost = { it.contentAsNum() }
        )
        return fastest[target]!!.first().cost
    }

}
