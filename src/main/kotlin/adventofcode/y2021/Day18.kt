import adventofcode.*
import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    val testInput = """
[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
    """.trimIndent()
    runDay(
        day = Day18::class,
        testInput = testInput,
        testAnswer1 = 4140,
        testAnswer2 = 3993
    )
}

open class Day18(staticInput: String? = null) : Y2021Day(18, staticInput) {
    private val input = fetchInput()

    override fun reset() {
        super.reset()
    }

    fun getPair(str: String, i: Int): Pair<Int, Int>? {
        if (str[i] == '[') {
            var pos = i + 1
            var leftStr = ""
            while (str[pos].isDigit()) {
                leftStr += str[pos++]
            }
            if (leftStr.isEmpty()) {
                return null
            }
            if (str[pos++] != ',') {
                return null
            }
            var rightStr = ""
            while (str[pos].isDigit()) {
                rightStr += str[pos++]
            }
            if (rightStr.isEmpty()) {
                return null
            }
            return Pair(leftStr.toInt(), rightStr.toInt())
        }
        return null
    }

    private fun addLeft(str: String, pos: Int, num: Int): String {
        var i = pos
        while (i > 0) {
            if (str[i].isDigit()) {
                var numStr = str[i].toString()
                if (str[i - 1].isDigit()) {
                    numStr = str[i - 1].toString() + str[i]
                }

                val newNum = numStr.toInt() + num
                return str.substring(0, i + 1 - numStr.length) + newNum + str.substring(i + 1)
            }
            i--
        }
        return str
    }

    private fun addRight(str: String, pos: Int, num: Int): String {
        for (i in pos..str.lastIndex) {
            if (str[i].isDigit()) {
                var numStr = str[i].toString()
                if (str[i + 1].isDigit()) {
                    numStr = str[i] + str[i + 1].toString()
                }

                val newNum = numStr.toInt() + num
                return str.substring(0, i) + newNum + str.substring(i + numStr.length)
            }
        }
        return str
    }

    fun Pair<Int, Int>.size(): Int {
        return 1 + first.toString().length + 1 + second.toString().length + 1
    }

    fun Pair<Int, Int>.toStringPair(): String {
        return "[$first,$second]"
    }

    fun Pair<Int, Int>.magnitude(): Int {
        return first * 3 + second * 2
    }

    private fun explode(str: String): String? {
        var cnt = 0
        var i = 0
        while (i < str.length) {
            if (str[i] == '[') {
                val pair = getPair(str, i)
                if (pair == null) {
                    cnt++
                } else {
                    if (cnt == 4) {
                        val left = addLeft(str, i - 1, pair.first)
                        val leftAddedSize = left.length - str.length
                        val right = addRight(left, i + pair.size(), pair.second)
                        return right.substring(0, i + leftAddedSize) + "0" + right.substring(i + pair.size() + leftAddedSize)
                    }
                    cnt++
                }
            } else if (str[i] == ']') {
                cnt--
            }
            i++
        }
        return null
    }

    fun split(str: String): String? {
        var i = 0
        while (i < str.length) {
            if (str[i].isDigit() && str[i + 1].isDigit()) {
                val num = (str[i].toString() + str[i + 1].toString()).toInt()
                val left = floor(num.toDouble() / 2).toInt()
                val right = ceil(num.toDouble() / 2).toInt()
                val pair = Pair(left, right)
                return str.substring(0, i) + pair.toStringPair() + str.substring(i + 2)
            }
            i++
        }
        return null
    }

    fun magnitude(str: String): BigInteger {
        var newStr = str
        while (true) {
            var i = 0
            while (i < newStr.length) {
                if (newStr[i] == '[') {
                    val pair = getPair(newStr, i)
                    if (pair != null) {
                        newStr = newStr.substring(0, i) + pair.magnitude() + newStr.substring(i + pair.size())
                        break
                    }
                }
                i++
            }
            if (newStr.count { it == '[' } == 0 && newStr.count { it == ']' } == 0) {
                return newStr.parseNumbersToSingleBigInteger()
            }
        }
    }

    fun reduce(str: String): String {
        var r = str
        while (true) {
            val e = explode(r)
            if (e != null) {
                r = e
                continue
            }
            val s = split(r)
            if (s != null) {
                r = s
                continue
            }
            return r
        }
    }

    override fun part1(): Number? {
        var q = input[0]

        for (a in input.subList(1, input.size)) {
            q = "[$q,$a]"
            q = reduce(q)
        }

        return magnitude(q)
    }

    override fun part2(): Number? {
        val max = MaxValue()

        for (a in input.indices) {
            for (b in input.indices) {
                if (a == b) continue
                val q = "[${input[a]},${input[b]}]"
                val r = reduce(q)
                max.next(magnitude(r))
            }
        }

        return max.get()
    }
}
