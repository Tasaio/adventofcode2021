import adventofcode.*
import java.math.BigInteger

fun main() {
    val testInput = """
        16,1,2,0,4,2,7,1,2,14
    """.trimIndent()
    val test = Day07(testInput)
    println("TEST: " + test.part1())
    println("TEST: " + test.part2())

    val day = Day07()
    val t1 = System.currentTimeMillis()
    println("${day.part1()}   (${System.currentTimeMillis() - t1}ms)")
    day.reset()

    val t2 = System.currentTimeMillis()
    println("${day.part2()}   (${System.currentTimeMillis() - t2}ms)")
}

open class Day07(staticInput: String? = null) : Y2021Day(7, staticInput) {
    private val input = fetchInputAsSingleLineInteger(",")

    override fun part1(): Number? {
        val minValue = MinValue()
        for (i in input.minOf { it }..input.maxOf { it }) {
            val sum = input.sumOf { (it - i).toBigInteger().abs() }
            minValue.next(sum)
        }
        return minValue.get()
    }

    override fun part2(): Number? {
        val minValue = MinValue()
        for (i in input.minOf { it }..input.maxOf { it }) {
            val sum = input.sumOf { fuelCost((it - i).toBigInteger().abs()) }
            minValue.next(sum)
        }
        return minValue.get()
    }

    val mem = hashMapOf<Int, BigInteger>()

    fun fuelCost(i: BigInteger): BigInteger {
        if (mem.containsKey(i.toInt())) return mem[i.toInt()]!!
        var sum = BigInteger.ZERO
        for (j in 1..i.toInt()) {
            sum += j.toBigInteger()
            mem[j] = sum
        }
        return sum
    }
}
