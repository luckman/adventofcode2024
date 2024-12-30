package com.luckman.adventofcode2024.day22

import com.luckman.adventofcode2024.tools.InputReader

val initialNumbers = InputReader().readAsIntegers()
const val MOD_WITH_AND_OPERATION = (1 shl 24) - 1 // 16777216 - 1
const val NUMBERS_AMT = 2000

fun main() {
    // println(part1())
    println(part2())
}

fun part2(): Int {
    val pricesAndChanges = ArrayList<List<PriceAndChanges4>>()
    val priceByChangeForEachNumber = ArrayList<Map<Changes4, Int>>()
    val allChanges = HashSet<Changes4>()
    for (num in initialNumbers) {
        val currentNumberList = generatePricesAndChanges(num, NUMBERS_AMT)
        pricesAndChanges.add(currentNumberList)

        val priceByChange = HashMap<Changes4, Int>()
        for (priceAndChanges in currentNumberList) {
            val changes = priceAndChanges.changes
            if (changes != null) {
                if (!priceByChange.containsKey(changes)) {
                    priceByChange[changes] = priceAndChanges.price
                }
                allChanges.add(changes)
            }
        }
        priceByChangeForEachNumber.add(priceByChange)
    }

    var result = -1
    var bestChange: Changes4? = null
    for (changes in allChanges) {
        var sum = 0
        for (i in priceByChangeForEachNumber.indices) {
            val maxPrices = priceByChangeForEachNumber[i]
            sum += maxPrices.getOrDefault(changes, 0)
        }
        if (sum > result) {
            result = sum
            bestChange = changes
        }
    }
    println("Best change: $bestChange")
    return result
}

fun generatePricesAndChanges(number: Int, amt: Int): List<PriceAndChanges4> {
    var curNumber = number
    val prices = ArrayList<Int>(amt + 1)
    prices.add(number % 10)
    for (i in 1 until amt) {
        val step1 = ((curNumber shl 6) xor curNumber) and MOD_WITH_AND_OPERATION
        val step2 = ((step1 shr 5) xor step1) and MOD_WITH_AND_OPERATION
        val step3 = ((step2 shl 11) xor step2) and MOD_WITH_AND_OPERATION
        curNumber = step3
        prices.add(curNumber % 10)
    }

    val result = ArrayList<PriceAndChanges4>()
    for (i in 0 until amt) {
        if (i < 4) {
            result.add(PriceAndChanges4(prices[i], null))
        } else {
            result.add(
                PriceAndChanges4(prices[i], Changes4(
                prices[i - 3] - prices[i - 4],
                prices[i - 2] - prices[i - 3],
                prices[i - 1] - prices[i - 2],
                prices[i] - prices[i - 1])
                )
            )
        }
    }
    return result
}

fun part1(): Long {
    var sum = 0L
    for (num in initialNumbers) {
        val secret2000 = generateSecret(num, NUMBERS_AMT)
        sum += secret2000
    }
    return sum
}

fun generateSecret(number: Int, amt: Int): Int {
    var curNumber = number
    for (i in 0 until amt) {
        val step1 = ((curNumber shl 6) xor curNumber) and MOD_WITH_AND_OPERATION
        val step2 = ((step1 shr 5) xor step1) and MOD_WITH_AND_OPERATION
        val step3 = ((step2 shl 11) xor step2) and MOD_WITH_AND_OPERATION
        curNumber = step3
    }
    return curNumber
}

data class Changes4(
    val c1: Int,
    val c2: Int,
    val c3: Int,
    val c4: Int
)

data class PriceAndChanges4(
    val price: Int,
    val changes: Changes4?
)