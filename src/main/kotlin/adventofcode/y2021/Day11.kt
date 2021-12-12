import adventofcode.*
import java.math.BigInteger

fun main() {
    val testInput = """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent()
    runDay(
        day = Day11::class,
        testInput = testInput,
        testAnswer1 = 1656,
        testAnswer2 = 195
    )
}

open class Day11(staticInput: String? = null) : Y2021Day(11, staticInput) {
    private val grid = fetchInputAsGrid()

    override fun reset() {
        super.reset()
        grid.reset()
    }

    override fun part1(): Number? {
        for (i in 1..100) {
            var added = false
            grid.forEach {
                if (it.content != 'X') {
                    if (it.contentAsNum() + 1 >= 10) {
                        it.content = 'X'
                        added = true
                    } else {
                        it.content = (it.contentAsNum() + 1).digitToChar()
                    }
                }
            }
            val flashes = arrayListOf<GridNode>()
            while (added) {
                added = false
                grid.forEach {
                    if (it.content == 'X' && !flashes.contains(it)) {
                        flashes.add(it)
                        sum += BigInteger.ONE
                        it.getNeighbors(diagonal = true).forEach {
                            if (it.content != 'X') {
                                if (it.contentAsNum() + 1 >= 10) {
                                    it.content = 'X'
                                    added = true
                                } else {
                                    it.content = (it.contentAsNum() + 1).digitToChar()
                                }
                            }
                        }
                    }
                }
            }

            grid.forEach {
                if (it eq 'X') {
                    it.content = '0'
                }
            }
        }

        return sum
    }

    override fun part2(): Number? {
        for (i in 1..Integer.MAX_VALUE) {
            var added = false
            grid.forEach {
                if (it.content != 'X') {
                    if (it.contentAsNum() + 1 >= 10) {
                        it.content = 'X'
                        added = true
                    } else {
                        it.content = (it.contentAsNum() + 1).digitToChar()
                    }
                }
            }
            val flashes = arrayListOf<GridNode>()
            while (added) {
                added = false
                grid.forEach {
                    if (it.content == 'X' && !flashes.contains(it)) {
                        flashes.add(it)
                        sum += BigInteger.ONE
                        it.getNeighbors(diagonal = true).forEach {
                            if (it.content != 'X') {
                                if (it.contentAsNum() + 1 >= 10) {
                                    it.content = 'X'
                                    added = true
                                } else {
                                    it.content = (it.contentAsNum() + 1).digitToChar()
                                }
                            }
                        }
                    }
                }
            }

            if (flashes.size == grid.size()) {
                return i
            }

            grid.forEach {
                if (it eq 'X') {
                    it.content = '0'
                }
            }
        }

        return null
    }
}
