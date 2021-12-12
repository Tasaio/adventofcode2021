import adventofcode.*

fun main() {
    val testInput = """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent()
    runDay(
        day = Day12::class,
        testInput = testInput,
        testAnswer1 = 10,
        testAnswer2 = 36
    )
}

open class Day12(staticInput: String? = null) : Y2021Day(12, staticInput) {
    private val input = fetchInput().map { Pair(it.split("-")[0], it.split("-")[1]) }

    override fun reset() {
        super.reset()
    }

    override fun part1(): Number? {
        val options = visitCave(arrayListOf("start"), true)
        if (options.size < 100) println(options)
        return options.size
    }

    fun visitCave(visited: List<String>, visitedSmallCave: Boolean): Set<List<String>> {
        val lists = hashSetOf<List<String>>()
        val cave = visited.last()

        if (cave == "end") {
            return hashSetOf(visited)
        }
        val options = input.filter { cave == it.first }.map { it.second } + input.filter { cave == it.second }.map { it.first }

        options
            .filter { it != "start" }
            .filter { !visited.contains(it) || it.isUpperCase() || (it.isLowerCase() && !visitedSmallCave) }
            .forEach {
                lists.addAll(visitCave(visited.copyOfListWithNewElement(it),
                    visitedSmallCave || (visited.contains(it) && it.isLowerCase())))
            }
        return lists
    }

    override fun part2(): Number? {
        return visitCave(arrayListOf("start"), false).size
    }

}
