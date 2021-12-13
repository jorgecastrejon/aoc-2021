fun main() {

    fun part1(input: List<String>): Int =
        parsePaper(input).foldWithInstruction(parseInstructions(input).first()).count()

    fun part2(input: List<String>): Int {
        val paper = parseInstructions(input).fold(parsePaper(input)) { paper, instruction ->
            paper.foldWithInstruction(instruction)
        }
        printPaper(paper)

        return paper.count()
    }

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

private fun Paper.foldWithInstruction(instruction: FoldInstruction): Paper =
    map { point -> fold(point, instruction) }
        .filter {
            if (instruction.axis == "y") {
                it.py < instruction.value
            } else {
                it.px < instruction.value
            }
        }
        .distinct()

private fun fold(point: PaperPoint, instructions: FoldInstruction): PaperPoint =
    when {
        instructions.axis == "x" && point.px > instructions.value -> {
            (instructions.value - (point.px - instructions.value)) to point.py
        }
        instructions.axis == "y" && point.py > instructions.value -> {
            point.px to (instructions.value - (point.py - instructions.value))
        }
        else -> point
    }

private fun printPaper(originalPaper: Paper) {
    val paper = originalPaper.toSet()

    for (y in paper.minOf { point -> point.py }..paper.maxOf { point -> point.py }) {
        println("")
        for (x in paper.minOf { point -> point.px }..paper.maxOf { point -> point.px }) {
            if (x to y in paper) print("#") else print(" ")
        }
    }
    println("")
}

private fun parsePaper(input: List<String>): Paper {
    val count = input.indexOf("")

    return input.take(count).map { point -> point.split(",").let { (x, y) -> x.toInt() to y.toInt() } }
}

private fun parseInstructions(input: List<String>): List<FoldInstruction> {
    val count = input.indexOf("")

    return input.takeLast(input.size - (count + 1)).map { instruction ->
        instruction.split(" ")
            .let { (_, _, value) -> value.split("=").let { (axis, number) -> axis to number.toInt() } }
    }
}

typealias Paper = List<PaperPoint>
typealias PaperPoint = Pair<Int, Int>

val PaperPoint.px get() = first
val PaperPoint.py get() = second
typealias FoldInstruction = Pair<String, Int>

val FoldInstruction.axis get() = first
val FoldInstruction.value get() = second