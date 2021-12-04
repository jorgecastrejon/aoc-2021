fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.first().split(",").map(String::toInt)
        val boards = input.drop(2).windowed(5, 6).map { Board.fromRawData(it) }

        var lastNumber = -1
        var board: Board? = null

        for (number in numbers) {
            lastNumber = number
            boards.forEach { it.markNumber(number) }

            board = boards.firstOrNull { it.hasBingo() }

            if (board != null) {
                break
            }
        }
        requireNotNull(board) { "there should be at least a winner board" }

        return board.allNumbers.filterNot(Board.Number::selected).sumOf(Board.Number::value) * lastNumber
    }

    fun part2(input: List<String>): Int  {
        val numbers = input.first().split(",").map(String::toInt)
        val boards = input.drop(2).windowed(5, 6).map { Board.fromRawData(it) }

        var lastNumber = -1
        val winnerBoards: MutableList<Board> = mutableListOf()

        for (number in numbers) {
            lastNumber = number
            boards.forEach { it.markNumber(number) }

            boards.filter { it !in winnerBoards && it.hasBingo() }
                .forEach { winnerBoards.add(it) }

            if (winnerBoards.size == boards.size) {
                break
            }
        }

        return winnerBoards.last().allNumbers.filterNot(Board.Number::selected).sumOf(Board.Number::value) * lastNumber
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

class Board private constructor(private val rows: List<List<Number>>) {

    companion object {
        fun fromRawData(data: List<String>): Board =
            Board(data.map { row -> row.split(" ").filter { it.isNotBlank() }.map { Number(it.toInt()) } })
    }

    val allNumbers: List<Number>
        get() = rows.flatten()

    fun markNumber(number: Int) {
        rows.forEach { row ->
            row.forEach { element ->
                if (element.value == number) {
                    element.selected = true
                }
            }
        }
    }

    fun hasBingo(): Boolean =
        rowBingo() || columnBingo()

    private fun rowBingo(): Boolean =
        rows.any { row -> row.all { it.selected } }

    private fun columnBingo(): Boolean =
        rows.first().indices.any { columnNumber ->
            rows.map { row -> row[columnNumber] }.all { it.selected }
        }

     class Number(val value: Int) {
         var selected: Boolean = false
    }
}