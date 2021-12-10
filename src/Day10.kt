import java.util.*

fun main() {

    fun part1(input: List<String>): Long =
        input.map(::isValid)
            .filterNot(Result::success)
            .mapNotNull(Result::wrongChar)
            .fold(0) { acc, char -> acc + char.wrongCharScore }

    fun part2(input: List<String>): Long =
        input.asSequence()
            .map(::isValid)
            .filterNot(Result::success)
            .map(Result::missingChars)
            .filterNot(List<Char>::isEmpty)
            .map { chars -> chars.reversed().fold(0L) { acc, char -> 5 * acc + char.missingCharScore } }
            .sorted()
            .toList()
            .let { scores -> scores[scores.size / 2] }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

fun isValid(chunk: String): Result {
    val queue: Deque<Char> = ArrayDeque()

    for (char in chunk) {
        when {
            char == '(' -> queue.add(')')
            char == '[' -> queue.add(']')
            char == '{' -> queue.add('}')
            char == '<' -> queue.add('>')
            queue.isEmpty() -> return Triple(false, null, emptyList())
            queue.removeLast() != char -> return Triple(false, char, emptyList())
        }
    }

    return Triple(queue.isEmpty(), null, queue.toList())
}

typealias Result = Triple<Boolean, Char?, List<Char>>

private val Result.success: Boolean
    get() = first
private val Result.wrongChar: Char?
    get() = second
private val Result.missingChars: List<Char>
    get() = third

private val Char.wrongCharScore: Long
    get() = when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> error("no other option could reach here")
    }

private val Char.missingCharScore: Long
    get() = when (this) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> error("no other option could reach here")
    }
