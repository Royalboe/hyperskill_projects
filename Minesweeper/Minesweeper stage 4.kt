package minesweeper
import kotlin.random.Random

const val ROWS = 9
const val COLUMNS = 9
const val MINE = 'X'
const val SAFECELL = '.'
const val MARKEDCELL = '*'

fun main() {
    println("How many mines do you want on the field?")
    val input = readLine()!!.toInt()
    val mines = Mines(input)
    mines.processMineField()
    val hiddenMines = mines.mineField
    val displayedMineField = mines.duplicateMine
    displayedMineField.forEach{
        for(i in it.indices) {
            if(it[i] == MINE) {
                it[i] = SAFECELL
            } else continue
        }
    }
    printField(displayedMineField)

    var inputMine = input
    while (inputMine > 0) {
        println("Set/delete mines marks (x and y coordinates): ")
        val (x, y) = readLine()!!.split(" ")
        val xCor = x.toInt() - 1
        val yCor = y.toInt() - 1
        when {
            hiddenMines[yCor][xCor].isDigit() -> {
                println("There is a number here!")
            }
            hiddenMines[yCor][xCor] == MINE -> {
                displayedMineField[yCor][xCor] = MARKEDCELL
                printField(displayedMineField)
                --inputMine
            }
            hiddenMines[yCor][xCor] == SAFECELL
                    && displayedMineField[yCor][xCor] == SAFECELL -> {
                displayedMineField[yCor][xCor] = MARKEDCELL
                printField(displayedMineField)
                ++inputMine
            }
            displayedMineField[yCor][xCor] == MARKEDCELL -> {
                if(hiddenMines[yCor][xCor] == SAFECELL) {
                    displayedMineField[yCor][xCor] = SAFECELL
                    printField(displayedMineField)
                    --inputMine
                }
            }
            else -> continue
        }
    }
    println("Congratulations! You found all the mines!")
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
    }

    private fun addHints(field: MutableList<MutableList<Char>>, list: MutableList<Char>, index: Int) {
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

private fun printField(field: MutableList<MutableList<Char>>) {
    println(" |123456789|")
    println("—|—————————|")
    field.forEachIndexed {index, list ->
        println("${index + 1}|${list.joinToString("")}|")
    }
    println("—|—————————|")
}