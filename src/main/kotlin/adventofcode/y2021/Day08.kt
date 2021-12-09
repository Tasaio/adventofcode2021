import adventofcode.allPermutations

fun main() {
    val testInput = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent()
    val test = Day08(testInput)
    println("TEST: " + test.part1())
    test.reset()
    println("TEST: " + test.part2())

    val day = Day08()

    val t1 = System.currentTimeMillis()
    println("${day.part1()}   (${System.currentTimeMillis() - t1}ms)")
    day.reset()

    val t2 = System.currentTimeMillis()
    println("${day.part2()}   (${System.currentTimeMillis() - t2}ms)")
}

class Day08(staticInput: String? = null) : Y2021Day(8, staticInput) {
    private val input =
        fetchInput().map { it.split('|').map { it.split(' ').filter { it.isNotBlank() }.map { it.trim() } } }

    private val displayDigits = hashMapOf<Int, Set<Char>>(
        Pair(0, hashSetOf('a', 'b', 'c', 'e', 'f', 'g')),
        Pair(1, hashSetOf('c', 'f')),
        Pair(2, hashSetOf('a', 'c', 'd', 'e', 'g')),
        Pair(3, hashSetOf('a', 'c', 'd', 'f', 'g')),
        Pair(4, hashSetOf('b', 'c', 'd', 'f')),
        Pair(5, hashSetOf('a', 'b', 'd', 'f', 'g')),
        Pair(6, hashSetOf('a', 'b', 'd', 'e', 'f', 'g')),
        Pair(7, hashSetOf('a', 'c', 'f')),
        Pair(8, hashSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g')),
        Pair(9, hashSetOf('a', 'b', 'c', 'd', 'f', 'g')),
    )

    override fun reset() {
        super.reset()
    }

    override fun part1(): Number? {
        val targetLength = hashSetOf(2, 3, 5, 7)
        input.forEach {
            it[1].forEach {
                if (targetLength.contains(it.length)) {
                    sum++
                }
            }
        }
        return sum
    }

    override fun part2(): Number? {
        val permutations = displayDigits[8]!!.allPermutations()
        return input.sumOf { line ->
            val matchingDic = permutations.firstNotNullOf { permutation ->
                val dic = hashMapOf<Char, Char>()
                for (i in 0..6) {
                    dic['a' + i] = permutation[i]
                }
                val match = line[0].all { displayDigits.containsValue( it.map { dic[it] }.toSet() ) }
                if (match) dic else null
            }
            var res = ""
            for (word in line[1]) {
                val translated = word.map { matchingDic[it] }.toSet()
                val digit = displayDigits.entries.filter { it.value == translated }.map { it.key }.single()
                res += digit.toString()
            }
            res.toInt()
        }
    }

}
