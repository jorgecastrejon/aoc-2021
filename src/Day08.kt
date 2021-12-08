fun main() {

    fun part1(input: List<String>): Int =
        getSegmentsPerNumber(1, 4, 7, 8).let { segments ->
            input.map { it.split(" | ").last() }
                .flatMap { it.split(" ") }
                .count { it.length in segments }
        }

    fun part2(input: List<String>): Int =
        input.map { line -> line.split(" | ").let { calculateOutput(it.first(), it.last()) } }
            .sumOf { it[0] * 1000 + it[1] * 100 + it[2] * 10 + it[3] }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

//                                 0..1..2..3..4..5..6..7..8..9
val segmentsPerDigits = intArrayOf(6, 2, 5, 5, 4, 5, 6, 3, 7, 6)

fun getSegmentsPerNumber(vararg digits: Int): List<Int> =
    digits.map { segmentsPerDigits[it] }

fun decodeInput(input: List<String>): List<Set<Char>> {
    val seven = input.find { it.length == 3 }.orEmpty()
    val sevenSet = seven.toSet()
    val one = input.find { it.length == 2 }.orEmpty()
    val oneSet = one.toSet()
    val four = input.find { it.length == 4 }.orEmpty()
    val fourSet = four.toSet()
    val eight = input.find { it.length == 7 }.orEmpty()
    val eightSet = eight.toSet()

    val topSegment = sevenSet.minus(oneSet)
    val possibleTopLeftMiddleSegment = fourSet.minus(sevenSet)

    val five = input.find { number ->
        number.length == 5 && (possibleTopLeftMiddleSegment + topSegment).all { number.contains(it) }
    }.orEmpty()
    val fiveSet = five.toSet()

    val topRightSegment = oneSet.minus(fiveSet)
    val bottomRightSegment = oneSet.minus(topRightSegment)

    val three = input.find { number ->
        number.length == 5 && number != five && number.contains(bottomRightSegment.first())
    }.orEmpty()
    val threeSet = three.toSet()

    val two = input.find { number ->
        number.length == 5 && number != five && number != three
    }.orEmpty()
    val twoSet = two.toSet()

    val topLeftSegment = possibleTopLeftMiddleSegment.minus(threeSet)
    val middleSegment = possibleTopLeftMiddleSegment.minus(topLeftSegment)

    val zero = input.find { number ->
        number.length == 6 && !number.contains(middleSegment.first())
    }.orEmpty()
    val zeroSet = zero.toSet()

    val nine = input.find { number ->
        number.length == 6 && number != zero && number.contains(topRightSegment.first())
    }.orEmpty()
    val nineSet = nine.toSet()

    val six = input.find { number ->
        number.length == 6 && number != nine && number != zero
    }.orEmpty()
    val sixSet = six.toSet()

    return listOf(zeroSet, oneSet, twoSet, threeSet, fourSet, fiveSet, sixSet, sevenSet, eightSet, nineSet)
}

fun decodeOutput(output: List<String>, numbers: List<Set<Char>>): List<Int> =
    output.map { number ->
        val numberSet = number.toSet()
        val item = numbers.find { match -> match.size == numberSet.size && numberSet.minus(match).isEmpty() }
        numbers.indexOf(item)
    }

fun calculateOutput(rawInput: String, rawOutput: String): List<Int> {
    val input = rawInput.split(" ")
    val output = rawOutput.split(" ")

    return decodeOutput(output, decodeInput(input))
}


