package adventofcode

import java.math.BigInteger

class RepeatingList(val repeatSize: Int = 100, val skipFirst: Int? = null) {
    private var list: MutableList<BigInteger> = arrayListOf<BigInteger>()
    private val diffList = arrayListOf<BigInteger>()

    val repeat = arrayListOf<BigInteger>()
    private var skipped: Int = 0

    fun add(num: Int) {
        add(num.toBigInteger())
    }

    fun add(num: BigInteger) {
        if (skipFirst != null && skipped < skipFirst) {
            skipped++
            return
        }
        if (repeat.isNotEmpty()) return
        if (list.size > 1) {
            val diff = num - list.last()
            diffList.add(diff)
        }
        list.add(num)
    }

    fun isRepeat(): Boolean {
        if (repeat.isNotEmpty()) return true
        if (list.size >= repeatSize * 2) {
            val searchFor = list.takeLast(repeatSize)
            outer@for (i in 0 until list.size - repeatSize) {
                for (j in 0 until repeatSize) {
                    if (list[i + j] != searchFor[j]) continue@outer
                }
                repeat.addAll(minimizeRepeatList(searchFor))
                list = list.subList(0, i)
                return true
            }
        }
        return false
    }

    fun minimizeRepeatList(repeat: List<BigInteger>): List<BigInteger> {
        outer@for (i in 1 until repeatSize) {
            val listToTest = repeat.take(i)
            for (j in i until repeatSize) {
                if (listToTest[j % i] != repeat[j]) continue@outer
            }
            return listToTest
        }
        println("Could not minimize repeat list, this is probably wrong...")
        return repeat
    }

    fun getNumInSeries(num: BigInteger): BigInteger {
        if (!isRepeat() && num.toLong() > list.size) {
            throw Exception("Cant")
        }
        val numToUse = num - (skipFirst?.toBigInteger() ?: BigInteger.ZERO)
        if (numToUse <= BigInteger.ZERO) {
            throw Exception("Invalid numToUse")
        }
        if (numToUse.toLong() <= list.size) {
            return list[(num.toLong() - 1).toInt()]
        }
        val numInRepeat = ((numToUse - list.size.toBigInteger()) % repeat.size.toBigInteger()).toInt()
        return repeat[numInRepeat]
    }
}

