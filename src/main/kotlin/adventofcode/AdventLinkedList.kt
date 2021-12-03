package adventofcode

import java.math.BigInteger

internal class ListNodeIterator(val list: AdventLinkedList, val head: ListNode): Iterator<ListNode> {

    var cur: ListNode? = null
    var next: ListNode? = null

    init {
        cur = null
        next = head
    }

    override fun hasNext(): Boolean {
        return next !== null && list.tail !== cur
    }

    override fun next(): ListNode {
        cur = next!!
        next = cur!!.next
        return cur!!
    }
}

class ListNode(var value: BigInteger) {
    var next: ListNode? = null
    var prev: ListNode? = null

    fun moveForwardBy(num: Int): ListNode {
        var cur = this
        for (i in 0 until num) cur = cur.next!!
        return cur
    }
}

interface AdventLinkedList: Iterable<ListNode> {
    var head: ListNode?
    var tail: ListNode?

    // O(1)
    fun addBefore(node: ListNode, newNode: ListNode)

    // O(1)
    fun addAfter(node: ListNode, newNode: ListNode)

    // O(1)
    fun remove(node: ListNode)

    // O(N)
    fun size(): BigInteger

    // O(N)
    fun forEach(action: (ListNode) -> Unit)

    // O(N)
    fun find(value: BigInteger): ListNode?
}

open class DoubleLinkedList(override var head: ListNode? = null, override var tail: ListNode? = null) : AdventLinkedList {

    protected var size = BigInteger.ZERO

    open fun addFirst(value: Int) {
        addFirst(value.toBigInteger())
    }

    open fun addFirst(value: BigInteger) {
        addFirst(ListNode(value))
    }

    open fun addFirst(newNode: ListNode) {
        size++
        if (head != null) {
            newNode.prev = null
            newNode.next = head

            head!!.prev = newNode
            head = newNode
        } else {
            newNode.next = null
            newNode.prev = null

            head = newNode
            tail = newNode
        }
    }

    open fun addFirstMultiple(newNodes: Iterable<ListNode>) {
        val reversed = newNodes.reversed()
        reversed.forEach { addFirst(it) }
    }

    open fun addLast(value: Int) {
        addLast(value.toBigInteger())
    }

    open fun addLast(value: Long) {
        addLast(value.toBigInteger())
    }

    open fun addLast(value: BigInteger) {
        addLast(ListNode(value))
    }

    open fun get(index: Int): ListNode? {
        return get(index.toBigInteger())
    }


    open fun get(index: BigInteger): ListNode? {
        var i = BigInteger.ZERO
        forEach { if (i++ == index) return it }
        return null
    }

    open fun indexOf(searchFor: ListNode): BigInteger {
        var i = BigInteger.ZERO
        forEach {
            if (it == searchFor) return i
            i++
        }
        return BigInteger.valueOf(-1)
    }

    open fun addLast(newNode: ListNode) {
        size++
        if (tail != null) {
            newNode.prev = tail
            newNode.next = null

            tail!!.next = newNode
            tail = newNode
        } else {
            newNode.next = null
            newNode.prev = null

            head = newNode
            tail = newNode
        }
    }

    open fun addLastMultipleValues(newValues: Iterable<BigInteger>) {
        newValues.forEach { addLast(it) }
    }

    open fun addLastMultiple(newNodes: Iterable<ListNode>) {
        newNodes.forEach { addLast(it) }
    }

    open fun reverse() {
        var temp: ListNode? = null
        var current: ListNode? = head

        val oldHead = head
        val oldTail = tail

        while (current != null) {
            temp = current.prev
            current.prev = current.next
            current.next = temp
            if (current === tail) {
                break
            }
            current = current.prev
        }

        head = oldTail
        tail = oldHead
    }

    open fun pop(): ListNode? {
        val head = head
        if (head != null) {
            remove(head)
            return head
        }
        return null
    }

    open fun popMultiple(count: Int): DoubleLinkedList {
        val newList = DoubleLinkedList()
        for (i in 1..count) {
            val newNode = pop()
            if (newNode != null) {
                newList.addLast(newNode)
            }
        }
        return newList
    }

    open fun popLast(): ListNode? {
        val tail = tail
        if (tail != null) {
            remove(tail)
            return tail
        }
        return null
    }

    open fun popLastMultiple(count: Int): DoubleLinkedList {
        val newList = DoubleLinkedList()
        for (i in 1..count) {
            val newNode = popLast()
            if (newNode != null) {
                newList.addFirst(newNode)
            }
        }
        return newList
    }

    override fun remove(node: ListNode) {
        size--
        val prevNode = node.prev
        val nextNode = node.next

        if (prevNode != null) {
            prevNode.next = nextNode
        }
        if (nextNode != null) {
            nextNode.prev = prevNode
        }

        if (node === head) {
            head = nextNode
        }
        if (node === tail) {
            tail = prevNode
        }

        node.next = null
        node.prev = null
    }

    override fun size(): BigInteger {
        return size
    }

    override fun forEach(action: (ListNode) -> Unit) {
        for (node in this) {
            action.invoke(node)
        }
    }

    override fun iterator(): Iterator<ListNode> {
        return ListNodeIterator(this, head!!)
    }

    override fun addBefore(node: ListNode, newNode: ListNode) {
        if (node === head) {
            addFirst(newNode)
        } else {
            size++
            val prevNode = node.prev
            newNode.next = node
            newNode.prev = prevNode

            if (prevNode != null) {
                prevNode.next = newNode
            }
            node.prev = newNode
        }
    }

    fun addAfter(node: ListNode, newNode: Int) {
        addAfter(node, ListNode(newNode.toBigInteger()))
    }

    override fun addAfter(node: ListNode, newNode: ListNode) {
        if (node === tail) {
            addLast(newNode)
        } else {
            size++
            val nextNode = node.next
            newNode.next = nextNode
            newNode.prev = node

            if (nextNode != null) {
                nextNode.prev = newNode
            }
            node.next = newNode
        }
    }

    override fun find(value: BigInteger): ListNode? {
        for (node in this) {
            if (node.value == value) {
                return node
            }
        }
        return null
    }
}

class CircularLinkedList: DoubleLinkedList() {

    override fun addFirst(newNode: ListNode) {
        head = addToCircle(newNode)
    }

    override fun addLast(newNode: ListNode) {
        tail = addToCircle(newNode)
    }

    override fun get(index: BigInteger): ListNode? {
        val i = if (index >= size()) index % size() else index
        return super.get(i)
    }

    private fun addToCircle(newNode: ListNode): ListNode {
        size++
        if (head != null) {
            newNode.prev = tail
            newNode.next = head

            head!!.prev = newNode
            tail!!.next = newNode
        } else {
            newNode.next = newNode
            newNode.prev = newNode

            head = newNode
            tail = newNode
        }
        return newNode
    }

    fun forEachCircular(action: (ListNode) -> Unit) {
        var current = head
        var count = size()
        while (current !== null) {
            val next = current.next
            action.invoke(current)
            if (count-- == BigInteger.ZERO) {
                current = null
            } else {
                current = next
            }
        }
    }
}
