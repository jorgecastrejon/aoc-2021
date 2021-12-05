import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val board = mutableMapOf<Point, Int>()

        getSegments(input)
            .filterNot(Segment::isDiagonal)
            .forEach { segment ->
                segment.range.forEach { point -> board[point] = board.getOrDefault(point, 0) + 1 }
            }

        return board.values.count { it >= 2 }
    }

    fun part2(input: List<String>): Int {
        val board = mutableMapOf<Point, Int>()

        getSegments(input)
            .filterNot(Segment::isIrregular)
            .forEach { segment ->
                segment.range.forEach { point -> board[point] = board.getOrDefault(point, 0) + 1 }
            }
        return board.values.count { it >= 2 }
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

fun getSegments(input: List<String>): List<Segment> =
    input.map { line -> line.split(" -> ") }.map { point -> point.getSegment() }

fun List<String>.getSegment(): Segment =
    first().getPoint() to last().getPoint()

fun String.getPoint(): Point =
    split(",").let { it[0].toInt() to it[1].toInt() }

typealias Segment = Pair<Point, Point>

val Segment.start: Point get() = first
val Segment.end: Point get() = second
typealias Point = Pair<Int, Int>

val Point.x: Int get() = first
val Point.y: Int get() = second

fun Segment.isDiagonal(): Boolean =
    !isVertical() && !isHorizontal()

fun Segment.isVertical(): Boolean =
    start.x == end.x

fun Segment.isHorizontal(): Boolean =
    start.y == end.y

val Segment.range
    get() = when {
        isVertical() && start.y < end.y -> (start.y..end.y).map { y -> start.x to y }
        isVertical() -> (end.y..start.y).map { y -> start.x to y }
        isHorizontal() && start.x < end.x -> (start.x..end.x).map { x -> x to start.y }
        isHorizontal() -> (end.x..start.x).map { x -> x to start.y }
        is45degreesDiagonal() -> {
            val steps = abs(end.x - start.x)
            val progression = ((end.x - start.x) / steps)  to ((end.y - start.y) / steps)

            (0 .. steps).map { step -> (start.x + step * progression.first ) to (start.y + step * progression.second)  }
        }
        else -> emptyList()
    }

fun Segment.is45degreesDiagonal(): Boolean =
    abs(start.x - end.x) == abs(start.y - end.y)

fun Segment.isIrregular(): Boolean =
    !isVertical() && !isHorizontal() && !is45degreesDiagonal()