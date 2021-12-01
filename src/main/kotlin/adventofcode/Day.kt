package adventofcode;

import java.io.File
import java.io.InputStream
import java.math.BigInteger
import java.net.URL
import java.nio.charset.Charset
import java.nio.file.Files

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}

abstract class Day(val year: Int, val day: Int) {

    var sum: BigInteger = BigInteger.ZERO
    var counter: Long = 0L

    fun reset() {
        counter = 0L
        sum = BigInteger.ZERO
    }

    abstract fun part1(): Number?

    abstract fun part2(): Number?

    open fun fetchFromRemote(): String {
        val session = this.javaClass.getResource("session.conf").readText().trim()
        val url = "https://adventofcode.com/${year}/day/${day}/input"
        return URL(url).openConnection().apply {
            readTimeout = 800
            connectTimeout = 200
            setRequestProperty(
                "Cookie",
                "session=${session}"
            )
        }
            .getInputStream().use { it.readTextAndClose() }
    }

    open fun fetchInput(): List<String> {
        val f = File("src/main/resources/input/y${year}/${year}-12-${day.toString().padStart(2, '0')}.txt")
        if (!f.exists()) {
            Files.createDirectories(f.toPath().parent)
            f.writeText(fetchFromRemote())
        }
        val list: List<String> = f.readLines()
        if (list.last().isEmpty()) {
            return list.dropLast(1)
        }
        return list
    }

    fun fetchInputAsBigInt(): List<BigInteger> {
        return fetchInput().map { it.toBigInteger() }
    }

    fun fetchInputAsSingleLineBigInteger(separator: String = ","): MutableList<BigInteger> {
        return ArrayList(fetchInput()[0].split(separator).map { it.toBigInteger() })
    }

    fun fetchInputAsListMap(delimiter: String = ":"): List<Map<String, String>> {
        val lines = fetchInput()
        val listMap = arrayListOf<Map<String, String>>()
        var map = hashMapOf<String, String>()
        lines.forEach {
            if (it.isBlank()) {
                if (map.isNotEmpty()) {
                    listMap.add(map)
                    map = hashMapOf()
                }
            } else {
                it.split(" ").forEach {
                    val keyValue = it.split(delimiter)
                    if (map.containsKey(keyValue.get(0))) throw RuntimeException("Duplicate key " + keyValue.get(0))
                    map.put(keyValue.get(0), keyValue.get(1))
                }
            }
        }
        if (map.isNotEmpty()) {
            listMap.add(map)
        }
        return listMap
    }

    fun fetchInputAsListOfList(delimiter: String): List<List<BigInteger>> {
        return fetchInput().map { it -> it.split(delimiter).map { it.toBigInteger() } }
    }

}
