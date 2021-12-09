fun main() {

    fun part1(input: List<String>): Int {
        val heightMap = input.map { xLine -> xLine.map(Character::getNumericValue) }
        val lowPoints = mutableListOf<Int>()

        for (row in heightMap.indices) {
            for (column in heightMap.first().indices) {
                val point = heightMap[row][column]
                val left = heightMap[row].getOrNull(column - 1) ?: (point + 1)
                val right = heightMap[row].getOrNull(column + 1) ?: (point + 1)
                val top = heightMap.getOrNull(row - 1)?.get(column) ?: (point + 1)
                val bottom = heightMap.getOrNull(row + 1)?.get(column) ?: (point + 1)

                if (point < minOf(left, right, top, bottom)) {
                    lowPoints.add(point)
                }
            }
        }

        return lowPoints.sumOf { height -> height + 1 }
    }

    fun part2(input: List<String>): Int {
        val heightMap = input.map { xLine -> xLine.map(Character::getNumericValue) }
        val crossedPoints = mutableSetOf<Pair<Int, Int>>()
        val basins: MutableList<MutableList<Int>> = mutableListOf()

        for (row in heightMap.indices) {
            for (column in heightMap.first().indices) {

                val group = basins.getOrElse(basins.size) { mutableListOf() }
                evaluate(row, column, heightMap, group, crossedPoints)

                if (group.isNotEmpty()) {
                    basins.add(group)
                }
            }
        }

        return basins.map { it.count() }.sortedDescending().take(3).reduce { acc, i -> acc * i }
    }

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

fun evaluate(
    row: Int,
    column: Int,
    heightMap: List<List<Int>>,
    group: MutableList<Int>,
    crossedPoints: MutableSet<Pair<Int, Int>>
) {
    val height = heightMap.getOrNull(row)?.getOrNull(column)

    if (height == null || height == 9 || crossedPoints.contains(row to column)) {
        return
    } else {
        crossedPoints.add(row to column)
        group.add(height)
        evaluate(row, column - 1, heightMap, group, crossedPoints)
        evaluate(row - 1, column, heightMap, group, crossedPoints)
        evaluate(row, column + 1, heightMap, group, crossedPoints)
        evaluate(row + 1, column, heightMap, group, crossedPoints)
    }
}
