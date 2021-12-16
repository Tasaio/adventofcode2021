package adventofcode

import java.math.BigInteger

fun allCombinationsOf(num: List<Int>): Set<List<Int>> {
    return allCombinationsOf(num, arrayListOf())
}

fun allCombinationsOf(num: IntRange): Set<List<Int>> {
    return allCombinationsOf(num.toList(), arrayListOf())
}

private fun allCombinationsOf(num: List<Int>, builtList: ArrayList<Int>): Set<List<Int>> {
    val result = hashSetOf<List<Int>>()
    for (i in num.indices) {
        val value = num[i]
        if (!builtList.contains(value)) {
            val newList = ArrayList(builtList)
            newList.add(value)
            if (newList.size == num.size) {
                result.add(newList)
            } else {
                result.addAll(allCombinationsOf(num, newList))
            }
        }
    }
    return result
}

fun BigInteger.max(other: BigInteger): BigInteger {
    return if (this >= other) this else other
}

class RotatingNumber {

    val from: BigInteger
    val to: BigInteger
    val value: BigInteger
    val diff: BigInteger

    constructor(from: BigInteger, to: BigInteger, value: BigInteger = from) {
        this.from = from
        this.to = to
        this.value = value
        this.diff = to - from + BigInteger.ONE
    }

    constructor(from: Int, to: Int, value: Int = from) {
        this.from = from.toBigInteger()
        this.to = to.toBigInteger()
        this.value = value.toBigInteger()
        this.diff = (to - from + 1).toBigInteger()
    }

    fun reverse(): RotatingNumber {
        val diffToTail = to - value
        return RotatingNumber(from, to, from + diffToTail)
    }

    operator fun inc(): RotatingNumber {
        val newValue = if (value + BigInteger.ONE > to) from else value + BigInteger.ONE
        return RotatingNumber(from, to, newValue)
    }

    operator fun plus(numberToAdd: Int): RotatingNumber {
        return plus(numberToAdd.toBigInteger())
    }

    operator fun plus(numberToAdd: BigInteger): RotatingNumber {
        var newValue = value + numberToAdd
        while (newValue > to) {
            newValue -= diff
        }
        while (newValue < from) {
            newValue += diff
        }
        return RotatingNumber(from, to, newValue)
    }

    operator fun dec(): RotatingNumber {
        val newValue = if (value - BigInteger.ONE < from) to else value - BigInteger.ONE
        return RotatingNumber(from, to, newValue)
    }

}

class MinValue() {
    private var value: BigInteger? = null

    fun next(next: Int): Boolean {
        return next(next.toBigInteger())
    }

    fun next(next: Long): Boolean {
        return next(next.toBigInteger())
    }

    fun next(next: BigInteger): Boolean {
        if (value == null || value!! > next) {
            value = next
            return true
        }
        return false
    }

    fun hasValue(): Boolean {
        return value != null
    }

    fun get(): BigInteger {
        return value!!
    }
}

class MaxValue() {
    private var value: BigInteger? = null
    private var related: Any? = null

    fun next(next: Int, related: Any? = null) {
        next(next.toBigInteger(), related)
    }

    fun next(next: Long, related: Any? = null) {
        next(next.toBigInteger(), related)
    }

    fun next(next: BigInteger, related: Any? = null) {
        if (value == null || value!! < next) {
            value = next
            this.related = related
        }
    }

    fun hasValue(): Boolean {
        return value != null
    }

    fun get(): BigInteger {
        return value!!
    }

    fun getRelated(): Any {
        return related!!
    }
}
