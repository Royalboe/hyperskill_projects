package minesweeper
import kotlin.random.Random

const val ROWS = 9
const val COLUMNS = 9
const val MINES = 'X'
const val SAFECELLS = '.'

fun main() {
    println("How many mines do you want on the field?")
    val input = readLine()!!.toInt()
    val mines = Mines(input)
    mines.processMines()
}

// A class to create an instance of a field containing a number of mines stated by the player
class Mines(private var input: Int) {

    // A 2-D list to represent the minefield
    private val mineField = MutableList(ROWS) { MutableList(COLUMNS) {SAFECELLS} }

    fun processMines() {
        while (input > 0) {
            val iRows = Random.nextInt(0, ROWS)
            val iColumns = Random.nextInt(0, COLUMNS)
            if(mineField[iRows][iColumns] != MINES){
                mineField[iRows][iColumns] = MINES
                input--
            }
        }
        for (index in mineField.indices) {
            when(index) {
                0 -> { printFirstRow(mineField, mineField[index], index) }
                8 -> { printLastRow(mineField, mineField[index], index) }
                else -> { printRow(mineField, mineField[index], index) }
            }
        }
    }

    private fun printRow(field: MutableList<MutableList<Char>>, list: MutableList<Char>, index: Int) {
        var count = 0
        for (columnIndex in list.indices) {
            when (columnIndex) {
                0 -> {
                    if (checkSouth(field, index, columnIndex)) count++
                    if (checkEast(field, index, columnIndex)) count++
                    if (checkSouthEast(field, index, columnIndex)) count++
                    if (checkNorth(field, index, columnIndex)) count++
                    if (checkNorthEast(field, index, columnIndex)) count++
                    if (count > 0 && list[columnIndex] != MINES) {
                        print(count)
                    } else print(list[columnIndex])
                    count = 0
                }
                8 -> {
                    if (checkSouth(field, index, columnIndex)) count++
                    if (checkWest(field, index, columnIndex)) count++
                    if (checkSouthWest(field, index, columnIndex)) count++
                    if (checkNorthWest(field, index, columnIndex)) count++
                    if (checkNorth(field, index, columnIndex)) count++
                    if (count > 0 && list[columnIndex] != MINES) {
                        print(count)
                    } else print(list[columnIndex])
                    count = 0
                }
                else -> {
                    if (checkSouth(field, index, columnIndex)) count++
                    if (checkEast(field, index, columnIndex)) count++
                    if (checkSouthEast(field, index, columnIndex)) count++
                    if (checkSouthWest(field, index, columnIndex)) count++
                    if (checkWest(field, index, columnIndex)) count++
                    if (checkNorthEast(field, index, columnIndex)) count++
                    if (checkNorthWest(field, index, columnIndex)) count++
                    if (checkNorth(field, index, columnIndex)) count++
                    if (count > 0 && list[columnIndex] != MINES) {
                        print(count)
                    } else print(list[columnIndex])
                    count = 0
                }
            }
        }
        println()
    }

    private fun printLastRow(
        field: MutableList<MutableList<Char>>,
        list: MutableList<Char>,
        index: Int) {
        var count = 0
        for (columnIndex in list.indices) {
            when (columnIndex) {
                0 -> {
                    if (checkNorth(field, index, columnIndex)) count++
                    if (checkEast(field, index, columnIndex)) count++
                    if (checkNorthEast(field, index, columnIndex)) count++
                    if (count > 0 && list[columnIndex] != MINES) {
                        print(count)
                    } else print(list[columnIndex])
                    count = 0
                }
                8 -> {
                    if (checkNorth(field, index, columnIndex)) count++
                    if (checkWest(field, index, columnIndex)) count++
                    if (checkNorthWest(field, index, columnIndex)) count++
                    if (count > 0 && list[columnIndex] != MINES) {
                        print(count)
                    } else print(list[columnIndex])
                    count = 0
                }
                else -> {
                    if (checkNorth(field, index, columnIndex)) count++
                    if (checkEast(field, index, columnIndex)) count++
                    if (checkNorthEast(field, index, columnIndex)) count++
                    if (checkNorthWest(field, index, columnIndex)) count++
                    if (checkWest(field, index, columnIndex)) count++
                    if (count > 0 && list[columnIndex] != MINES) {
                        print(count)
                    } else print(list[columnIndex])
                    count = 0
                }
            }
        }
        println()
    }

    private fun printFirstRow(
        field: MutableList<MutableList<Char>>,
        list: MutableList<Char>,
        index: Int) {
        var count = 0
        for (columnIndex in list.indices) {
            when (columnIndex) {
                0 -> {
                    if (checkSouth(field, index, columnIndex)) count++
                    if (checkEast(field, index, columnIndex)) count++
                    if (checkSouthEast(field, index, columnIndex)) count++
                    if (count > 0 && list[columnIndex] != MINES) {
                        print(count)
                    } else print(list[columnIndex])
                    count = 0
                }
                8 -> {
                    if (checkSouth(field, index, columnIndex)) count++
                    if (checkWest(field, index, columnIndex)) count++
                    if (checkSouthWest(field, index, columnIndex)) count++
                    if (count > 0 && list[columnIndex] != MINES) {
                        print(count)
                    } else print(list[columnIndex])
                    count = 0
                }
                else -> {
                    if (checkSouth(field, index, columnIndex)) count++
                    if (checkEast(field, index, columnIndex)) count++
                    if (checkSouthEast(field, index, columnIndex)) count++
                    if (checkSouthWest(field, index, columnIndex)) count++
                    if (checkWest(field, index, columnIndex)) count++
                    if (count > 0 && list[columnIndex] != MINES) {
                        print(count)
                    } else print(list[columnIndex])
                    count = 0
                }
            }
        }
        println()
    }

    private fun checkSouth(field: MutableList<MutableList<Char>>, rowIndex: Int, columnIndex: Int)
    = field[rowIndex + 1][columnIndex] == MINES

    private fun checkSouthWest(field: MutableList<MutableList<Char>>, rowIndex: Int, columnIndex: Int)
            = field[rowIndex + 1][columnIndex - 1] == MINES

    private fun checkSouthEast(field: MutableList<MutableList<Char>>, rowIndex: Int, columnIndex: Int)
            = field[rowIndex + 1][columnIndex + 1] == MINES

    private fun checkWest(field: MutableList<MutableList<Char>>, rowIndex: Int, columnIndex: Int)
            = field[rowIndex][columnIndex - 1] == MINES

    private fun checkEast(field: MutableList<MutableList<Char>>, rowIndex: Int, columnIndex: Int)
            = field[rowIndex][columnIndex + 1] == MINES

    private fun checkNorth(field: MutableList<MutableList<Char>>, rowIndex: Int, columnIndex: Int)
            = field[rowIndex - 1][columnIndex] == MINES

    private fun checkNorthWest(field: MutableList<MutableList<Char>>, rowIndex: Int, columnIndex: Int)
            = field[rowIndex - 1][columnIndex - 1] == MINES

    private fun checkNorthEast(field: MutableList<MutableList<Char>>, rowIndex: Int, columnIndex: Int)
            = field[rowIndex - 1][columnIndex + 1] == MINES
}