import adventofcode.*

fun main() {
    val testInput = """
        target area: x=20..30, y=-10..-5
    """.trimIndent()
    runDay(
        day = Day17::class,
        testInput = testInput,
        testAnswer1 = 45,
        testAnswer2 = 112
    )
}

open class Day17(staticInput: String? = null) : Y2021Day(17, staticInput) {
    private val input = fetchInput()

    val target = hashSetOf<Coordinate>()
    val minMax: MinMaxXY

    init {
        val list = input[0].parseNumbersToIntList()
        for (x in list[0]..list[1]) {
            for (y in list[2]..list[3]) {
                target.add(Coordinate(x, y))
            }
        }
        minMax = target.getMinMaxValues()
    }

    override fun part1(): Number? {
        val high = MaxValue()

        for (x in 0..50) {
            for (y in 0..200) {
                var probe = Coordinate.ORIGIN
                var vx = x
                var vy = y
                val max = MaxValue()

                while (true) {
                    probe = probe.move(vx, vy)
                    max.next(probe.y)

                    if (target.contains(probe)) {
                        high.next(max.get())
                        continue
                    } else if (probe.x > minMax.maxX || probe.y < minMax.minY) {
                        break
                    }

                    if (vx > 0) {
                        vx--
                    }
                    vy--
                }
            }
        }

        return high.get()
    }

    override fun part2(): Number? {
        val found = hashSetOf<Coordinate>()

        for (x in 0..minMax.maxX.toInt()) {
            for (y in -minMax.minY.abs().toInt()..minMax.maxY.abs().toInt() * 2) {
                var probe = Coordinate.ORIGIN
                var vx = x
                var vy = y

                while (true) {
                    probe = probe.move(vx, vy)

                    if (target.contains(probe)) {
                        found.add(Coordinate(x, y))
                        break
                    } else if (probe.x > minMax.maxX || probe.y < minMax.minY) {
                        break
                    }

                    if (vx > 0) {
                        vx--
                    }
                    vy--
                }
            }
        }

        return found.size
    }
}
