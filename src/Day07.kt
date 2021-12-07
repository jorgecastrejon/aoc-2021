import kotlin.math.abs
import kotlin.math.min

fun main() {

    fun part1(input: List<String>): Int =
        input.first().split(",").map(String::toInt)
            .let { positions -> positions to positions.median() }
            .let { (positions, median) -> positions.fold(0) { acc, position -> acc + abs(position - median) } }

    fun part2(input: List<String>): Int =
        input.first().split(",").map(String::toInt)
            .let { positions -> positions to positions.meanAsInts() }
            .let { (positions, means) ->
                min(
                    positions.fold(0) { acc, position -> acc + abs(position - means.first()).triangularNumber() },
                    positions.fold(0) { acc, position -> acc + abs(position - means.last()).triangularNumber() }
                )
            }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
