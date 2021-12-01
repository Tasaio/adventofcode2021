import java.math.BigInteger

fun main() {
    val day = Day01()
    println(day.part1())
    day.reset()
    println(day.part2())
}

class Day01 : Y2021Day(1) {
    val input = fetchInputAsBigInt()

    override fun part1(): Number? {
        var last: BigInteger? = null
        input.forEach {
            if (last != null && it > last) {
                counter++
            }
            last = it
        }
        return counter
    }

    override fun part2(): Number? {
        input.indices.forEach {
            if (it >= 3) {
                val oldSum = input[it - 3] + input[it - 2] + input[it - 1]
                val newSum = input[it - 2] + input[it - 1] + input[it]
                if (newSum > oldSum) {
                    counter++
                }
            }
        }
        return counter
    }

}
