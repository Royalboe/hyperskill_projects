package sorting
import java.util.Scanner

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    if (args.isNotEmpty() && args.contains("-sortingType")) {
        if (args.contains("natural")) parser(args, scanner, "natural")
         else if (args.contains("byCount")) parser(args, scanner, "byCount")
    } else parser(args, scanner, "natural")
}

private fun parser(args: Array<String>, scanner: Scanner, flag: String) {
    when {
        args.contains("long") -> {
            val list = mutableListOf<Int>()
            while (scanner.hasNext()) {
                list.add(scanner.nextInt())
            }
            handleLong(list, flag)
        }
        args.contains("word") -> {
            val list = mutableListOf<String>()
            while (scanner.hasNext()) {
                list.add(scanner.next())
            }
            handleWord(list, flag)
        }
        args.contains("line") -> {
            val list = mutableListOf<String>()
            while (scanner.hasNext()) {
                list.add(scanner.nextLine())
            }
            handleLine(list, flag)
        }
    }
}

/**
 * handleLong - when the arguments to the program contains long this method gets called
 * The method sorts the users input and prints it to the console
 * @return - Unit
 */
fun handleLong(list: MutableList<Int>, sortBy: String) {
    val newList = list.sorted().toMutableList()
    if (sortBy == "natural") {
        println("Total numbers: ${newList.size}.\nSorted data: ${newList.joinToString(" ")}")
    } else {
        println("Total numbers: ${newList.size}.")
        val sortedByCount = list.sorted().groupingBy { it }.eachCount().toList()
            .sortedBy { (_, value) -> value }.toMap()
        for ((element, count) in sortedByCount) {
            println("$element: $count time(s), ${(count * 100.0 / newList.size).toInt()}%")
        }
    }
}

/**
 * handleLine - when the parameter to the program is Line this method gets called
 * The method calculates the total size of user's input, it identifies the longest line and number
 * of occurrence, and it calculates the percentage relative to other lines and prints them out to console
 * @return - Unit
 */
fun handleLine(list: MutableList<String>, sortBy: String) {
    val newList = list.sorted().toMutableList()
    if (sortBy == "natural") {
        println("Total lines: ${newList.size}.\ndata:")
        for (element in newList) {
            println(element)
        }
    } else sortByCount("lines", newList, list)
}

/**
 * handleWord - when the arguments to the program contains word this method gets called
 * The method sorts the user's input based on count/frequency or in lexicographic order and
 * prints the result to the console
 * @return - Unit
 */
fun handleWord(list: MutableList<String>, sortBy: String) {
    val newList = list.sorted().toMutableList()
    if (sortBy == "natural") {
        println("Total words: ${newList.size}.\nSorted data: ${newList.joinToString(" ")}")
    } else {
        sortByCount("words", newList, list)
    }
}

/**
 * sortByCount - a function to help sort words and lines by count
 * @param - e
 * @param - list : An unsorted list
 * @param - newList: A sorted list
 * @return - Unit

 */
private fun sortByCount(
    e: String,
    newList: MutableList<String>,
    list: MutableList<String>
) {
    println("Total $e: ${newList.size}.")
    val sortedByCount = list.sorted().groupingBy { it }.eachCount().toList()
        .sortedBy { (_, value) -> value }.toMap()
    for ((element, count) in sortedByCount) {
        println("$element: $count time(s), ${(count * 100.0 / newList.size).toInt()}%")
    }
}