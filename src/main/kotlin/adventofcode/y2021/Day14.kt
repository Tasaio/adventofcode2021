import adventofcode.BigIntegerMap
import adventofcode.countOfEachCharacter
import adventofcode.runDay

fun main() {
    val testInput = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent()
    runDay(
        day = Day14::class,
        testInput = testInput,
        testAnswer1 = 1588,
        testAnswer2 = 2188189693529
    )
}

open class Day14(staticInput: String? = null) : Y2021Day(14, staticInput) {
    private val input = fetchInput()

    private val template = input[0]
    private val rules: MutableMap<Pair<Char, Char>, Char> = hashMapOf()

    init {
        input.filter { it.contains("->") }
            .forEach { rules[Pair(it[0], it[1])] = it[6] }
    }

    override fun reset() {
        super.reset()
    }

    override fun part1(): Number? {
        var str = template
        for (i in 0..9) {
            val nextStr = StringBuffer()
            str.indices.forEach { i ->
                nextStr.append(str[i])
                if (str.length > i + 1) {
                    val rule = rules.get(Pair(str[i], str[i + 1]))
                    if (rule != null) {
                        nextStr.append(rule)
                    }
                }
            }
            str = nextStr.toString()
        }
        val map = str.countOfEachCharacter()
        return map.maxOf { it.value } - map.minOf { it.value }
    }

    override fun part2(): Number? {
        var dict = BigIntegerMap<Pair<Char, Char>>()
        val count = template.countOfEachCharacter()

        template.indices.forEach { i ->
            if (template.length > i + 1) {
                dict.inc(Pair(template[i], template[i + 1]))
            }
        }

        for (i in 1..40) {
            val newDict = BigIntegerMap<Pair<Char, Char>>()
            dict.forEach { (pair, c) ->
                val toInsert = rules[pair]
                if (toInsert != null) {
                    count[toInsert] += c
                    newDict[Pair(pair.first, toInsert)] += c
                    newDict[Pair(toInsert, pair.second)] += c
                }
            }
            dict = newDict
        }

        return count.maxOf { it.value } - count.minOf { it.value }
    }
}
