package calculator

fun main() {
    val digitsMap = mutableMapOf<String, Int>()
    while (true) {
        val input = readLine()!!
        when {
            input == "" -> continue
            input.contains("/") -> {
                if (commands(input)) {
                    digitsMap.clear()
                    break
                }
            }
            input.isNotEmpty() -> {
                try {
                    compute(input, digitsMap)
                } catch (e: Exception) {
                    println("Invalid expression")
                }
            }
        }
    }
}

private fun commands(line: String): Boolean {
    when (line) {
        "/exit" -> {
            println("Bye!")
            return true
        }
        "/help" -> {
            println("This program is a calculator with addition and subtraction functionalities")
        }
        else -> {
            println("Unknown command")
        }
    }
    return false
}

fun reformatInput(strings: String): List<String> {
    var input = strings
    input = input.replace("---", "-")
    input = input.replace("--", "+")
    input = input.replace("[+]+".toRegex(), "+")
    for (i in listOf("+", "=", "*", "/")) {
        input = input.replace(i, " $i ")
    }
    input = input.replace("\\s+".toRegex(), " ").trim()
    return input.split(" ")
}

private fun compute(line: String, identifiers: MutableMap<String, Int>) {
    val regex = Regex("""\s*""")
    val complex = Regex("[*]|[/]|[)]|[(]")
    val newLine = line.replace(regex, "")
//    val addWords = mutableListOf<String>()
    val invalidRegex = Regex("([\\d]+[+|-]+)")
    when {
        line.contains("=") -> {
            // To assign variables to store values
            assignVariables(newLine, identifiers)
        }
        line.matches(invalidRegex) -> {
            println("Invalid expression")
        }
        !line.contains(" ") -> {
            if (line.matches("[+][\\d]+".toRegex())) {
                println(line.replace("+", ""))
            } else if (line.matches("[a-zA-Z]+".toRegex())) {
                if (identifiers.containsKey(line)) {
                    println(identifiers[line])
                } else if (line.matches("[-][\\d]+".toRegex())) {
                    println(line)
                } else {
                    println("Unknown variable")
                }
            } else println(line)
        }
        line.contains("(") && !line.contains(")")
                || line.contains(")") && !line.contains("(")
                || line.contains("[*]{2,}".toRegex()) -> {
            println("Invalid expression")
        }
        line.contains(complex) && line.contains("[a-zA-Z]+".toRegex()) -> {
            var lines = ""
            for (item in line.replace(" ", "").map { it.toString() }) {
                lines += if (item.matches("[a-zA-Z]+".toRegex())) {
                    "${identifiers[item]!!}"
                } else {
                    item
                }
            }
            println(convertToPostFix(getDigitsMul(lines)))
        }
        line.contains(complex) -> {
            println(convertToPostFix(getDigitsMul(line)))
        }
        line.contains("[-|+]".toRegex()) && line.contains("[a-zA-Z]+".toRegex()) -> {
            var lines = ""
            for (element in line.split(" ")) {
                lines += if (element.matches("[a-zA-Z]+".toRegex())) {
                    "${identifiers[element]!!}"
                } else {
                    element
                }
            }
            //addWords.addAll(getDigits(lines))
//            addWords.removeLast()
            println(convertToPostFix(getDigitsMul(lines)))
        }
        else -> {
            //addWords.addAll(getDigits(line))
            val addNumbers = println(convertToPostFix(getDigitsMul(line)))
            println(addNumbers)
        }
    }
}

fun convertToPostFix(line: String): Int {

    val operators = mutableListOf<String>()
    val operands = mutableListOf<String>()


    for (i in line.replace(" ", "").map { it.toString() }) {
        when {
            i == " " -> continue
            i.matches("[\\d]+".toRegex()) -> {
                operands.add(i)
            }

            i == "(" -> {
                operators.add(i)

            }

            i == ")" -> {
                val range = operators.lastIndexOf("(") until operators.size
                for (num in range) {
                    val temp = operators.last()
                    if (temp != "(") {
                        operands.add(temp)
                    }
                    operators.removeAt(operators.lastIndex)
                }
            }
            i == "+" -> {
                lowerPrecedents(operators, i, operands)
            }
            i == "-" -> {
                lowerPrecedents(operators, i, operands)
            }
            i == "*" -> {
                higherPrecedents(operators, i, operands)
            }
            i == "/" -> {
                higherPrecedents(operators, i, operands)
            }
        }
    }
    for (i in operators) {
        operands.add(i)
    }
    operators.clear()
    return calculate(operands)
}

fun calculate(operands: MutableList<String>): Int {
    val resultList = mutableListOf<Int>()
    var result: Int
    for (item in operands) {
        if (item.matches("[\\d]+".toRegex())) {
            resultList.add(item.toInt())
        } else {
            when (item) {
                "+" -> {
                    result = resultList[resultList.lastIndex - 1] + resultList.last()
                    repeat(2) {
                        resultList.removeLast()
                    }
                    resultList.add(result)
                }
                "-" -> {
                    result = resultList[resultList.lastIndex - 1] - resultList.last()
                    repeat(2) {
                        resultList.removeLast()
                    }
                    resultList.add(result)
                }
                "*" -> {
                    result = resultList[resultList.lastIndex - 1] * resultList.last()
                    repeat(2) {
                        resultList.removeLast()
                    }
                    resultList.add(result)
                }
                "/" -> {
                    result = resultList[resultList.lastIndex - 1] / resultList.last()
                    repeat(2) {
                        resultList.removeLast()
                    }
                    resultList.add(result)
                }
            }
        }
    }
    return resultList[0]
}

private fun higherPrecedents(
    operators: MutableList<String>,
    i: String,
    operands: MutableList<String>
) {
    if (operators.isEmpty() || operators.size == 1 && operators[0] == "("
        || operators.size == 1 && operators[0].matches("[+]|[-]".toRegex())
    ) {
        operators.add(i)
    } else if (operators.isNotEmpty() && operators.size == 1
        && operators[0].matches("[*]|[/]".toRegex())
    ) {
        val temp = operators[0]
        operators.clear()
        operators.add(i)
        operands.add(temp)
    } else if (operators.isNotEmpty() && operators.size > 1
        && operators.last().matches("[/]|[*]".toRegex())
        && !operators.contains("(")
    ) {
        val range1 = 0..operators.lastIndex
        for (num in range1) {
            val temp = operators.last()
            operators.removeLast()
            operands.add(temp)
        }
    } else if (operators.isNotEmpty() && operators.size > 1
        && operators[0] != "("
        && operators.contains("(")
    ) {
        val subList = operators.subList(operators.lastIndexOf("("), operators.lastIndex)
        if (subList.size >= 1
            && subList.last().contains("[*]|[/]".toRegex())
        ) {
            do {
                val temp = operators.last()
                operators.removeLast()
                operands.add(temp)
            } while (operators.last() != "(")
            operators.add(i)
        } else operators.add(i)
    }
}

private fun lowerPrecedents(
    operators: MutableList<String>,
    i: String,
    operands: MutableList<String>
) {
    when {
        operators.isEmpty() || operators.size == 1 && operators[0] == "(" -> {
            operators.add(i)
        }
        operators.isNotEmpty() && operators.size == 1
                && operators[0].matches("[*]|[-]|[/]|[+]".toRegex()) -> {
            val temp = operators[0]
            operators.clear()
            operators.add(i)
            operands.add(temp)
        }
        operators.isNotEmpty() && operators.size > 1
                && operators[0].matches("[+]|[-]".toRegex())
                && !operators.contains("(") -> {
            val range1 = 0..operators.lastIndex
            for (num in range1) {
                val temp = operators.last()
                operators.removeLast()
                operands.add(temp)
            }
        }
        operators.isNotEmpty() && operators.size > 1
                && operators[0] != "("
                && operators.contains("(") -> {
            val subList = operators.subList(operators.lastIndexOf("("), operators.lastIndex)
            if (subList.size >= 1
                && subList[0].matches("[*]|[-]|[/]|[+]".toRegex())
            ) {
                do {
                    val temp = operators.last()
                    operators.removeLast()
                    operands.add(temp)
                } while (operators.last() != "(")
                operators.add(i)
            } else operators.add(i)
        }
    }
}

private fun assignVariables(
    newLine: String,
    identifiers: MutableMap<String, Int>
) {
    val lines = newLine.split("=")
    when {
       /* lines[0].contains("\\d".toRegex()) -> {
            println("Invalid identifier")
        }*/
        lines[1].contains("[\\d]+".toRegex()) && lines[1].contains("[a-zA-Z]+".toRegex())
                || lines.size > 2 -> {
            println("Invalid assignment")
        }
        lines[1].matches("[a-zA-Z]+".toRegex()) -> {
            if (identifiers.isNotEmpty() && identifiers.containsKey(lines[1])) {
                identifiers += lines[0] to identifiers[lines[1]]!!
            } else {
                println("Unknown variable")
            }
        }
        lines[0].matches("[a-zA-Z]+".toRegex()) && lines[1].matches("[\\d]+".toRegex()) -> {
//                identifiers += lines[0] to lines[1].toInt()
            identifiers[lines[0]] = lines[1].toInt()
        }
        lines[0].matches("[a-zA-Z]+".toRegex()) && lines[1].matches("[-][\\d]+".toRegex()) -> {
//                identifiers += lines[0] to lines[1].toInt()
            identifiers[lines[0]] = lines[1].toInt()
        }
        else -> println("Invalid identifier")
    }
}

/*
private fun performComputation(
    addWords: MutableList<String>,
): Int {
    var addNumbers1 = 0
        for ((count, i) in addWords.withIndex()) {
            when (i) {
                "-" -> {
                    addNumbers1 -= 2 * (addWords[count + 1].toInt())
                }
                else -> addNumbers1 += i.toInt()
            }
        }
       return addNumbers1

}*/

/*
private fun getDigits(lined: String): MutableList<String> {
    val addWords = mutableListOf<String>()
    for (i in lined.replace(" ", "").map { it.toString() }) {
        when {
            i.contains("+") || i.contains("--") && i.length % 2 == 0 -> {
                continue
            }
            i.contains("--") && i.length % 2 != 0 || i == "-" -> {
                addWords.add("-")
            }
            else -> {
                addWords.add(i)
            }
        }
    }
    return addWords
}
*/

private fun getDigitsMul(lined: String): String {
    var addWords = ""
    for (i in lined.replace(" ", "").map { it.toString() }) {
        addWords += when {
            i.contains("+") || i.contains("--") && i.length % 2 == 0 -> {
                "+"
            }
            i.contains("--") && i.length % 2 != 0 || i == "-" -> {
                "-"
            }
            i == "*" -> "*"
            i == "/" -> "/"
            i == "(" -> "("
            i == "(" -> "("
            else -> {
                i
            }
        }
    }
    return addWords
}
