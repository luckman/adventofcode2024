package com.luckman.adventofcode2024.day25

import com.luckman.adventofcode2024.tools.InputReader
const val HEIGHT = 7
const val WIDTH = 5
val locks = ArrayList<Lock>()
val keys = ArrayList<Key>()

fun main() {
    val lines = InputReader().readAllLines()
    var idx = 0
    while (idx < lines.size) {
        readLockOrKey(lines.subList(idx, idx + HEIGHT))
        idx += HEIGHT + 1
    }

    println(part1())
}

fun part1(): Int {
    var result = 0
    for (lock in locks) {
        for (key in keys) {
            if (match(lock, key)) {
                result++
            }
        }
    }
    return result
}

fun match(lock: Lock, key: Key): Boolean {
    for (j in 0 until WIDTH) {
        if (lock[j] + key[j] > HEIGHT - 2) {
            return false
        }
    }
    return true
}

fun readLockOrKey(lines: List<String>) {
    var isLock = lines[0].count { c -> c == '#' } == WIDTH
    val nums = ArrayList<Int>()
    for (j in 0 until WIDTH) {
        var count = -1
        for (i in 0 until HEIGHT) {
            if (lines[i][j] == '#') {
                count++
            }
        }
        nums.add(count)
    }
    if (isLock) {
        locks.add(nums)
    } else {
        keys.add(nums)
    }
}

typealias Lock = List<Int>
typealias Key = List<Int>
