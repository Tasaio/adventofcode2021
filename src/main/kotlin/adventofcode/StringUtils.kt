package adventofcode

import java.math.BigInteger

fun String.parseNumbersToSingleInt(): Int {
    val sb = StringBuilder()
    for (c in this.toCharArray()) {
        if (c.isDigit() || (sb.isEmpty() && c == '-')) {
            sb.append(c)
        }
    }
    return sb.toString().toInt()
}

fun String.binaryStringToInt(): Int {
    return Integer.parseInt(this, 2);
}

fun String.parseNumbersToSingleBigInteger(): BigInteger {
    val sb = StringBuilder()
    for (c in this.toCharArray()) {
        if (c.isDigit() || (sb.isEmpty() && c == '-')) {
            sb.append(c)
        }
    }
    return sb.toString().toBigInteger()
}

fun String.parseNumbersToIntList(): List<Int> {
    return parseNumbersToBigIntegerList().map { it.toInt() }
}

fun String.parseNumbersToBigIntegerList(): List<BigInteger> {
    val list = arrayListOf<BigInteger>()
    val sb = StringBuilder()
    for (c in this) {
        if (c.isDigit() || (sb.isEmpty() && (c == '-' || c == '+'))) {
            sb.append(c)
        } else if (sb.isNotEmpty()) {
            list.add(sb.toString().toBigInteger())
            sb.clear()
        }
    }
    if (sb.isNotEmpty()) {
        list.add(sb.toString().toBigInteger())
    }
    return list
}

fun String.countOfEachCharacter(): Map<Char, Int> {
    val counter = hashMapOf<Char, Int>()
    for (c in this) {
        counter.compute(c) { _, b -> if (b == null) 1 else b + 1 }
    }
    return counter
}

fun Char.toIntValue(): Int {
    return when(this) {
        '0' -> 0
        '1' -> 1
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        else -> throw Exception("Not a int char: $this")
    }
}

fun String.textBetween(start: Char, end: Char): List<String> {
    val found = arrayListOf<String>()
    val sb = StringBuilder()
    var between = false
    for (c in this) {
        if (between) {
            if (c == end) {
                between = false
                found.add(sb.toString())
                sb.clear()
            } else {
                sb.append(c)
            }
        } else if (c == start) {
            between = true
        }
    }
    return found
}
