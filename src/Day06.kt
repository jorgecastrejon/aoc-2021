fun main() {

    fun part1(input: List<String>): Int {
        var lanternFishes: List<Int> = input.first().split(",").map(String::toInt)

        for (i in 0 until 80) {
            val new = lanternFishes.count { it == 0 }
            lanternFishes = lanternFishes.map { if (it == 0) 6 else it - 1 } + (0 until new).map { 8 }
        }

        return lanternFishes.count()
    }

    fun part2(input: List<String>): Long {
        val lanternFishes: MutableMap<Int, Long> = (0..8).associateWith { 0L }.toMutableMap()

        input.first().split(",").forEach {
            lanternFishes[it.toInt()] = lanternFishes.getOrDefault(it.toInt(), 0) + 1
        }

        for (i in 0 until 256) {
            val new = lanternFishes.getOrDefault(0, 0)
            (0..8).forEach { day -> lanternFishes[day] = lanternFishes[day + 1] ?: 0 }
            lanternFishes[6] = (lanternFishes[6] ?: 0) + new
            lanternFishes[8] = new
        }

        return lanternFishes.values.sum()
    }

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
