import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

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
