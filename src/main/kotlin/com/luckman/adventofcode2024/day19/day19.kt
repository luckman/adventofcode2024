package com.luckman.adventofcode2024.day19

import com.luckman.adventofcode2024.tools.InputReader

var towels: List<String> = emptyList()
var designs: List<String> = emptyList()

fun main() {
    val lines = InputReader().readAllLines()
    towels = lines[0].split(", ")
    designs = lines.subList(2, lines.size)

    println(solve())
}

// return result for part 1 and part 2
fun solve(): Pair<Int, Long> {
    var resultPart1 = 0
    var resultPart2 = 0L
    for (design in designs) {
        val f = Array(design.length * 2) { 0L }
        f[0] = 1L
        for (i in 0..design.length) {
            if (f[i] > 0) {
                for (towel in towels) {
                    val match = (i + towel.length <= design.length)
                            && design.substring(i, i + towel.length) == towel
                    if (match) {
                        f[i + towel.length] += f[i]
                    }
                }
            }
        }
        if (f[design.length] > 0) {
            resultPart1++
            resultPart2 += f[design.length]
        }
    }
    return Pair(resultPart1, resultPart2)
}