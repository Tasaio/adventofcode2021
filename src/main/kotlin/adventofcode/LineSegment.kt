package adventofcode

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.atan2

class LineSegment(var from: Coordinate, var to: Coordinate) {

    fun intersects(other: LineSegment): Coordinate? {
        // Line AB represented as a1x + b1y = c1
        val a1 = this.to.y - this.from.y
        val b1 = this.from.x - this.to.x
        val c1 = a1 * (this.from.x) + b1 * (this.from.y)

        // Line CD represented as a2x + b2y = c2
        val a2 = other.to.y - other.from.y
        val b2 = other.from.x - other.to.x
        val c2 = a2 * (other.from.x) + b2 * (other.from.y)

        val determinant = a1 * b2 - a2 * b1;

        if (determinant == BigInteger.ZERO) {
            // The lines are parallel
            return null
        } else {
            val x = (b2 * c1 - b1 * c2).toBigDecimal() / determinant.toBigDecimal()
            val y = (a1 * c2 - a2 * c1).toBigDecimal() / determinant.toBigDecimal()

            try {
                val c = Coordinate(x.toBigIntegerExact(), y.toBigIntegerExact())
                if (pointIsOnLine(c) && other.pointIsOnLine(c)) {
                    return c
                }
            } catch (e: ArithmeticException) {
                return null
            }
        }
        return null
    }

    fun angle(): Double {
        val dy = (to.y - from.y).toDouble()
        val dx = (to.x - from.x).toDouble()
        return Math.toDegrees(atan2(dy, dx))
    }

    fun length(): BigInteger {
        return from.distance(to)
    }

    fun pointIsOnLine(point: Coordinate): Boolean {
        val dxc = point.x - from.x;
        val dyc = point.y - from.y;

        val dxl = to.x - from.x;
        val dyl = to.y - from.y;

        val cross = dxc * dyl - dyc * dxl;

        if (cross != BigInteger.ZERO) {
            return false
        }

        if (dxl.abs() >= dyl.abs()) {
            return if (dxl > BigInteger.ZERO) from.x <= point.x && point.x <= to.x
            else to.x <= point.x && point.x <= from.x;
        } else
            return if (dyl > BigInteger.ZERO) from.y <= point.y && point.y <= to.y
            else to.y <= point.y && point.y <= from.y;
    }

}