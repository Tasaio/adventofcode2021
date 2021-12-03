package adventofcode

import java.math.BigInteger

class Coordinate {

    companion object {
        val ORIGIN = Coordinate(0, 0)
    }

    var x: BigInteger
    var y: BigInteger

    constructor() {
        this.x = BigInteger.ZERO
        this.y = BigInteger.ZERO
    }

    constructor(x: BigInteger, y: BigInteger) {
        this.x = x
        this.y = y
    }

    constructor(x: Int, y: Int) {
        this.x = x.toBigInteger()
        this.y = y.toBigInteger()
    }

    constructor(x: Long, y: Long) {
        this.x = x.toBigInteger()
        this.y = y.toBigInteger()
    }

    fun move(x: Int, y: Int): Coordinate {
        return move(x.toBigInteger(), y.toBigInteger())
    }

    fun move(x: BigInteger, y: BigInteger): Coordinate {
        return Coordinate(this.x + x, this.y + y)
    }

    fun moveBy(c: Coordinate): Coordinate {
        return Coordinate(x + c.x, y + c.y)
    }

    fun difference(other: Coordinate): Coordinate {
        return Coordinate(x - other.x, y - other.y)
    }

    fun rotateLeft(times: Int): Coordinate {
        var newX = this.x
        var newY = this.y
        for (i in 0 until times) {
            newX = -y
            newY = x
        }
        return Coordinate(newX, newY)
    }

    fun rotateRight(times: Int) {
        for (i in 0 until times) {
            val newX = y
            val newY = -x
            this.x = newX
            this.y = newY
        }
    }

    fun clone(): Coordinate {
        return Coordinate(x, y)
    }

    fun distance(other: Coordinate): BigInteger {
        return ((other.y - this.y).pow(2) + (other.x - this.x).pow(2)).sqrt()
    }

    fun manhattanDistanceTo(other: Coordinate): BigInteger {
        return (x - other.x).abs() + (y - other.y).abs()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String {
        return "Coordinate(x=$x, y=$y)"
    }

}

data class CoordinateWithDimensions(val dim: Int, val values: List<BigInteger>? = null) {
    val pos: Array<BigInteger?> = arrayOfNulls(dim)

    init {
        if (values != null) {
            for (i in values.indices) {
                pos[i] = values[i]
            }
        }
    }

    fun set(dim: Int, value: BigInteger) {
        pos[dim] = value
    }

    fun manhattanDistanceTo(c: CoordinateWithDimensions): BigInteger {
        if (dim != c.dim) throw Exception("Different dim count")
        var sum = BigInteger.ZERO
        for (i in 0 until dim) {
            sum += (pos[i]!! - c.pos[i]!!).abs()
        }
        return sum
    }
}

data class MinMaxXY(val minX: BigInteger, val maxX: BigInteger, val minY: BigInteger, val maxY: BigInteger) {
    fun sizeX(): BigInteger {
        return maxX - minX + BigInteger.ONE
    }

    fun sizeY(): BigInteger {
        return maxY - minY + BigInteger.ONE
    }

    fun isWithin(c: Coordinate): Boolean {
        return c.x in minX..maxX && c.y in minY..maxY
    }
}

fun Iterable<Coordinate>.getMinMaxValues(): MinMaxXY {
    val minX = MinValue()
    val maxX = MaxValue()
    val minY = MinValue()
    val maxY = MaxValue()
    forEach {
        minX.next(it.x)
        maxX.next(it.x)
        minY.next(it.y)
        maxY.next(it.y)
    }
    return MinMaxXY(minX.get(), maxX.get(), minY.get(), maxY.get())
}
