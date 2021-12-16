import adventofcode.*
import java.math.BigInteger
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    val testInput = """
        C200B40A82
    """.trimIndent()
    runDay(
        day = Day16::class,
        testInput = testInput,
        testAnswer1 = 14,
        testAnswer2 = 3
    )
}

open class Day16(staticInput: String? = null) : Y2021Day(16, staticInput) {
    private val input = fetchInput()

    data class Packet(val version: Int, val typeId: Int, val subPackets: List<Packet>, val value: BigInteger)

    val packet: Packet

    init {
        fun hexToString(s: Char): String {
            return when (s) {
                '0' -> "0000"
                '1' -> "0001"
                '2' -> "0010"
                '3' -> "0011"
                '4' -> "0100"
                '5' -> "0101"
                '6' -> "0110"
                '7' -> "0111"
                '8' -> "1000"
                '9' -> "1001"
                'A' -> "1010"
                'B' -> "1011"
                'C' -> "1100"
                'D' -> "1101"
                'E' -> "1110"
                'F' -> "1111"
                else -> throw RuntimeException()
            }
        }

        val translated = input[0].map { hexToString(it) }.joinToString(separator = "") { it }
        packet = readPacket(translated, AtomicInteger(0))
    }

    fun readPacket(s: String, i: AtomicInteger): Packet {
        val V = s.substring(i.get(), i.addAndGet(3)).binaryStringToInt()
        val T = s.substring(i.get(), i.addAndGet(3)).binaryStringToInt()

        if (T == 4) {
            var num = ""
            while (true) {
                val nextNum = s.substring(i.get(), i.addAndGet(5))
                num += nextNum.substring(1, 5)

                if (nextNum[0] == '0') {
                    break
                }
            }

            return Packet(V, T, emptyList(), num.binaryStringToBigInteger())
        } else {
            val lengthType = s.substring(i.get(), i.addAndGet(1))
            val bits = if (lengthType == "1") 11 else 15

            val subPacketLength = s.substring(i.get(), i.addAndGet(bits)).binaryStringToInt()

            val subPackets = arrayListOf<Packet>()
            val subPacketString = s.substring(i.get(), i.get() + subPacketLength)
            if (lengthType == "0") {
                val subPacketI = AtomicInteger(0)
                while (subPacketI.get() < subPacketLength) {
                    subPackets.add(readPacket(subPacketString, subPacketI))
                }
                i.addAndGet(subPacketI.get())
            } else if (lengthType == "1") {
                for (i_ in 0 until subPacketLength) {
                    subPackets.add(readPacket(s, i))
                }
            }

            return Packet(V, T, subPackets, BigInteger.ZERO)
        }
    }

    override fun reset() {
        super.reset()
    }

    override fun part1(): Number? {
        val left = LinkedList<Packet>()
        left.add(packet)
        while (left.isNotEmpty()) {
            val p = left.pop()
            left.addAll(p.subPackets)
            sum += p.version.toBigInteger()
        }
        return sum
    }

    override fun part2(): Number? {
        return packetValue(packet)
    }

    fun packetValue(p: Packet): BigInteger {
        return when (p.typeId) {
            0 -> p.subPackets.sumOf { packetValue(it) }
            1 -> p.subPackets.map { packetValue(it) }.reduce { a, b -> a * b }
            2 -> {
                val m = MinValue()
                p.subPackets.map { packetValue(it) }.forEach { m.next(it) }
                m.get()
            }
            3 -> {
                val m = MaxValue()
                p.subPackets.map { packetValue(it) }.forEach { m.next(it) }
                m.get()
            }
            4 -> {
                p.value
            }
            5 -> {
                val a = packetValue(p.subPackets[0])
                val b = packetValue(p.subPackets[1])
                if (a > b) {
                    BigInteger.ONE
                } else {
                    BigInteger.ZERO
                }
            }
            6 -> {
                val a = packetValue(p.subPackets[0])
                val b = packetValue(p.subPackets[1])
                if (a < b) {
                    BigInteger.ONE
                } else {
                    BigInteger.ZERO
                }
            }
            7 -> {
                val a = packetValue(p.subPackets[0])
                val b = packetValue(p.subPackets[1])
                if (a == b) {
                    BigInteger.ONE
                } else {
                    BigInteger.ZERO
                }
            }
            else -> throw java.lang.RuntimeException()
        }
    }
}
