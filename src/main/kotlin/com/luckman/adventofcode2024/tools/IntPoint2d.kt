package com.luckman.adventofcode2024.tools

data class IntPoint2d(
    val x: Int,
    val y: Int,
) {
    override fun toString(): String {
        return "$x,$y"
    }
}