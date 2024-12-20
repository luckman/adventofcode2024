package com.luckman.adventofcode2024.day20

import com.luckman.adventofcode2024.tools.InputReader
import com.luckman.adventofcode2024.tools.IntPoint2d
import java.util.LinkedList

const val INFINITY = Int.MAX_VALUE / 2
val dx = intArrayOf(0, -1, 0, 1)
val dy = intArrayOf(-1, 0, 1, 0)

var map: List<String> = emptyList()
var n = 0
var m = 0
var minCheatRequired = 100
var sPoint = IntPoint2d(0, 0)
var ePoint = IntPoint2d(0, 0)

fun main() {
    map = InputReader().readAllLines()
    n = map.size
    m = map[0].length
    for (i in 0 until n) {
        for (j in 0 until m) {
            if (map[i][j] == 'S') {
                sPoint = IntPoint2d(i, j)
            }
            if (map[i][j] == 'E') {
                ePoint = IntPoint2d(i, j)
            }
        }
    }

    println(part2())
}

fun part2(): Int {
    val dist = calcDist()

    var result = 0
    for (i in 0 until n) {
        for (j in 0 until m) {
            val cheatTo = IntPoint2d(i, j)
            if (!canMoveInto(cheatTo)) {
                continue
            }

            for (k0 in 0..20) {
                for (k1 in 0..20 - k0) {
                    for (k2 in 0..20 - k1 - k0) {
                        if (k2 != 0 && k0 != 0) {
                            continue
                        }
                        for (k3 in 0..20 - k2 - k1 - k0) {
                            if (k3 != 0 && k1 != 0) {
                                continue
                            }
                            val cheatFrom = IntPoint2d(
                                cheatTo.x + dx[0] * k0 + dx[1] * k1 + dx[2] * k2 + dx[3] * k3,
                                cheatTo.y + dy[0] * k0 + dy[1] * k1 + dy[2] * k2 + dy[3] * k3
                            )
                            val cheatLength = k0 + k1 + k2 + k3
                            if (canMoveInto(cheatFrom)) {
                                if (dist[cheatTo.x][cheatTo.y] - dist[cheatFrom.x][cheatFrom.y] >= cheatLength + minCheatRequired) {
                                    result++
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    return result
}

fun part1(): Int {
    val dist = calcDist()

    var result = 0
    for (i in 0 until n) {
        for (j in 0 until m) {
            val cheatTo = IntPoint2d(i, j)
            if (canMoveInto(cheatTo)) {
                for (m1 in 0 until 4) {
                    for (m2 in 0 until 4) {
                        val cheatFrom = IntPoint2d(i + dx[m1] + dx[m2], j + dy[m1] + dy[m2])
                        if (canMoveInto(cheatFrom)) {
                            if (dist[cheatTo.x][cheatTo.y] - dist[cheatFrom.x][cheatFrom.y] >= 2 + minCheatRequired) {
                                result++
                            }
                        }
                    }
                }
            }
        }
    }
    return result
}

fun calcDist(): Array<Array<Int>> {
    val dist = Array(n) { Array (m) { INFINITY } }
    dist[sPoint.x][sPoint.y] = 0
    val queue = LinkedList<IntPoint2d>()
    queue.add(sPoint)

    while (!queue.isEmpty()) {
        val curPoint = queue.poll()
        val curDist = dist[curPoint.x][curPoint.y]

        for (k in 0 until 4) {
            val nextPoint = IntPoint2d(curPoint.x + dx[k], curPoint.y + dy[k])
            if (canMoveInto(nextPoint) && curDist + 1 < dist[nextPoint.x][nextPoint.y]) {
                queue.add(nextPoint)
                dist[nextPoint.x][nextPoint.y] = curDist + 1
            }
        }
    }

    return dist
}

private fun canMoveInto(point: IntPoint2d): Boolean {
    return point.x in 0 until n
            && point.y in 0 until m
            && map[point.x][point.y] != '#'
}