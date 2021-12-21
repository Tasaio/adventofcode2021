package adventofcode

import java.lang.RuntimeException
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

fun List<Char>.charListToString(): String {
    return joinToString(separator = "")
}

fun String.binaryStringToInt(): Int {
    return Integer.parseInt(this, 2);
}

fun String.binaryStringToBigInteger(): BigInteger {
    return BigInteger(this, 2)
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

fun String.toPair(split: String = ","): Pair<String, String> {
    val split = split(split)
    if (split.size > 2) {
        throw RuntimeException("Too many values in $this")
    }
    return Pair(split[0], split[1])
}

fun String.toTriple(split: String = ","): Triple<String, String, String> {
    val split = split(split)
    if (split.size > 3) {
        throw RuntimeException("Too many values in $this")
    }
    return Triple(split[0], split[1], split[2])
}

fun String.stringBetween(before: String, after: String): String {
    return this.substringAfter(before).substringBefore(after)
}

fun String.isUpperCase(): Boolean {
    return all { it.isUpperCase() }
}

fun String.isLowerCase(): Boolean {
    return all { it.isLowerCase() }
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

fun String.countOfEachCharacter(): BigIntegerMap<Char> {
    val counter = BigIntegerMap<Char>()
    forEach { counter.inc(it) }
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
