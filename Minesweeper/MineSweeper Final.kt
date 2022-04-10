package minesweeper
import kotlin.random.Random

const val ROWS = 9
const val COLUMNS = 9
const val MINE = 'X'
const val SAFECELL = '.'
const val MARKEDCELL = '*'
const val EXPLOREDCELL = '/'
var isFirst = true
val displayedMineField = mutableListOf<MutableList<Char>>()
val hiddenMinesField = mutableListOf<MutableList<Char>>()
val checked = mutableListOf<String>()

enum class MineSweeper(val status: Int) {
    GAMEOVER(0),
    GAMEON(1)
}

fun main() {
    println("How many mines do you want on the field?")
    val input = readLine()!!.toInt()
    val mines = Mines(input)
    mines.processMineField()
    hiddenMinesField.addAll(mines.mineField)
    displayedMineField.addAll(mines.duplicateMine)
    printField(displayedMineField)

    var inputMine = input
    var status = 1
    while (inputMine > 0 && status == 1) {
        var freeCellsCount = 0
        var exploredCellsCount = 0
        var minesCount = 0
        var markedCellsCount = 0
        println("Set/unset mine marks or claim a cell as free: ")
        val (x, y, command) = readLine()!!.split(" ")
        val xCor = x.toInt() - 1
        val yCor = y.toInt() - 1
        when (command) {
            "mine" -> inputMine = mark(displayedMineField, hiddenMinesField, xCor, yCor, inputMine)
            "free" -> status = exploreCells(displayedMineField, hiddenMinesField, xCor, yCor, mines)
            else -> println("Unknown Command")
        }

        // To check if all safe cells have been identified
        val pair = compareMineFieldForSafeCells(freeCellsCount, exploredCellsCount)
        exploredCellsCount = pair.first
        freeCellsCount = pair.second

        // To check if all the cells containing mines are marked and no safe cell is marked
        val pair1 = compareFieldsForRiggedCells(minesCount, markedCellsCount)
        markedCellsCount = pair1.first
        minesCount = pair1.second

        if (freeCellsCount == exploredCellsCount || minesCount == markedCellsCount || inputMine == 0) {
            println("Congratulations! You found all the mines!")
            break
        }
        if (status == 0) println("You stepped on a mine and failed!")
    }
}

private fun compareFieldsForRiggedCells(
    minesCount: Int,
    markedCellsCount: Int
): Pair<Int, Int> {
    var minesCount1 = minesCount
    var markedCellsCount1 = markedCellsCount
    for (i in hiddenMinesField.indices) {
        for (v in hiddenMinesField[i].indices) {
            if (hiddenMinesField[i][v] == MINE || hiddenMinesField[i][v].isDigit()) minesCount1++
            if (displayedMineField[i][v] == MARKEDCELL || displayedMineField[i][v].isDigit()) markedCellsCount1++
        }
    }
    return Pair(markedCellsCount1, minesCount1)
}

private fun compareMineFieldForSafeCells(
    freeCellsCount: Int,
    exploredCellsCount: Int
): Pair<Int, Int> {
    var freeCellsCount1 = freeCellsCount
    var exploredCellsCount1 = exploredCellsCount
    for (i in hiddenMinesField.indices) {
        for (v in hiddenMinesField[i].indices) {
            if (hiddenMinesField[i][v] == SAFECELL || hiddenMinesField[i][v].isDigit()) freeCellsCount1++
            if (displayedMineField[i][v] == EXPLOREDCELL || displayedMineField[i][v].isDigit()) exploredCellsCount1++
        }
    }
    return Pair(exploredCellsCount1, freeCellsCount1)
}

// When the command is free
fun exploreCells(
    display: MutableList<MutableList<Char>>,
    hidden: MutableList<MutableList<Char>>,
    x: Int,
    y: Int,
    mine: Mines
): Int {
    return when {
        !hidden[y][x].isDigit() && hidden[y][x] == SAFECELL -> {
            isFirst = false
            checkAndUpdateCells(display, hidden, x, y)
            printField(display)
            MineSweeper.GAMEON.status
        }
        hidden[y][x].isDigit() -> {
            isFirst = false
            display[y][x] = hidden[y][x]
            printField(display)
            MineSweeper.GAMEON.status
        }
        // If mine cell is a mine and the command is called for the first time, make the cell safe, reset the mineField
        // and add hints
        hidden[y][x] == MINE -> {
            if (isFirst) {
                isFirst = false
                val u = Random.nextInt(0, ROWS)
                val t = Random.nextInt(0, COLUMNS)
                if (display[u][t] != MARKEDCELL && hidden[u][t] != MINE) {
                    hidden[u][t] = MINE
                    hidden[y][x] = SAFECELL
                    for (n in hidden) {
                        for (b in n.indices) {
                            if (n[b].isDigit()) n[b] = SAFECELL
                        }
                    }
                    for (w in hidden.indices) {
                        mine.addHints(hidden, hidden[w], w)
                    }
                }
                return exploreCells(display, hidden, x, y, mine)
            } else {
                for (iRows in hidden.indices) {
                    for (iCol in hidden[y].indices) {
                        if (hidden[iRows][iCol] == MINE) {
                            display[iRows][iCol] = hidden[iRows][iCol]
                        }
                    }
                }
            }
            printField(display)
            MineSweeper.GAMEOVER.status
        }
        else -> {
            MineSweeper.GAMEON.status
        }
    }
}

fun checkAndUpdateCells(
    display: MutableList<MutableList<Char>>,
    hidden: MutableList<MutableList<Char>>,
    x: Int,
    y: Int
) {
    if (checked.isNotEmpty() && checked.contains("${y}y ${x}x")) {
        return
    } else checked.add("${y}y ${x}x")
    if (
        y == -1
        || y == hidden.size
        || x == -1
        || x == hidden[y].size
    ) return
    if (display[y][x] == EXPLOREDCELL) return
    if (hidden[y][x].isDigit()) {
        display[y][x] = hidden[y][x]
        return
    }
    if (
        hidden[y][x] == SAFECELL) {
        display[y][x] = EXPLOREDCELL
        checkAndUpdateCells(display, hidden, x - 1, y - 1)
        checkAndUpdateCells(display, hidden, x - 1, y + 1)
        checkAndUpdateCells(display, hidden, x - 1, y)
        checkAndUpdateCells(display, hidden, x + 1, y - 1)
        checkAndUpdateCells(display, hidden, x + 1, y)
        checkAndUpdateCells(display, hidden, x + 1, y + 1)
        checkAndUpdateCells(display, hidden, x, y + 1)
        checkAndUpdateCells(display, hidden, x, y - 1)
    }
}

// For when the command is "mine"
fun mark (
    display: MutableList<MutableList<Char>>,
    hidden: MutableList<MutableList<Char>>,
    x: Int,
    y: Int,
    input: Int): Int {
    var inputMine = input
    when {
        hidden[y][x].isDigit() && display[y][x] == MARKEDCELL -> {
            display[y][x] = SAFECELL
            printField(display)
            --inputMine
        }
        display[y][x] == MARKEDCELL && hidden[y][x] == SAFECELL-> {
                display[y][x] = SAFECELL
                printField(display)
                --inputMine
        }
        display[y][x] == MARKEDCELL && hidden[y][x] == MINE -> {
            ++inputMine
            display[y][x] = SAFECELL
            printField(display)
        }
        hidden[y][x] == SAFECELL
                && display[y][x] == SAFECELL -> {
            display[y][x] = MARKEDCELL
            printField(display)
            ++inputMine
        }
        hidden[y][x].isDigit() -> {
            display[y][x] = MARKEDCELL
            printField(display)
            ++inputMine
        }
        hidden[y][x] == MINE -> {
            display[y][x] = MARKEDCELL
            printField(display)
            --inputMine
        }
    }
    return inputMine
}

// A class to create an instance of a field containing a number of mines stated by the player
class Mines(private var input: Int) {
    // A 2-D list to represent the minefield
    val mineField = MutableList(ROWS) { MutableList(COLUMNS) { SAFECELL } }
    val duplicateMine = MutableList(ROWS) { MutableList(COLUMNS) { SAFECELL } }

    fun processMineField() {
        while (input > 0) {
            val iRows = Random.nextInt(0, ROWS)
            val iColumns = Random.nextInt(0, COLUMNS)
            if (mineField[iRows][iColumns] != MINE) {
                mineField[iRows][iColumns] = MINE
                duplicateMine[iRows][iColumns] = MINE
                input--
            }
        }
        for (index in mineField.indices) {
            addHints(mineField, mineField[index], index)
            addHints(duplicateMine, duplicateMine[index], index)
        }
        duplicateMine.forEach{
            for(i in it.indices) {
                    it[i] = SAFECELL
            }
        }
    }

    fun addHints(field: MutableList<MutableList<Char>>, list: MutableList<Char>, index: Int) {
        for (columnIndex in list.indices) {
            var count = 0
            if (isMine(field, index, columnIndex)) {
                list[columnIndex] = MINE
                continue
            }
            if (isMine(field, index - 1, columnIndex - 1)) count++
            if (isMine(field, index - 1, columnIndex + 1)) count++
            if (isMine(field, index - 1, columnIndex)) count++
            if (isMine(field, index + 1, columnIndex - 1)) count++
            if (isMine(field, index + 1, columnIndex + 1)) count++
            if (isMine(field, index + 1, columnIndex)) count++
            if (isMine(field, index, columnIndex - 1)) count++
            if (isMine(field, index, columnIndex + 1)) count++
            if (count > 0 && list[columnIndex] != MINE) {
                field[index][columnIndex] = count.toString().first()
            }
        }
    }

    // Check for the index to not throw the indexOutOfBounds error
    private fun isMine(field: MutableList<MutableList<Char>>, rowIndex: Int, columnIndex: Int): Boolean {
        return if (
            rowIndex == -1
            || rowIndex == field.size
            || columnIndex == -1
            || columnIndex == field[rowIndex].size
        ) {
            false
        } else {
            field[rowIndex][columnIndex] == MINE
        }
    }
}

// To print out the grid in the required format
private fun printField(field: MutableList<MutableList<Char>>) {
    println(" |123456789|")
    println("—|—————————|")
    field.forEachIndexed {index, list ->
        println("${index + 1}|${list.joinToString("")}|")
    }
    println("—|—————————|")
}