import java.util.*

fun main() {

    fun part1(input: List<String>): Int {
        val distances = parserInput(input).dijkstra(0 to 0)

        return distances.getValue(input.lastIndex to input.first().lastIndex)
    }

    fun part2(input: List<String>): Int {
        val matrix = parserInput(input, scaleFactor = 5)
        val distances = matrix.dijkstra(0 to 0)

        return distances.getValue(matrix.lastIndex to matrix.first().lastIndex)
    }

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}

private fun Array<IntArray>.dijkstra(from: Node): Map<Node, Int> {
    val minDistanceQueue = PriorityQueue<DistanceTo> { d1, d2 -> d1.value.compareTo(d2.value) }
    val visitedNodes = mutableSetOf<Node>()
    val distances: MutableMap<Node, Int> = hashMapOf(from to Int.MAX_VALUE)

    fun includeNode(node: Node, distance: Int) {
        if (node !in visitedNodes && distance < distances.getOrDefault(node, Int.MAX_VALUE)) {
            distances[node] = distance
            minDistanceQueue.add(node to distance)
        }
    }

    includeNode(from, distance = 0)

    while (minDistanceQueue.isNotEmpty()) {
        val (node, distance) = minDistanceQueue.remove()
        visitedNodes.add(node)
        val previousDistance = distances[node]

        if (previousDistance == null || previousDistance >= distance) {
            val includeNeighbour = { neighbour: Node ->
                includeNode(neighbour, (previousDistance ?: 0) + this[neighbour.first][neighbour.second])
            }
            rightOf(node)?.let { includeNeighbour(it) }
            aboveOf(node)?.let { includeNeighbour(it) }
            leftOf(node)?.let { includeNeighbour(it) }
            belowOf(node)?.let { includeNeighbour(it) }
        }
    }

    return distances
}

private fun Array<IntArray>.aboveOf(node: Node): Node? =
    getOrNull(node.first - 1)?.get(node.second)?.let { (node.first - 1) to node.second }

private fun Array<IntArray>.rightOf(node: Node): Node? =
    get(node.first).getOrNull(node.second + 1)?.let { node.first to (node.second + 1) }

private fun Array<IntArray>.belowOf(node: Node): Node? =
    getOrNull(node.first + 1)?.get(node.second)?.let { (node.first + 1) to node.second }

private fun Array<IntArray>.leftOf(node: Node): Node? =
    get(node.first).getOrNull(node.second - 1)?.let { node.first to (node.second - 1) }

private fun parserInput(input: List<String>, scaleFactor: Int = 1): Array<IntArray> =
    Array(input.size * scaleFactor) { row ->
        IntArray(input.first().length * scaleFactor) { column ->
            val initialRowInput = row % input.size
            val rowScale = row / input.size
            val initialColumnInput = column % input.first().length
            val columScale = column / input.first().length
            val original = input[initialRowInput][initialColumnInput].intValue
            val scaledNumber = original + 1 * rowScale + 1 * columScale

            (scaledNumber % 10) + scaledNumber / 10
        }
    }

private typealias Node = Pair<Int, Int>
private typealias DistanceTo = Pair<Node, Int>

private val DistanceTo.value get() = second