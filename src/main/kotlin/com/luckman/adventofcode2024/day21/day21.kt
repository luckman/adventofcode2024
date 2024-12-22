package com.luckman.adventofcode2024.day21

import com.luckman.adventofcode2024.tools.InputReader
import java.util.LinkedList
import kotlin.math.abs
import kotlin.text.StringBuilder

var codes: List<String> = emptyList()
val keyboard1 = listOf(
    "789",
    "456",
    "123",
    " 0A"
)
val keyboard2 = listOf(
    " ^A",
    "<v>"
)
var keyboard1ShortestDist = PossiblePathsResult()
var keyboard2ShortestDist = PossiblePathsResult()

fun main() {
    codes = InputReader().readAllLines()
    keyboard1ShortestDist = calcShortestPathsForKeyboard(keyboard1)
    keyboard2ShortestDist = calcShortestPathsForKeyboard(keyboard2)

    println(part1())
}

fun part1(): Long {
    var result = 0L
    for (code in codes) {
        val seq2 = calcNextSequences(code, keyboard1ShortestDist)
        val seq3 = ArrayList<String>()
        for (currentSequence in seq2) {
            seq3.addAll(calcNextSequences(currentSequence, keyboard2ShortestDist))
        }

        val seq4 = ArrayList<String>()
        for (currentSequence in seq3) {
            seq4.addAll(calcNextSequences(currentSequence, keyboard2ShortestDist))
        }

        val minLength = seq4.minOf { s -> s.length }

        println("len: $minLength")
        val codeNum = code.replace("A", "").toInt()
        result += codeNum * minLength
    }

    return result
}

fun calcNextSequences(sequence: String, pathResult: PossiblePathsResult): List<String> {
    calcNextSeqCurAppend.clear()
    calcNextSeqResult.clear()
    doCalcNextSeq(0, sequence, pathResult)
    val minLength = calcNextSeqResult.map { s -> s.length }.min()
    calcNextSeqResult.removeIf { s -> s.length > minLength }
    return calcNextSeqResult.toList() // copy elements
}

val calcNextSeqCurAppend = LinkedList<String>()
val calcNextSeqResult = ArrayList<String>()
fun doCalcNextSeq(pos: Int, sequence: String, pathResult: PossiblePathsResult) {
    if (pos == sequence.length) {
        calcNextSeqResult.add(calcNextSeqCurAppend.joinToString(separator = ""))
        return
    }
    val prevChar = if (pos == 0) { 'A' } else { sequence[pos - 1] }
    val curChar = sequence[pos]
    for (nextAppend in pathResult[Pair(prevChar, curChar)]!!) {
        calcNextSeqCurAppend.addLast(nextAppend)
        doCalcNextSeq(pos + 1, sequence, pathResult)
        calcNextSeqCurAppend.removeLast()
    }
}

fun calcShortestPathsForKeyboard(keyboard: List<String>): PossiblePathsResult {
    val result = PossiblePathsResult()

    for (fromX in keyboard.indices) {
        for (fromY in 0 until keyboard[fromX].length) {
            val fromChar = keyboard[fromX][fromY]
            for (toX in keyboard.indices) {
                for (toY in 0 until keyboard[toX].length) {
                    val toChar = keyboard[toX][toY]

                    val xDiffAmt = abs(toX - fromX)
                    val yDiffAmt = abs(toY - fromY)
                    val moves = xDiffAmt + yDiffAmt
                    for (i in 0 until (1 shl moves)) {
                        if (i.countOneBits() == xDiffAmt) {
                            val curAppend = StringBuilder()
                            var curX = fromX
                            var curY = fromY
                            var noSpaceInPath = true
                            for (move in 0 until moves) {
                                if ((i and (1 shl move)) != 0) {
                                    if (toX > fromX) {
                                        curAppend.append("v")
                                        curX++
                                    } else {
                                        curAppend.append("^")
                                        curX--
                                    }
                                } else {
                                    if (toY > fromY) {
                                        curAppend.append(">")
                                        curY++
                                    } else {
                                        curAppend.append("<")
                                        curY--
                                    }
                                }
                                if (keyboard[curX][curY] == ' ') {
                                    noSpaceInPath = false
                                    break
                                }
                            }

                            if (noSpaceInPath) {
                                if (!result.containsKey(Pair(fromChar, toChar))) {
                                    result[Pair(fromChar, toChar)] = ArrayList()
                                }
                                result[Pair(fromChar, toChar)]!!.add(curAppend.toString() + "A")
                            }
                        }
                    }
                }
            }
        }
    }

    return result
}

typealias PossiblePathsResult = HashMap<Pair<Char, Char>, MutableList<String>>


