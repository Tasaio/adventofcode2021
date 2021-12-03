package adventofcode

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun <T> List<T>.rotateToElement(element: T) {
    Collections.rotate(this, -this.indexOf(element))
}

fun <T> Collection<T>.powerset(): Set<Set<T>> = when {
    isEmpty() -> setOf(setOf())
    else -> drop(1).powerset().let { it + it.map { it + first() } }
}

fun <T> List<T>.copyOfListWithNewElement(elementToAdd: T): ArrayList<T> {
    val newList = ArrayList(this)
    newList.add(elementToAdd)
    return newList
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
