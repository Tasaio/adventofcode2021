package adventofcode

import java.math.BigInteger
import java.util.*
import java.util.stream.IntStream
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.streams.toList

fun <T> List<T>.rotateToElement(element: T) {
    Collections.rotate(this, -this.indexOf(element))
}

fun List<String>.applyToEachColumn(function: (List<Char>) -> Char): String {
    return IntStream.range(0, this[0].length)
        .mapToObj { pos ->
            this.map { it[pos] }.toList()
        }
        .map { function.invoke(it) }
        .toList()
        .joinToString(separator = "") { it.toString() }
}

fun <T> Collection<T>.powerset(): Set<Set<T>> = when {
    isEmpty() -> setOf(setOf())
    else -> drop(1).powerset().let { it + it.map { it + first() } }
}

fun <T> Set<T>.allPermutations(): Set<List<T>> {
    fun <T> internalAllPermutations(list: List<T>): Set<List<T>> {
        if (list.isEmpty()) return setOf(emptyList())

        val result: MutableSet<List<T>> = mutableSetOf()
        for (i in list.indices) {
            internalAllPermutations(list - list[i]).forEach{ item -> result.add(item + list[i]) }
        }
        return result
    }

    return internalAllPermutations(toList())
}

fun <T> List<T>.copyOfListWithNewElement(elementToAdd: T): ArrayList<T> {
    val newList = ArrayList(this)
    newList.add(elementToAdd)
    return newList
}

fun <T> List<T>.maxValueOfTwoElements(extract: (T, T) -> BigInteger): BigInteger {
    val max = MaxValue()
    indices.forEach { a ->
        indices.filter { it != a }.forEach { b ->
            max.next(extract.invoke(get(a), get(b)))
        }
    }
    return max.get()
}

fun <T> List<T>.minValueOfTwoElements(extract: (T, T) -> BigInteger): BigInteger {
    val min = MinValue()
    indices.forEach { a ->
        indices.filter { it != a }.forEach { b ->
            min.next(extract.invoke(get(a), get(b)))
        }
    }
    return min.get()
}

fun <T> List<T>.applyToEach(element: T) {
    Collections.rotate(this, -this.indexOf(element))
}

class HashMapSet<A, B>: HashMap<A, MutableSet<B>>() {

    fun add(key: A, value: B): Boolean {
        if (!containsKey(key)) {
            super.put(key, HashSet())
        }
        return super.get(key)!!.add(value)
    }

    fun containsSetValue(value: B): Boolean {
        return values.flatten().contains(value)
    }
}


class HashMapList<A, B>: HashMap<A, MutableList<B>>() {

    fun add(key: A, value: B): Boolean {
        if (!containsKey(key)) {
            super.put(key, ArrayList())
        }
        return super.get(key)!!.add(value)
    }

    fun containsListValue(value: B): Boolean {
        return values.flatten().contains(value)
    }
}
