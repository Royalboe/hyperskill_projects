package calculator

val stack = StackWithList()
val exp = mutableListOf<String>()
val vars = mutableMapOf<String, Int>()


fun main() {
    while (true) {
        val s = readLine()!!
        if (s.isBlank()) continue
        if (s == "/exit") break
        if (s == "/help") {
            println("The program calculates the sum of numbers")
            continue
        }
        if (s[0] == '/') {
            println("Unknown command")
            continue
        }
        val ss = s.checkStr()
//println("ss= $ss.")
        decomp(ss)
//println("exp= $exp.")
        comp(exp)
    }
    println("Bye!")
}

fun String.checkStr(): List<String> {
    var str = this
    str = str.replace("---", "-")
    str = str.replace("--", "+")
    str = str.replace("++", "+")
    for (w in listOf("+", "=", "*", "/")) {
        str = str.replace(w, " $w ")
    }
    str = str.replace("\\s+".toRegex(), " ").trim()
    return str.split(" ")
}

fun String.opToInt(): Int = if (this.isNumber()) this.toInt() else vars[this] ?: 0

fun String.isNumber() = this.matches("[-+]?[0-9]+\\b".toRegex())

fun String.isIdent() = this.matches("[a-zA-Z]+\\b".toRegex())

fun assign(k: String, v: String): Int? {
    val isKvalid = k.isIdent()
    val isVvalid = v.isIdent()
    val isVnumber = v.isNumber()

    if (!isKvalid) {
        println("Invalid identifier")
        return null
    }
    if (!isVnumber) { // not number
        if (!isVvalid) {
            println("Invalid assignment")
            return null
        }
        if (!vars.containsKey(v)) {
            println("Unknown variable")
            return null
        }
    }
    vars[k] = if (isVnumber) v.toInt() else vars[v] ?: 0
    return vars[k]
}

fun comp(list: List<String>) {
    var res = 0
    var isCalc = false
    var isAss = false
    val p = mutableListOf<String>()
    for (w in list) {
        if (w in listOf("+", "-", "*", "/", "=")) {
            isCalc = true
            isAss = isAss || w == "="
            val op1 = p[p.lastIndex - 1]
            val op2 = p[p.lastIndex]
            res = when (w) {
                "+" -> op1.opToInt() + op2.opToInt()
                "-" -> op1.opToInt() - op2.opToInt()
                "*" -> op1.opToInt() * op2.opToInt()
                "/" -> op1.opToInt() / op2.opToInt()
                "=" -> assign(op1, op2) ?: 0
                else -> 0
            }
            p.removeAt(p.lastIndex)
            p[p.lastIndex] = res.toString()
        } else {
            p.add(w)
        }
    }
    if (!isCalc) {
        if (vars.containsKey(p.last())) {
            res = vars[p.last()] ?: 0
        } else {
            println("Unknown variable")
            return
        }
    }
    if (!isAss) println(res)
}

fun decomp(list: List<String>) {
    exp.clear()
    for (w in list) {
        when (w) {
            "(" -> stack.push(w)
            ")" -> while (!stack.isEmpty()) {
                val st = stack.pop().toString()
                if (st != "(") exp.add(st)
            }
            "+" -> pushLow(w)
            "-" -> pushLow(w)
            "*" -> stack.push(w)
            "/" -> stack.push(w)
            "=" -> stack.push(w)
            else -> exp.add(w)
        }
    }
    while (!stack.isEmpty()) {
        exp.add(stack.pop().toString())
    }
}

fun pushLow(w: String) {
    if (!stack.isEmpty() && stack.peek().toString() != "(" ) {
        val st = stack.pop().toString()
        exp.add(st)
    }
    stack.push(w)
}

class StackWithList{
    private val elements: MutableList<Any> = mutableListOf()

    fun isEmpty() = elements.isEmpty()

    fun size() = elements.size

    fun push(item: Any) = elements.add(item)

    fun pop() : Any? {
        val item = elements.lastOrNull()
        if (!isEmpty()){
            elements.removeAt(elements.size -1)
        }
        return item
    }
    fun peek() : Any? = elements.lastOrNull()

    override fun toString(): String = elements.toString()
}
