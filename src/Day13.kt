fun main() {

    fun part1(input: List<String>): Int {
        val separator = input.indexOf("")
        val table: List<BoardPoint> = input.take(separator).map { position ->
            position.split(",").let { (x, y) -> x.toInt() to y.toInt() }
        }
        val instructions: List<FoldInstruction> = input.takeLast(input.size - (separator + 1)).map { instruction ->
            instruction.split(" ")
                .let { (_, _, value) -> value.split("=").let { (axis, number) -> axis to number.toInt() } }
        }

        return table.foldWithInstruction(instructions.first()).count()
    }

    fun part2(input: List<String>): Int {
        val separator = input.indexOf("")
        val table: List<BoardPoint> = input.take(separator).map { position ->
            position.split(",").let { (x, y) -> x.toInt() to y.toInt() }
        }
        val instructions: List<FoldInstruction> = input.takeLast(input.size - (separator + 1)).map { instruction ->
            instruction.split(" ")
                .let { (_, _, value) -> value.split("=").let { (axis, number) -> axis to number.toInt() } }
        }
        val foldedPaper = instructions.fold(table) { board, instruction -> board.foldWithInstruction(instruction) }
        printTable(foldedPaper)

        return foldedPaper.count()

    }

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

private fun printTable(table: List<BoardPoint>) {
    val paper = table.toSet()

    for (y in paper.minOf { point -> point.py }..paper.maxOf { point -> point.py }) {
        println("")
        for (x in paper.minOf { point -> point.px }..paper.maxOf { point -> point.px }) {
            if (x to y in paper) {
                print("#")
            } else {
                print(" ")
            }
        }
    }
    println("")
}

private fun List<BoardPoint>.foldWithInstruction(instruction: FoldInstruction): List<BoardPoint> =
    map { point -> fold(point, instruction) }
        .filter {
            if (instruction.axis == "y") {
                it.py < instruction.value
            } else {
                it.px < instruction.value
            }
        }
        .distinct()

private fun fold(point: BoardPoint, instructions: FoldInstruction): BoardPoint {
    return if (instructions.axis == "x") {
        if (point.px > instructions.value) {
            (instructions.value - (point.px - instructions.value)) to point.py
        } else {
            point
        }
    } else {
        if (point.py > instructions.value) {
            point.px to (instructions.value - (point.py - instructions.value))
        } else {
            point
        }
    }
}

typealias BoardPoint = Pair<Int, Int>

val BoardPoint.px get() = first
val BoardPoint.py get() = second
typealias FoldInstruction = Pair<String, Int>

val FoldInstruction.axis get() = first
val FoldInstruction.value get() = second