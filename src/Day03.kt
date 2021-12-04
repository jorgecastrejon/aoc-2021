import java.util.Comparator

fun main() {
    fun part1(input: List<String>): Int =
        input.fold(mutableMapOf<Int, Int>()) { storage, consumption ->
            consumption.forEachIndexed { index, char ->
                storage[index] = (storage[index] ?: 0) + Character.getNumericValue(char)
            }
            storage
        }.values.fold(Rate()) { rate, value ->
            if (value > (input.size / 2)) {
                rate.gamma.append(1)
                rate.epsilon.append(0)
            } else {
                rate.gamma.append(0)
                rate.epsilon.append(1)
            }
            rate
        }.let { rate -> rate.gammaValue.toDecimal() * rate.epsilonValue.toDecimal() }

    tailrec fun getMostCommonValue(list: List<String>, nDigit: Int = 0): List<String> =
        if (list.size == 1) {
            list
        } else {
            getMostCommonValue(list.groupBy { it[nDigit] }.maxWithOrNull(BitComparator())?.value.orEmpty(), nDigit + 1)
        }

    tailrec fun getLeastCommonValue(list: List<String>, nDigit: Int = 0): List<String> =
        if (list.size == 1) {
            list
        } else {
            getLeastCommonValue(list.groupBy { it[nDigit] }.minWithOrNull(BitComparator())?.value.orEmpty(), nDigit + 1)
        }

    fun part2(input: List<String>): Int =
        getMostCommonValue(input).first().toDecimal() * getLeastCommonValue(input).first().toDecimal()


    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

class BitComparator : Comparator<Map.Entry<Char, List<String>>> {
    override fun compare(o1: Map.Entry<Char, List<String>>, o2: Map.Entry<Char, List<String>>): Int =
        when {
            o1.value.size < o2.value.size -> -1
            o1.value.size > o2.value.size -> 1
            o1.key == '0' -> -1
            else -> 1
        }
}

class Rate(val gamma: StringBuilder = StringBuilder(), val epsilon: StringBuilder = StringBuilder()) {

    val gammaValue: String get() = gamma.toString()
    val epsilonValue: String get() = epsilon.toString()
}
