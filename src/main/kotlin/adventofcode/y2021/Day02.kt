import adventofcode.Coordinate
import adventofcode.parseNumbersToSingleBigInteger
import adventofcode.parseNumbersToSingleInt
import java.math.BigInteger

fun main() {
    val day = Day02()
    println(day.part1())
    day.reset()
    println(day.part2())
}

class Day02 : Y2021Day(2) {
    val input = fetchInput()

    override fun part1(): Number? {
        var c = Coordinate.ORIGIN
        input.forEach {
            if (it.startsWith("forward")) {
                c = c.move(it.parseNumbersToSingleInt(), 0)
            } else if (it.startsWith("down")) {
                c = c.move(0, it.parseNumbersToSingleInt())
            } else if (it.startsWith("up")) {
                c = c.move(0, -it.parseNumbersToSingleInt())
            }
        }
        return c.x * c.y
    }

    override fun part2(): Number? {
        var aim = BigInteger.ZERO
        var c = Coordinate.ORIGIN
        input.forEach {
            if (it.startsWith("forward")) {
                c = c.move(it.parseNumbersToSingleBigInteger(), aim * it.parseNumbersToSingleBigInteger())
            } else if (it.startsWith("down")) {
                aim += it.parseNumbersToSingleBigInteger()
            } else if (it.startsWith("up")) {
                aim -= it.parseNumbersToSingleBigInteger()
            }
        }
        return c.x * c.y
    }

}
