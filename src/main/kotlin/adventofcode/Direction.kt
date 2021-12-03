package adventofcode

enum class Direction {
    RIGHT, DOWN, LEFT, UP;

    fun turnLeft(): Direction {
        val next = RotatingNumber(0, 3, this.ordinal).dec()
        return values()[next.value.toInt()]
    }

    fun turnRight(): Direction {
        val next = RotatingNumber(0, 3, this.ordinal).inc()
        return values()[next.value.toInt()]
    }

    fun coordinateForDirection(spacesToMove: Int): Coordinate {
        return when (this) {
            RIGHT -> Coordinate(spacesToMove, 0)
            UP -> Coordinate(0, -spacesToMove)
            LEFT -> Coordinate(-spacesToMove, 0)
            DOWN -> Coordinate(0, spacesToMove)
        }
    }

    fun toArrow(): Char {
        return when (this) {
            UP -> '^'
            LEFT -> '<'
            RIGHT -> '>'
            DOWN -> 'v'
        }
    }
}

fun directionFromArrow(arrow: Char): Direction {
    return when (arrow) {
        '^' -> Direction.UP
        '<' -> Direction.LEFT
        '>' -> Direction.RIGHT
        'v' -> Direction.DOWN
        else -> throw Exception("Unknown ${arrow}")
    }
}

fun directionFromLetter(arrow: Char): Direction {
    return when (arrow) {
        'N' -> Direction.UP
        'W' -> Direction.LEFT
        'E' -> Direction.RIGHT
        'S' -> Direction.DOWN
        else -> throw Exception("Unknown ${arrow}")
    }
}
