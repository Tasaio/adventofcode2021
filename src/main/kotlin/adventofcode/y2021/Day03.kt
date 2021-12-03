import adventofcode.applyToEachColumn
import adventofcode.binaryStringToInt

fun main() {
    val day = Day03()
    println(day.part1())
    day.reset()
    println(day.part2())
}

class Day03 : Y2021Day(3) {
    private val input = fetchInput()

    override fun part1(): Number? {
        val gamma = input.applyToEachColumn {
            if (it.count { it == '1' } > it.count { it == '0' }) '1' else '0'
        }
        val epsilon = input.applyToEachColumn {
            if (it.count { it == '1' } > it.count { it == '0' }) '0' else '1'
        }
        return gamma.binaryStringToInt() * epsilon.binaryStringToInt()
    }

    override fun part2(): Number? {
        val skipOxy = Array(input.size) { false }
        val skipCo2 = Array(input.size) { false }

        for (i in 0 until input[0].length) {

            var countOxy = 0
            var countCo2 = 0

            input.indices.forEach {
                if (!skipOxy[it]) countOxy += if (input[it][i] == '1') 1 else -1
                if (!skipCo2[it]) countCo2 += if (input[it][i] == '1') 1 else -1
            }

            if (skipOxy.count { it } < input.size - 1) {
                input.indices.forEach {
                    val skipBit = if (countOxy >= 0) '0' else '1'
                    if (input[it][i] == skipBit) {
                        skipOxy[it] = true
                    }
                }
            }

            if (skipCo2.count { it } < input.size - 1) {
                input.indices.forEach {
                    val skipBit = if (countCo2 >= 0) '1' else '0'
                    if (input[it][i] == skipBit) {
                        skipCo2[it] = true
                    }
                }
            }
        }

        val oxy = input[ skipOxy.indexOf(false) ]
        val co2 = input[ skipCo2.indexOf(false) ]

        return Integer.parseInt(oxy, 2) * Integer.parseInt(co2, 2)
    }

}
