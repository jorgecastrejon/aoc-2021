import kotlin.math.abs
import kotlin.math.max

fun main() {

    fun part1(input: List<String>): Int {
        val targetArea = createTargetArea(input.first())
        var maxY = 0

        for (x in 0..targetArea.xRange.last) {
            for (y in targetArea.yRange.first..abs(targetArea.yRange.first)) {
                val (height, succeed) = fire(x to y, targetArea)

                if (succeed) {
                    maxY = max(height, maxY)
                }
            }
        }

        return maxY
    }

    fun part2(input: List<String>): Int {
        val targetArea = createTargetArea(input.first())
        var count = 0

        for (x in 0..targetArea.xRange.last) {
            for (y in targetArea.yRange.first..abs(targetArea.yRange.first)) {
                val (_, succeed) = fire(x to y, targetArea)

                if (succeed) {
                    count++
                }
            }
        }

        return count
    }

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}

fun fire(startVelocity: Pair<Int, Int>, targetArea: TargetArea): Pair<Int, Boolean> {
    var collision = false
    var position = 0 to 0
    var velocity = startVelocity
    var maxY = 0

    while ((velocity.x > 1 && position.x in targetArea.xRange) || (position.y >= targetArea.yRange.first)) {

        if (position.x in targetArea.xRange && position.y in targetArea.yRange) {
            collision = true
            break
        }

        position = position.x + velocity.x to position.y + velocity.y
        maxY = maxOf(position.y, maxY)
        val vX = if (velocity.x > 0) velocity.x - 1 else (velocity.x + 1).coerceAtMost(0)
        velocity = vX to velocity.y - 1
    }

    return maxY to collision
}

private fun createTargetArea(input: String): TargetArea {
    val (xRange, yRange) = input.removePrefix("target area: ").split(", ")
    val (xMin, xMax) = xRange.removePrefix("x=").split("..")
    val (yMin, yMax) = yRange.removePrefix("y=").split("..")

    return (xMin.toInt()..xMax.toInt()) to (yMin.toInt()..yMax.toInt())
}

private typealias TargetArea = Pair<IntRange, IntRange>

private val TargetArea.xRange get() = first
private val TargetArea.yRange get() = second
