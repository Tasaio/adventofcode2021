import adventofcode.parseNumbersToSingleInt
import java.lang.RuntimeException

fun main() {
    val day = Day04()
    println(day.part1())
    day.reset()
    println(day.part2())
}

class Day04 : Y2021Day(4) {
    private val input = fetchInput()

    private val numbers = input[0].split(",").map { it.parseNumbersToSingleInt() }
    private val boards = input.subList(1, input.size).chunked(6).map { Board(it.filter { it.isNotBlank() }) }

    private class Board(input: List<String>) {
        private val rows: List<List<Int>>
        private val marks = Array(5) { Array(5) { false} }

        init {
            rows = input.map { it.trim().split(Regex("[ ]+")).map { it.parseNumbersToSingleInt() } }
        }

        fun call(num: Int) {
            for (x in 0..4) {
                for (y in 0..4) {
                    if (rows[x][y] == num) {
                        marks[x][y] = true
                    }
                }
            }
        }

        fun bingo(): Boolean {
            upper@for (x in 0..4) {
                for (y in 0..4) {
                    if (!marks[x][y]) {
                        continue@upper
                    }
                }
                return true
            }
            upper@for (x in 0..4) {
                for (y in 0..4) {
                    if (!marks[y][x]) {
                        continue@upper
                    }
                }
                return true
            }
            return false
        }

        fun reset() {
            for (x in 0..4) {
                for (y in 0..4) {
                    marks[x][y] = false
                }
            }
        }

        fun score(winningNo: Int): Int {
            var sum = 0
            for (x in 0..4) {
                for (y in 0..4) {
                    if (!marks[x][y]) {
                        sum += rows[x][y]
                    }
                }
            }
            return sum * winningNo
        }
    }

    override fun reset() {
        boards.forEach { it.reset() }
    }

    override fun part1(): Number? {
        numbers.forEach { num ->
            boards.forEach { board ->
                board.call(num)
                if (board.bingo()) {
                    return board.score(num)
                }
            }
        }
        throw RuntimeException("Failed")
    }

    override fun part2(): Number? {
        val skip = HashSet<Int>()
        numbers.forEach { num ->
            boards.indices.forEach { boardNo ->
                if (!skip.contains(boardNo)) {
                    boards[boardNo].call(num)
                    if (boards[boardNo].bingo()) {
                        if (skip.size < boards.size - 1) {
                            skip.add(boardNo)
                        } else {
                            return boards[boardNo].score(num)
                        }
                    }
                }
            }
        }
        throw RuntimeException("Failed")
    }

}
