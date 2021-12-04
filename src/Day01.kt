fun main() {
    fun part1(input: List<Int>): Int =
        input.zipWithNext().count { (prev , next) -> prev < next }

    fun part2(input: List<Int>): Int =
        input.windowed(3).zipWithNext().count { (prev , next) -> prev.sum() < next.sum() }

    val input = readInputAsInt("Day01")
    println(part1(input))
    println(part2(input))
}
