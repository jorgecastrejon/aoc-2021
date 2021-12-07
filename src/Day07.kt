import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int =
        input.first().split(",").map(String::toInt)
            .let { positions -> positions to positions.median() }
            .let { (positions, median) ->  positions.fold(0) { acc, position -> acc + abs(position - median) }  }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
