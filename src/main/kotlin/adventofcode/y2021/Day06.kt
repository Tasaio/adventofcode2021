import adventofcode.*
import java.math.BigInteger

fun main() {
    val testInput = "3,4,3,1,2"
    val test = Day06(testInput)
    println("TEST: " + test.part1())

    val day = Day06()
    println(day.part1())
    day.reset()
    println(day.part2())
}

open class Day06(staticInput: String? = null) : Y2021Day(6, staticInput) {
    private val input = fetchInputAsSingleLineBigInteger(",").map { it.toInt() }

    override fun part1(): Number? {
        val fish = input.map { RotatingNumber(0, 6, it) }.toMutableList()
        val newFish = arrayListOf<RotatingNumber>()

        var fishesToAdd: Int
        for (i in 1..80) {

            fishesToAdd = 0
            for (j in fish.indices) {
                if (fish[j].value == BigInteger.ZERO) {
                    fishesToAdd++
                }
                fish[j] = fish[j].dec()
            }

            for (j in 0 until fishesToAdd) {
                newFish.add(RotatingNumber(0, 6, 8))
            }

            fish.addAll(newFish)
            newFish.clear()
        }

        return fish.size
    }

    override fun part2(): Number? {
        var fishCount = input.size.toBigInteger()
        val addOnDay = BigIntegerMap<Int>()

        input.forEach { addOnDay.inc(it + 1) }

        for (i in 1..256) {
            fishCount += addOnDay[i]
            addOnDay[i + 7] += addOnDay[i]
            addOnDay[i + 9] += addOnDay[i]
        }

        return fishCount
    }
}
