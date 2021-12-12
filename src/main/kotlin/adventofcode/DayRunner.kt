package adventofcode

import kotlin.reflect.KClass

fun <T: Day> runDay(day: KClass<T>, testInput: String, testAnswer1: Number? = null, testAnswer2: Number? = null) {
    val testInput = testInput.trimIndent()
    var test1Pass = false
    var test2Pass = false
    println("---- Test ----")
    if (testInput.isNotBlank()) {
        val test = day.constructors.first().call(testInput)
        val testPart1 = test.part1()
        if (testAnswer1 == null) {
            println("No test answer to part1 given")
        } else if (testPart1.toString() == testAnswer1.toString()) {
            test1Pass = true
        }
        println("TEST ${if (test1Pass) "OK" else "NOT OK"}: $testPart1 (test answer is ${testAnswer1})")
        test.reset()
        val testPart2 = test.part2()
        if (testAnswer2 == null) {
            println("No test answer to part2 given")
        } else if (testPart2 == testAnswer2) {
            test2Pass = true
        }
        println("TEST ${if (test2Pass) "OK" else "NOT OK"}: $testPart2 (test answer is ${testAnswer2})")
    } else {
        println("( no test input)")
    }
    println("--------------")
    println()
    println("---- Real ----")

    val day = day.constructors.first().call(null)

    // Part 1
    val t1 = System.currentTimeMillis()
    val result1 = day.part1()
    println("${result1}   (${System.currentTimeMillis() - t1}ms)")
    if (test1Pass) {
        postIfValid(day, 1, result1)
    } else {
        println("Not posting as test was invalid")
    }

    day.reset()

    // Part 2
    val t2 = System.currentTimeMillis()
    val result2 = day.part2()
    println("${result2}   (${System.currentTimeMillis() - t2}ms)")
    if (test2Pass) {
        postIfValid(day, 2, result2)
    } else {
        println("Not posting as test was invalid")
    }
    println("--------------")
}

private fun postIfValid(day: Day, part: Int, num: Number?) {
    if (num == null) {
        println("Not posting null answer")
        return
    }
    if (num == 0) {
        println("Not posting 0 answer")
    }
    day.postAnswer(part, num.toString())
}
