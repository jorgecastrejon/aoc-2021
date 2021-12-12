fun main() {

    fun part1(input: List<String>): Int =
        findPaths(
            routes = createRoutes(input),
            start = "start",
            end = "end",
            externalPolicy = { nodes, node -> nodes.countOf(node) > 0 }
        ).orEmpty().count()

    fun part2(input: List<String>): Int =
        findPaths(
            routes = createRoutes(input),
            start = "start",
            end = "end",
            externalPolicy = { nodes, node ->
                nodes.countOf("start") > 1 || (nodes.countOf(node) > 0 && nodes.hasDuplicated())
            }
        ).orEmpty().count()

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private fun createRoutes(paths: List<String>): Map<String, List<String>> {
    val routes = mutableMapOf<String, MutableList<String>>()

    paths.forEach { path ->
        val (origin, destination) = path.split("-")
        routes.getOrPut(origin) { mutableListOf() }.apply { add(destination) }
        routes.getOrPut(destination) { mutableListOf() }.apply { add(origin) }
    }

    return routes
}

private fun findPaths(
    routes: Map<String, List<String>>,
    start: String,
    end: String,
    visitedNodes: MutableMap<String, Int> = mutableMapOf(),
    externalPolicy: (MutableMap<String, Int>, String) -> Boolean = { _, _ -> true }
): List<String>? {

    if (start == end) {
        return listOf(end)
    }

    if (externalPolicy(visitedNodes, start)) {
        return null
    }

    if (start.first().isLowerCase()) {
        visitedNodes[start] = visitedNodes.getOrDefault(start, 0) + 1
    }

    val paths = routes[start]?.mapNotNull { node ->
        findPaths(routes, node, end, visitedNodes, externalPolicy)?.map { "$start,$it" }
    }?.flatten()

    visitedNodes[start]?.let { visitedNodes[start] = it - 1 }

    return paths
}

private fun Map<String, Int>.countOf(key: String): Int =
    getOrDefault(key, 0)

private fun Map<String, Int>.hasDuplicated(): Boolean =
    (values.maxOrNull() ?: 0) > 1
