package adventofcode

import java.lang.UnsupportedOperationException
import java.math.BigInteger

fun Pair<String, String>.toCoordinate(): Coordinate {
    return Coordinate(first.toBigInteger(), second.toBigInteger())
}

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

    constructor(str: String, split: String = ",") {
        val s = str.split(split)
        this.x = s[0].toBigInteger()
        this.y = s[1].toBigInteger()
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

    val rotations2d: List<List<Int>> =
        arrayListOf(
            arrayListOf(1, 1), arrayListOf(1, -1),
            arrayListOf(-1, 1), arrayListOf(-1, -1)
        );

    val rotations3d: List<List<Int>> =
        arrayListOf(
            arrayListOf(1, 1, 1), arrayListOf(1, 1, -1),
            arrayListOf(1, -1, 1), arrayListOf(-1, 1, 1),
            arrayListOf(-1, -1, 1), arrayListOf(-1, 1, -1),
            arrayListOf(1, -1, -1), arrayListOf(-1, -1, -1)
        );

    init {
        if (values != null) {
            for (i in values.indices) {
                pos[i] = values[i]
            }
        }
    }

    fun allRotations(): List<CoordinateWithDimensions> {
        if (dim > 3) throw UnsupportedOperationException()

        var rotations = if (dim == 2) rotations2d else rotations3d

        val res = arrayListOf<CoordinateWithDimensions>()
        for (rotation in rotations) {
            val new = arrayListOf<BigInteger>()
            for (d in 0 until dim) {
                new.add(pos[d]!! * rotation[d].toBigInteger())
            }
            res.add(CoordinateWithDimensions(dim, new))
        }
        return res
    }

    operator fun plus(c: CoordinateWithDimensions): CoordinateWithDimensions {
        val new = arrayListOf<BigInteger>()
        for (d in 0 until dim) {
            new.add(pos[d]!! + c.pos[d]!!)
        }
        return CoordinateWithDimensions(dim, new)
    }

    operator fun minus(c: CoordinateWithDimensions): CoordinateWithDimensions {
        val new = arrayListOf<BigInteger>()
        for (d in 0 until dim) {
            new.add(pos[d]!! - c.pos[d]!!)
        }
        return CoordinateWithDimensions(dim, new)
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

    fun allRotationsAndDirections(): List<CoordinateWithDimensions> {
        if (dim > 3) throw UnsupportedOperationException()

        val res = arrayListOf<CoordinateWithDimensions>()

        data class ABC(val a: BigInteger, val b: BigInteger, val c: BigInteger)

        fun add2dim(a: (ABC) -> BigInteger, b: (ABC) -> BigInteger) {
            val abc = ABC(pos[0]!!, pos[1]!!, BigInteger.ZERO)
            res.add(CoordinateWithDimensions(dim, arrayListOf(a.invoke(abc), b.invoke(abc))))
        }

        fun add3dim(a: (ABC) -> BigInteger, b: (ABC) -> BigInteger, c: (ABC) -> BigInteger) {
            val abc = ABC(pos[0]!!, pos[1]!!, pos[2]!!)
            res.add(CoordinateWithDimensions(dim, arrayListOf(a.invoke(abc), b.invoke(abc), c.invoke(abc))))
        }

        if (dim == 2) {
            add2dim({ it.a }, { it.b })
            add2dim({ it.a }, { -it.b })
            add2dim({ -it.a }, { it.b })
            add2dim({ -it.a }, { -it.b })

            add2dim({ it.b }, { it.a })
            add2dim({ it.b }, { -it.a })
            add2dim({ -it.b }, { it.a })
            add2dim({ -it.b }, { -it.a })
        } else if (dim == 3) {
            CoordinateWithDimensions(3, arrayListOf(pos[0]!!, pos[1]!!, pos[2]!!))
            add3dim({ it.a }, { it.b }, { it.c })
            add3dim({ it.b }, { it.c }, { it.a })
            add3dim({ it.c }, { it.a }, { it.b })
            add3dim({ it.c }, { it.b }, { -it.a })
            add3dim({ it.b }, { it.a }, { -it.c })
            add3dim({ it.a }, { it.c }, { -it.b })

            add3dim({ it.a }, { -it.b }, { -it.c })
            add3dim({ it.b }, { -it.c }, { -it.a })
            add3dim({ it.c }, { -it.a }, { -it.b })
            add3dim({ it.c }, { -it.b }, { it.a })
            add3dim({ it.b }, { -it.a }, { it.c })
            add3dim({ it.a }, { -it.c }, { it.b })

            add3dim({ -it.a }, { it.b }, { -it.c })
            add3dim({ -it.b }, { it.c }, { -it.a })
            add3dim({ -it.c }, { it.a }, { -it.b })
            add3dim({ -it.c }, { it.b }, { it.a })
            add3dim({ -it.b }, { it.a }, { it.c })
            add3dim({ -it.a }, { it.c }, { it.b })

            add3dim({ -it.a }, { -it.b }, { it.c })
            add3dim({ -it.b }, { -it.c }, { it.a })
            add3dim({ -it.c }, { -it.a }, { it.b })
            add3dim({ -it.c }, { -it.b }, { -it.a })
            add3dim({ -it.b }, { -it.a }, { -it.c })
            add3dim({ -it.a }, { -it.c }, { -it.b })
        }

        return res
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoordinateWithDimensions

        if (dim != other.dim) return false
        if (values != other.values) return false
        if (!pos.contentEquals(other.pos)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dim
        result = 31 * result + (values?.hashCode() ?: 0)
        result = 31 * result + pos.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "$values"
    }
}

fun Iterable<CoordinateWithDimensions>.allPermutations(): List<List<CoordinateWithDimensions>> {
    val res = arrayListOf<List<CoordinateWithDimensions>>()

    val allLists = map { it.allRotationsAndDirections() }

    for (i in 0 until allLists[0].size) {
        val newList = arrayListOf<CoordinateWithDimensions>()
        allLists.forEach {
            newList.add(it[i])
        }
        res.add(newList)
    }

    return res
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
