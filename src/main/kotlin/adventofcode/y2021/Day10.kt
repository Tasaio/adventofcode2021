import java.math.BigInteger
import java.util.*

fun main() {
    val testInput = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent()
    val test = Day10(testInput)
    println("TEST: " + test.part1())
    println("TEST: " + test.part2())

    val day = Day10()

    val t1 = System.currentTimeMillis()
    println("${day.part1()}   (${System.currentTimeMillis() - t1}ms)")
    day.reset()

    val t2 = System.currentTimeMillis()
    println("${day.part2()}   (${System.currentTimeMillis() - t2}ms)")
}

open class Day10(staticInput: String? = null) : Y2021Day(10, staticInput) {
    private val input = fetchInput()

    override fun reset() {
        super.reset()
    }

    override fun part1(): Number? {
        var c1 = 0;
        var c2 = 0;
        var c3 = 0;
        var c4 = 0;

        input.forEach {
            val stack = Stack<Char>()

            for (c in it) {
                if (c == '{' || c == '(' || c == '<' || c == '[') {
                    stack.push(c)
                } else {
                    if (c == ')') {
                        if (stack.isEmpty() || stack.pop()!! != '(') {
                            c1++
                            break
                        }
                    } else if (c == ']') {
                        if (stack.isEmpty() || stack.pop()!! != '[') {
                            c2++
                            break
                        }
                    } else if (c == '}') {
                        if (stack.isEmpty() || stack.pop()!! != '{') {
                            c3++
                            break
                        }
                    } else if (c == '>') {
                        if (stack.isEmpty() || stack.pop()!! != '<') {
                            c4++
                            break
                        }
                    }
                }
            }
        }

        return c1 * 3 + c2 * 57 + c3 * 1197 + c4 * 25137
    }

    override fun part2(): Number? {
        val scores = arrayListOf<BigInteger>()
        input.forEach {
            val stack = Stack<Char>()

            var corrupt = false
            var score = BigInteger.ZERO
            for (c in it) {

                if (c == '{' || c == '(' || c == '<' || c == '[') {
                    stack.push(c)
                } else {
                    if (c == ')') {
                        if (stack.pop()!! != '(') {
                            corrupt = true
                        }
                    } else if (c == ']') {
                        if (stack.pop()!! != '[') {
                            corrupt = true
                            break
                        }
                    } else if (c == '}') {
                        if (stack.pop()!! != '{') {
                            corrupt = true
                            break
                        }
                    } else if (c == '>') {
                        if (stack.pop()!! != '<') {
                            corrupt = true
                            break
                        }
                    }
                }
            }

            while (!corrupt && !stack.isEmpty()) {
                val c = stack.pop()
                if (c == '(') {
                    score = score * (5).toBigInteger() + (1).toBigInteger()
                } else if (c == '[') {
                    score = score * (5).toBigInteger() + (2).toBigInteger()
                } else if (c == '{') {
                    score = score * (5).toBigInteger() + (3).toBigInteger()
                } else if (c == '<') {
                    score = score * (5).toBigInteger() + (4).toBigInteger()
                } else {
                    corrupt = true
                }
            }

            if (!corrupt) {
                scores.add(score)
            }
        }

        return scores.sorted()[scores.size / 2]
    }
}
