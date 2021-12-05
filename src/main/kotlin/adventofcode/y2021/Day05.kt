import adventofcode.*

fun main() {
    val day = Day05()
    println(day.part1())
    day.reset()
    println(day.part2())
}

class Day05 : Y2021Day(5) {
    private val input = fetchInput()

    private val points: List<Pair<Coordinate, Coordinate>> = input.map {
        val a = it.split("->")
        val res1= a[0].split(",")
        val res2 = a[1].split(",")
        Pair(
            Coordinate(res1[0].parseNumbersToSingleInt(), res1[1].parseNumbersToSingleInt()),
                    Coordinate(res2[0].parseNumbersToSingleInt(), res2[1].parseNumbersToSingleInt())
        )
    }

    private val minMax = points.flatMap { it.toList() }.getMinMaxValues()

    override fun part1(): Number? {
        val lines = arrayListOf<LineSegment>()
        points.forEach {
            if (it.first.x == it.second.x || it.first.y == it.second.y) {
                lines.add(LineSegment(it.first, it.second))
            }
        }

        val g = Grid(minMax.maxX, minMax.maxY, '.')

        g.forEach { p ->
            lines.forEach { l ->
                if (p.posX == l.from.x.toInt() || p.posY == l.from.y.toInt()) {
                    if (l.pointIsOnLine(p.coordinate())) {
                        if (p.content.isDigit()) {
                            p.content = (p.content.toIntValue() + 1).digitToChar()
                        } else {
                            p.content = '1'
                        }
                    }
                }
            }
        }

        return g.count {
            it.content.isDigit() && it.content.toIntValue() > 1
        }
    }

    override fun part2(): Number? {
        val lines = arrayListOf<LineSegment>()
        val diag =  arrayListOf<LineSegment>()
        points.forEach {
            if (it.first.x == it.second.x || it.first.y == it.second.y) {
                lines.add(LineSegment(it.first, it.second))
            } else {
                diag.add(LineSegment(it.first, it.second))
            }
        }

        val g = Grid(minMax.maxX, minMax.maxY, '.')
        var c = 0

        g.forEach { p ->
            if (c++ % 10000 == 0) println(c)
            lines.forEach { l ->
                if (p.posX == l.from.x.toInt() || p.posY == l.from.y.toInt()) {
                    if (l.pointIsOnLine(p.coordinate())) {
                        if (p.content.isDigit()) {
                            p.content = (p.content.toIntValue() + 1).digitToChar()
                        } else {
                            p.content = '1'
                        }
                    }
                }
            }
            diag.forEach {
                if (it.pointIsOnLine(p.coordinate())) {
                    if (p.content.isDigit()) {
                        p.content = (p.content.toIntValue() + 1).digitToChar()
                    } else {
                        p.content = '1'
                    }
                }
            }
        }

        g.print()

        return g.count {
            it.content.isDigit() && it.content.toIntValue() > 1
        }
    }

}
