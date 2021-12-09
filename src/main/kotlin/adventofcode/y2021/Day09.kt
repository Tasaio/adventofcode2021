import adventofcode.GridNode

fun main() {
    val testInput = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent()
    val test = Day09(testInput)
    println("TEST: " + test.part1())
    println("TEST: " + test.part2())

    val day = Day09()

    val t1 = System.currentTimeMillis()
    println("${day.part1()}   (${System.currentTimeMillis() - t1}ms)")
    day.reset()

    val t2 = System.currentTimeMillis()
    println("${day.part2()}   (${System.currentTimeMillis() - t2}ms)")
}

open class Day09(staticInput: String? = null) : Y2021Day(9, staticInput) {
    private val grid = fetchInputAsGrid()

    override fun reset() {
        super.reset()
    }

    override fun part1(): Number? {
        return grid.filter { it.content < it.getNeighbors().minOf { it.content } }.sumOf { it.contentAsNum() + 1 }
    }

    override fun part2(): Number? {
        val lowPoints = grid.filter { node ->
            node.contentAsNum() < node.getNeighbors().minOf { it.contentAsNum() }
        }

        return lowPoints.map {
            val seen = hashSetOf<GridNode>()
            val nodes = hashSetOf<GridNode>(it)
            var found = false
            do {
                found = false
                nodes
                    .filter { node -> !seen.contains(node) }
                    .forEach { node ->
                        seen.add(node)
                        node.getNeighbors()
                            .filter { it.content.digitToInt() != 9 }
                            .filter { it.content.digitToInt() > node.content.digitToInt() }
                            .forEach {
                                if (nodes.add(it)) {
                                    found = true
                                }
                            }
                    }
            } while (found)
            nodes.size
        }.sortedByDescending { it }.take(3).reduce { a, b -> a * b }
    }
}
