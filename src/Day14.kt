fun main() {

    fun part1(input: List<String>): Long {
        val polymerTemplate = input.first().windowed(2).groupBy { it }.mapValues { (_, list) -> list.count().toLong() }
        val rules = parseInsertionRules(input.drop(2))

        val polymer = (0 until 10).fold(polymerTemplate) { polymer, _ -> processPolymer(polymer, rules) }
        val elementCount = countElements(polymer, polymerTemplate.keys.last().last())

        return elementCount.sorted().let { it.last() - it.first() }
    }


    fun part2(input: List<String>): Long {
        val polymerTemplate = input.first().windowed(2).groupBy { it }.mapValues { (_, list) -> list.count().toLong() }
        val rules = parseInsertionRules(input.drop(2))

        val polymer = (0 until 40).fold(polymerTemplate) { polymer, _ -> processPolymer(polymer, rules) }
        val elementCount = countElements(polymer, polymerTemplate.keys.last().last())

        return elementCount.sorted().let { it.last() - it.first() }
    }

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

fun processPolymer(polymer: Map<String, Long>, rules: Map<String, String>): Map<String, Long> {
    val map = mutableMapOf<String, Long>()

    polymer.forEach { (match, count) ->
        val firstPart = "${match.first()}${rules[match]}"
        val secondPart = "${rules[match]}${match.last()}"
        map[firstPart] = map.getOrDefault(firstPart, 0L) + count
        map[secondPart] = map.getOrDefault(secondPart, 0L) + count
    }

    return map
}

fun countElements(polymer: Map<String, Long>, lastItem: Char): Collection<Long> {
    val map = mutableMapOf<Char, Long>()

    polymer.forEach { (letters, count) ->
        val current = map.getOrDefault(letters.first(), 0L)
        map[letters.first()] = count + current
    }
    map[lastItem] = map.getOrDefault(lastItem, 0) + 1

    return map.values
}

fun parseInsertionRules(input: List<String>): Map<String, String> =
    input.associate { rule ->
        val (match, result) = rule.split(" -> ")
        match to result
    }
