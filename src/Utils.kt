import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = File("src", "$name.txt").readLines()

fun readInputAsInt(name: String): List<Int> = File("src", "$name.txt").readLines().map(String::toInt)

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun String.toDecimal(): Int = Integer.parseInt(this, 2)

fun List<Int>.median(): Int =
    sorted().let { sortedList ->
        if (sortedList.size % 2 == 0) {
            (sortedList[sortedList.size / 2] + sortedList[(sortedList.size - 1) / 2]) / 2
        } else {
            sortedList[sortedList.size / 2]
        }
    }

fun List<Int>.meanAsInts(): IntArray =
    (sum().toFloat() / size).let { mean -> intArrayOf(floor(mean).toInt(), ceil(mean).toInt()) }

fun Int.triangularNumber(): Int =
    (this downTo 1).fold(0) { acc, number -> acc + number }

val Char.intValue: Int
    get() = Character.getNumericValue(this)
