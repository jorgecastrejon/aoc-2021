fun main() {

    fun part1(input: List<String>): Int {
        val bits = input.first().map(Char::toBinary).joinToString("")
        val (packet, _) = processPacket(bits)

        return packet.combinedVersion()
    }

    fun part2(input: List<String>): Long {
        val bits = input.first().map(Char::toBinary).joinToString("")
        val (packet, _) = processPacket(bits)

        return packet.calculateNumber()
    }

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

private fun processPacket(packet: String): Pair<Packet, String> {
    val version = packet.substring(0, 3)
    val type = packet.substring(3, 6)

    when {
        type.toDecimal() == 4 -> {
            var start = 6
            val sb = StringBuilder()

            while (packet[start] != '0') {
                sb.append(packet.substring(start + 1, start + 5))
                start += 5
            }
            sb.append(packet.substring(start + 1, start + 5))
            start += 5

            return Packet.Literal(version, type, sb.toString()) to packet.substring(start)
        }
        packet[6] == '0' -> {
            val bits = packet.substring(7, 22).toDecimal()
            var packetsBits = packet.substring(22, 22 + bits)
            val subPackets = mutableListOf<Packet>()

            while (packetsBits.isNotBlank()) {
                val (child, rest) = processPacket(packetsBits)
                subPackets.add(child)
                packetsBits = rest
            }

            return Packet.Operator(version, type, subPackets) to packet.substring(22 + bits)
        }
        else -> {
            val packetCount = packet.substring(7, 18).toDecimal()
            var packetsBits = packet.substring(18)
            val subPackets = mutableListOf<Packet>()
            var count = 0

            while (count++ < packetCount) {
                val (child, rest) = processPacket(packetsBits)
                subPackets.add(child)
                packetsBits = rest
            }

            return Packet.Operator(version, type, subPackets) to packetsBits
        }
    }
}

private sealed class Packet {
    abstract val version: String
    abstract val type: String

    abstract fun combinedVersion(): Int
    abstract fun calculateNumber(): Long

    data class Literal(override val version: String, override val type: String, val number: String) : Packet() {
        override fun combinedVersion(): Int =
            version.toDecimal()

        override fun calculateNumber(): Long =
            number.toLongDecimal()
    }

    data class Operator(override val version: String, override val type: String, val subPackets: List<Packet>) :
        Packet() {
        override fun combinedVersion(): Int =
            version.toDecimal() + subPackets.sumOf { it.combinedVersion() }

        override fun calculateNumber(): Long = when (type.toDecimal()) {
            0 -> subPackets.sumOf { it.calculateNumber() }
            1 -> subPackets.fold(1) { acc, packet -> acc * packet.calculateNumber() }
            2 -> subPackets.minOf { it.calculateNumber() }
            3 -> subPackets.maxOf { it.calculateNumber() }
            5 -> if (subPackets[0].calculateNumber() > subPackets[1].calculateNumber()) 1 else 0
            6 -> if (subPackets[0].calculateNumber() < subPackets[1].calculateNumber()) 1 else 0
            7 -> if (subPackets[0].calculateNumber() == subPackets[1].calculateNumber()) 1 else 0
            else -> error("no other option could fit here")
        }
    }
}

