package com.luckman.adventofcode2024.day16

import com.luckman.adventofcode2024.tools.InputReader
import com.luckman.adventofcode2024.tools.IntArray3d
import com.luckman.adventofcode2024.tools.IntPoint2d
import com.luckman.adventofcode2024.tools.IntPoint3d
import java.util.*

const val INFINITY = Int.MAX_VALUE / 2
const val POINTS_FOR_ROTATION = 1000
const val POINTS_FOR_MOVING = 1

/*
 -----------> (y)
 |
 | #.#.#.#.#
 | #########
 | #.#.#.#.#
 v
(x)

0 - east
1 - north
2 - west
3 - south
 */
val dx = intArrayOf(0, -1, 0, 1)
val dy = intArrayOf(-1, 0, 1, 0)

fun main() {
    val map = InputReader().readAllLines()
    val n = map.size
    val m = map[0].length

    val state = Array(n) {
        Array(m) {
            Array(4) { // 0 - west, 1 - north, 2 - east, 3 - south
                INFINITY
            }
        }
    }

    var sx = -1
    var sy = -1
    var ex = -1
    var ey = -1

    for (i in 0 until n) {
        for (j in 0 until m) {
            if (map[i][j] == 'S') {
                state[i][j][2] = 0
                sx = i
                sy = j
            }
            if (map[i][j] == 'E') {
                ex = i
                ey = j
            }
        }
    }

    val sPoint = IntPoint3d(sx, sy, 2)
    val queue = LinkedList<IntPoint3d>()
    queue.add(sPoint)

    while (!queue.isEmpty()) {
        val curLocation = queue.poll()
        val curPoints = state[curLocation.x][curLocation.y][curLocation.z]

        // turn left
        var nextLocation = IntPoint3d(curLocation.x, curLocation.y, (curLocation.z + 3) % 4)
        var nextPoints = curPoints + POINTS_FOR_ROTATION
        tryAddToQueueAndUpdateState(state, queue, nextLocation, nextPoints)

        // turn right
        nextLocation = IntPoint3d(curLocation.x, curLocation.y, (curLocation.z + 1) % 4)
        nextPoints = curPoints + POINTS_FOR_ROTATION
        tryAddToQueueAndUpdateState(state, queue, nextLocation, nextPoints)

        // go forward
        val z = curLocation.z
        nextLocation = IntPoint3d(curLocation.x + dx[z], curLocation.y + dy[z], z)
        nextPoints = curPoints + POINTS_FOR_MOVING
        if (nextLocation.x in 0 until n && nextLocation.y in 0 until m
            && map[nextLocation.x][nextLocation.y] != '#'
        ) {
            tryAddToQueueAndUpdateState(state, queue, nextLocation, nextPoints)
        }
    }

    val resultPart1 = state[ex][ey].min()
    println(resultPart1)

    solvePart2(state, IntPoint3d(sx, sy, 2), IntPoint2d(ex, ey))
}

fun solvePart2(state: IntArray3d, startLoc: IntPoint3d, endLoc2d: IntPoint2d) {
    val result1 = state[endLoc2d.x][endLoc2d.y].min()
    val queue = LinkedList<IntPoint3d>()
    for (z in 0 until 4) {
        if (state[endLoc2d.x][endLoc2d.y][z] == result1) {
            queue.add(IntPoint3d(endLoc2d.x, endLoc2d.y, z))
        }
    }

    val visitedPoints = HashSet<IntPoint2d>()

    while (!queue.isEmpty()) {
        val curPoint = queue.poll()
        visitedPoints.add(IntPoint2d(curPoint.x, curPoint.y))

        // go back
        val zReverse = (curPoint.z + 2) % 4
        var prevPoint = IntPoint3d(curPoint.x + dx[zReverse], curPoint.y + dy[zReverse], curPoint.z)
        if (state[prevPoint.x][prevPoint.y][prevPoint.z] == state[curPoint.x][curPoint.y][curPoint.z] - POINTS_FOR_MOVING) {
            queue.add(prevPoint)
        }

        // turn right
        prevPoint = IntPoint3d(curPoint.x, curPoint.y, (curPoint.z + 1) % 4)
        if (state[prevPoint.x][prevPoint.y][prevPoint.z] == state[curPoint.x][curPoint.y][curPoint.z] - POINTS_FOR_ROTATION) {
            queue.add(prevPoint)
        }

        // turn left
        prevPoint = IntPoint3d(curPoint.x, curPoint.y, (curPoint.z + 3) % 4)
        if (state[prevPoint.x][prevPoint.y][prevPoint.z] == state[curPoint.x][curPoint.y][curPoint.z] - POINTS_FOR_ROTATION) {
            queue.add(prevPoint)
        }
    }
    println(visitedPoints.size)
}

private fun tryAddToQueueAndUpdateState(
    state: IntArray3d,
    queue: LinkedList<IntPoint3d>,
    location: IntPoint3d,
    points: Int
) {
    if (state[location.x][location.y][location.z] > points) {
        queue.add(location)
        state[location.x][location.y][location.z] = points
    }
}