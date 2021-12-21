import adventofcode.BigIntegerMap
import adventofcode.RotatingNumber
import adventofcode.parseNumbersToIntList
import adventofcode.runDay
import java.math.BigInteger

fun main() {
    val testInput = """
        Player 1 starting position: 4
        Player 2 starting position: 8
    """.trimIndent()
    runDay(
        day = Day21::class,
        testInput = testInput,
        testAnswer1 = 739785,
        testAnswer2 = "444356092776315".toBigInteger()
    )
}

open class Day21(staticInput: String? = null) : Y2021Day(21, staticInput) {
    private val input = fetchInput().map { it.parseNumbersToIntList()[1] }

    override fun part1(): Number? {
        var rolls = 0
        var p1 = RotatingNumber(1, 10, input[0])
        var p2 = RotatingNumber(1, 10, input[1])
        var die = RotatingNumber(1, 100)
        var score1 = BigInteger.ZERO
        var score2 = BigInteger.ZERO
        while (true) {
            var roll = BigInteger.ZERO
            for (i in 0 until 3) {
                rolls++
                roll += die.value
                die = die.inc()
            }
            p1 = p1.plus(roll)
            score1 += p1.value
            if (score1 >= (1000).toBigInteger()) {
                return rolls.toBigInteger() * score2
            }

            roll = BigInteger.ZERO
            for (i in 0 until 3) {
                rolls++
                roll += die.value
                die = die.inc()
            }
            p2 = p2.plus(roll)
            score2 += p2.value
            if (score2 >= (1000).toBigInteger()) {
                return rolls.toBigInteger() * score1
            }

        }
    }

    data class Game(val p1Score: Int, val p2Score: Int, val p1Pos: RotatingNumber, val p2Pos: RotatingNumber) {
        fun moveP1(steps: Int): Game {
            val p1 = p1Pos + steps
            val score = p1.value
            return Game(p1Score + score.toInt(), p2Score, p1, p2Pos)
        }

        fun moveP2(steps: Int): Game {
            val p2 = p2Pos + steps
            val score = p2.value
            return Game(p1Score, p2Score + score.toInt(), p1Pos, p2)
        }

        fun won(): Boolean {
            return p1Score >= 21 || p2Score >= 21
        }
    }

    override fun part2(): Number? {
        var map = BigIntegerMap<Game>()
        val rolls = arrayListOf<Int>()

        for (i in 1..3) {
            for (j in 1..3) {
                for (k in 1..3) {
                    rolls.add(i + j + k)
                }
            }
        }

        var winP1 = BigInteger.ZERO
        var winP2 = BigInteger.ZERO
        map[Game(
            0, 0,
            RotatingNumber(1, 10, input[0]),
            RotatingNumber(1, 10, input[1])
        )] = BigInteger.ONE

        var turnP1 = true

        while (map.isNotEmpty()) {
            val newMap = BigIntegerMap<Game>()
            for (e in map.entries) {
                for (r in rolls) {
                    val ng = if (turnP1) e.key.moveP1(r) else e.key.moveP2(r)
                    if (ng.won()) {
                        if (turnP1) {
                            winP1 += e.value
                        } else {
                            winP2 += e.value
                        }
                    } else {
                        newMap[ng] += e.value
                    }
                }
            }
            map = newMap
            turnP1 = !turnP1
        }

        return if (winP1 > winP2) winP1 else winP2
    }
}
