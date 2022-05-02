package sorting
import java.util.Scanner

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    if (args.isNotEmpty() && args[0] == "-dataType") {
        when (args[1]) {
            "long" -> {
                val list = mutableListOf<Int>()
                while (scanner.hasNext()) {
                    list.add(scanner.nextInt())
                }
                handleLong(list)
            }
            "word" -> {
                val list = mutableListOf<String>()
                while (scanner.hasNext()) {
                    list.add(scanner.next())
                }
                handleWord(list)
            }
            "line" -> {
                val list = mutableListOf<String>()
                while (scanner.hasNext()) {
                    list.add(scanner.nextLine())
                }
                handleLine(list)
            }
        }
    } else if (args.isEmpty() || args[0] != "-dataType") {
        val list = mutableListOf<String>()
        while (scanner.hasNext()) {
            list.add(scanner.next())
        }
        handleWord(list)
    }
}

/**
 * handleLong - when the parameter to the program is Long this method gets called
 * The method calculates the total size of user's input, it identifies the highest integer and number
 * of occurrence, and it calculates the percentage relative to other numbers and prints them out to console
 * @return - Unit
 * @author - Royalboe
 */
fun handleLong(list: MutableList<Int>) {
    val total = list.size
    val max = list.maxOrNull() ?: 0
    val maxFreq = list.count{ it == max}
    val percentage = (maxFreq * 100.0 / total).toInt()
    printToConsole(total, max, maxFreq, percentage, "number", "greatest")
}

/**
 * handleLine - when the parameter to the program is Line this method gets called
 * The method calculates the total size of user's input, it identifies the longest line and number
 * of occurrence, and it calculates the percentage relative to other lines and prints them out to console
 * @return - Unit
 * @author - Royalboe
 */
fun handleLine(list: MutableList<String>) {
    val total = list.size
    list.sortBy { it.length }
    val max = list.last()
    val maxFreq = list.count{ it == max}
    val percentage = (maxFreq * 100.0 / total).toInt()
    printLineToConsole(total, max, maxFreq, percentage)
}

/**
 * handleWord - when the parameter to the program is Word this method gets called
 * The method calculates the total size of user's input, it identifies the longest word and number
 * of occurrence, and it calculates the percentage relative to other words and prints them out to console
 * @return - Unit
 * @author - Royalboe
 */
fun handleWord(list: MutableList<String>) {
    val total = list.size
    list.sortBy { it.length }
    val max = list.last()
    val maxFreq = list.count { it == max }

    val percentage = (maxFreq * 100.0 / total).toInt()
    printToConsole(total, max, maxFreq, percentage, "word", "longest")
}

/**
 * printToConsole - A function that prints to the user the total, max, frequency of max
 * and percentage of the frequency to the console
 * @param - total is the total size of user input
 * @param - max is for the biggest figure or longest word/line
 * @param - maxFreq This is the number of times
 * @param - percentage of maxFreq relative to total.
 * @param - datatype
 * @param - qualifier
 * @return - Unit
 */
fun printToConsole(total: Int, max: Any, maxFreq: Int, percentages: Int, datatype: String, qualifier: String) {
    println("Total ${datatype}s: $total.")
    println("The $qualifier $datatype: $max ($maxFreq time(s), $percentages%).")
}

/**
 * printToConsole - A function that prints to the user the total, max, frequency of max
 * and percentage of the frequency to the console
 * @param - total is the total size of user input
 * @param - max is for the biggest figure or longest word/line
 * @param - maxFreq This is the number of times
 * @param - percentage of maxFreq relative to total.
 * @return - Unit
 */
fun printLineToConsole(total: Int, max: Any, maxFreq: Int, percentages: Int) {
    println("Total lines: $total.")
    println("The longest line:\n$max\n($maxFreq time(s), $percentages%).")
}