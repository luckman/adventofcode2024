package com.luckman.adventofcode2024.day18

import com.luckman.adventofcode2024.tools.InputReader
import com.luckman.adventofcode2024.tools.IntPoint2d
import java.util.LinkedList

var bytes: List<IntPoint2d> = emptyList()
const val n = 71
const val INFINITY = Int.MAX_VALUE / 2
val map = Array(n) { Array(n) { 0 } }
val dx = intArrayOf(0, -1, 0, 1)
val dy = intArrayOf(-1, 0, 1, 0)

fun main() {
    bytes = InputReader().readAsPoint2dWithCommas()


//    println(part1(1024))
    println(part2())
}

fun part2(): IntPoint2d {
    var l = 0
    var r = bytes.size

    while (r - l > 1) {
        val mid = (l + r) / 2
        if (part1(mid) < INFINITY) {
            l = mid
        } else {
            r = mid
        }
    }
    // map is filled exclusive in part1(), so the answer is on index l, not r
    return bytes[l]
}

fun part1(byteLength: Int): Int {
    for (i in 0 until n) {
        map[i].fill(0)
    }
    for (i in 0 until byteLength) {
        map[bytes[i].x][bytes[i].y] = 1
    }
    val dist = Array(n) { Array(n) { INFINITY } }
    dist[0][0] = 0
    val queue = LinkedList<IntPoint2d>()
    queue.add(IntPoint2d(0, 0))
    while (!queue.isEmpty()) {
        val curPoint = queue.poll()
        val curDist = dist[curPoint.x][curPoint.y]
        for (k in dx.indices) {
            val nextPoint = IntPoint2d(curPoint.x + dx[k], curPoint.y + dy[k])
            if (canMoveInto(nextPoint) && curDist + 1 < dist[nextPoint.x][nextPoint.y]) {
                dist[nextPoint.x][nextPoint.y] = curDist + 1
                queue.add(nextPoint)
            }
        }
    }
    return dist[n - 1][n - 1]
}

private fun canMoveInto(point: IntPoint2d): Boolean {
    return point.x in 0 until n
            && point.y in 0 until n
            && map[point.x][point.y] == 0
}