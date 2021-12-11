fun main() {

    fun part1(input: List<String>): Int {
        val days = 1..100
        val octopuses: MutableList<MutableList<Int>> = input.map {
            it.map(Character::getNumericValue).toMutableList()
        }.toMutableList()
        val lastFlashPerOctopus: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        var flashes = 0

        for (day in days) {
            for (row in octopuses.indices) {
                for (column in octopuses.first().indices) {
                    flashes +=  passDayOctopus(row = row, column = column, day, octopuses, lastFlashPerOctopus)
                }
            }
        }

        return flashes
    }

    fun part2(input: List<String>): Int {
        val octopuses: MutableList<MutableList<Int>> = input.map {
            it.map(Character::getNumericValue).toMutableList()
        }.toMutableList()
        val lastFlashPerOctopus: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        val octopusCount = octopuses.sumOf { it.count() }
        var day = 0
        var perfectDay = -1

        while (perfectDay == -1) {
            day++
            for (row in octopuses.indices) {
                for (column in octopuses.first().indices) {
                    passDayOctopus(row = row, column = column, day, octopuses, lastFlashPerOctopus)
                }
            }
            if (lastFlashPerOctopus.count() == octopusCount && lastFlashPerOctopus.all { (_, today) -> day == today }) {
                perfectDay = day
            }
        }

        return perfectDay
    }

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

fun passDayOctopus(
    row: Int,
    column: Int,
    day: Int,
    octopuses: MutableList<MutableList<Int>>,
    lastFlashPerOctopus: MutableMap<Pair<Int, Int>, Int>,
): Int {
    val current = octopuses.getOrNull(row)?.getOrNull(column)

    if (current == null || (current == 0 && lastFlashPerOctopus[row to column] == day)) {
        return 0
    }
    octopuses[row][column] += 1

    return if (octopuses[row][column] > 9) {
        octopuses[row][column] = 0
        lastFlashPerOctopus[row to column] = day

        (passDayOctopus(row = row, column = column - 1, day, octopuses, lastFlashPerOctopus)
                + passDayOctopus(row = row - 1, column = column - 1, day, octopuses, lastFlashPerOctopus)
                + passDayOctopus(row = row - 1, column = column, day, octopuses, lastFlashPerOctopus)
                + passDayOctopus(row = row - 1, column = column + 1, day, octopuses, lastFlashPerOctopus)
                + passDayOctopus(row = row, column = column + 1, day, octopuses, lastFlashPerOctopus)
                + passDayOctopus(row = row + 1, column = column + 1, day, octopuses, lastFlashPerOctopus)
                + passDayOctopus(row = row + 1, column = column, day, octopuses, lastFlashPerOctopus)
                + passDayOctopus(row = row + 1, column = column - 1, day, octopuses, lastFlashPerOctopus)
                ) + 1
    } else {
        0
    }
}