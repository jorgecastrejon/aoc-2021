import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int =
        input.groupBy({ it.split(" ")[0] }, { it.split(" ")[1].toInt() })
            .let { it["forward"].orEmpty().sum() * abs(it["down"].orEmpty().sum() - it["up"].orEmpty().sum()) }

    fun part2(input: List<String>): Int =
        input.map { it.split(" ").let { it[0] to it[1] } }
            .fold(Triple(0, 0, 0)) { acc, (direction, value) ->
                when (direction) {
                    "forward" -> {
                        acc.copy(first = acc.first + value.toInt(), second = acc.second + acc.third * value.toInt())
                    }
                    "down" -> {
                        acc.copy(third = acc.third + value.toInt())
                    }
                    "up" -> {
                        acc.copy(third = acc.third - value.toInt())
                    }
                    else -> error("no other option could fit here")
                }
            }.let { (hPosition, depth, _) -> hPosition * depth }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}