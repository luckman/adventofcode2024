package com.luckman.adventofcode2024.tools

import java.io.File
import java.nio.file.Files

class InputReader {
    companion object {
        private val file = File("input.txt")
    }

    fun readAllLines(): List<String> {
        return Files.readAllLines(file.toPath())
    }

    fun readAsPoint2dWithCommas(): List<IntPoint2d> {
        val lines = readAllLines()
        val result = ArrayList<IntPoint2d>(lines.size)
        for (line in lines) {
            val split = line.split(",")
            result.add(IntPoint2d(split[0].toInt(), split[1].toInt()))
        }
        return result
    }

    fun readAsIntegers(): List<Int> {
        val lines = readAllLines()
        return lines.map { s -> s.toInt() }
    }
}