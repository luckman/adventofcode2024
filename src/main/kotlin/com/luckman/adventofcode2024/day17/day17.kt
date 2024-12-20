package com.luckman.adventofcode2024.day17

import com.luckman.adventofcode2024.tools.InputReader
import java.lang.RuntimeException

var registerA = 0
var registerB = 0
var registerC = 0

var program: List<Int> = emptyList()

fun main() {
    val lines = InputReader().readAllLines()
    registerA = lines[0].split(": ")[1].toInt()
    registerB = lines[1].split(": ")[1].toInt()
    registerC = lines[2].split(": ")[1].toInt()
    program = lines[4].split(": ")[1].split(",").map { s -> s.toInt() }

//    val result = part1()
//    println(result.joinToString(separator = ","))

    println(part2())
}

// 2,4   regB = regA % 8
// 1,5   regB = regB xor 5 [101]
// 7,5   regC = regA / (2 ^ regB)
// 1,6   regB = regB xor 6 [110]
// 0,3   regA = regA / 8
// 4,1   regB = regB xor regC
// 5,5   println(regB % 8)
// 3,0   go to 0
//
// each iteration result depends on up to 10 last bits of regA value
// then last 3 bits are removed from regA
fun part2(): Long {
    val initA = registerA
    val initB = registerB
    val initC = registerC
    val expectedProgram = program

    val digitFromAvalue = HashMap<Int, ArrayList<Int>>()

    for (i in 0 until pow2(10)) {
        registerA = i
        val resultFirstDigit = part1()[0]
        if (!digitFromAvalue.containsKey(resultFirstDigit)) {
            digitFromAvalue[resultFirstDigit] = ArrayList()
        }
        digitFromAvalue[resultFirstDigit]!!.add(i)
    }

    val currentMatchNumbers = HashSet<Long>()
    val lastDigit = expectedProgram.last()
    currentMatchNumbers.addAll(digitFromAvalue[lastDigit]!!.map { it.toLong() })

    for (i in expectedProgram.size - 2 downTo 0) {
        val nextDigit = expectedProgram[i]
        val nextMatchNumbers = digitFromAvalue[nextDigit]!!

        val tmpSet = HashSet<Long>()
        tmpSet.addAll(currentMatchNumbers)
        currentMatchNumbers.clear()
        for (num in tmpSet) {
            for (nextNum in nextMatchNumbers) {
                if ((nextNum / 8) and 127 == (num and 127).toInt()) {
                    currentMatchNumbers.add(num * 8 + nextNum % 8)
                }
            }
        }
    }

    // some of the numbers will result in longer sequences, but minimum is always the correct answer
    return currentMatchNumbers.min()
}

fun part1(): List<Int> {
    val result = ArrayList<Int>()

    var pointer = 0
    while (pointer < program.size) {
        val command = program[pointer]
        var operand = program[pointer + 1]

        when(command) {
            0 -> {
                operand = comboOperandValue(operand.toInt())
                registerA /= pow2(operand.toInt())
            }
            1 -> {
                registerB = registerB xor operand
            }
            2 -> {
                operand = comboOperandValue(operand)
                registerB = operand % 8
            }
            3 -> {
                if (registerA != 0) {
                    pointer = operand
                    continue
                }
            }
            4 -> {
                registerB = registerB xor registerC
            }
            5 -> {
                operand = comboOperandValue(operand)
                result.add(operand % 8)
            }
            6 -> {
                operand = comboOperandValue(operand)
                registerB = registerA / pow2(operand)
            }
            7 -> {
                operand = comboOperandValue(operand)
                registerC = registerA / pow2(operand)
            }
        }

        pointer += 2
    }

    return result
}

fun comboOperandValue(v: Int): Int {
    return when (v) {
        in 0..3 -> v
        4 -> return registerA
        5 -> return registerB
        6 -> return registerC
        else -> throw RuntimeException()
    }
}

val pow2 = ArrayList<Int>()
fun pow2(v: Int): Int {
    if (pow2.isEmpty()) {
        var c = 1
        for (i in 0 until 31) {
            pow2.add(c)
            c *= 2
        }
    }
    return pow2[v]
}
