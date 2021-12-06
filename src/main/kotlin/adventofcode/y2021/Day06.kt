import adventofcode.*
import java.math.BigInteger

fun main() {
    val day = Day06()
    println(day.part1())
    day.reset()
    println(day.part2())
}

class Day06 : Y2021Day(6) {
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
        val addOnDay = hashMapOf<Int, BigInteger>()

        input.forEach { addOnDay[it + 1] = addOnDay.getOrDefault(it + 1, BigInteger.ZERO) + BigInteger.ONE }

        for (i in 1..256) {
            fishCount += addOnDay.getOrDefault(i, BigInteger.ZERO)
            addOnDay[i + 7] = addOnDay.getOrDefault(i + 7, BigInteger.ZERO) + addOnDay.getOrDefault(i, BigInteger.ZERO)
            addOnDay[i + 9] = addOnDay.getOrDefault(i + 9, BigInteger.ZERO) + addOnDay.getOrDefault(i, BigInteger.ZERO)
        }

        return fishCount
    }

}
