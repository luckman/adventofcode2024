package com.luckman.adventofcode2024.day23

import com.luckman.adventofcode2024.tools.InputReader

var graph: MutableMap<String, HashSet<String>> = HashMap()

fun main() {
    val lines = InputReader().readAllLines()
    for (line in lines) {
        val from = line.split("-")[0]
        val to = line.split("-")[1]
        if (!graph.containsKey(from)) {
            graph[from] = HashSet()
        }
        if (!graph.containsKey(to)) {
            graph[to] = HashSet()
        }
        graph[from]!!.add(to)
        graph[to]!!.add(from)
    }
    println(part2().joinToString(separator = ","))
}

fun part1(): Int {
    var result = 0
    val vertices = graph.keys
    for (v1 in vertices) {
        for (v2 in vertices) {
            if (v2 <= v1) {
                continue
            }
            for (v3 in vertices) {
                if (v3 <= v2) {
                    continue
                }

                if (graph[v1]!!.contains(v2) && graph[v2]!!.contains(v3) && graph[v3]!!.contains(v1)) {
                    if (v1.startsWith("t") || v2.startsWith("t") || v3.startsWith("t")) {
                        result++
                    }
                }
            }
        }
    }
    return result
}

// No idea, why monte-carlo method works well on this graph (maybe on all random generated graphs???)
// but it finds the best answer almost instantly (usually lest than 70 attempts is enough)
// ofc we are not sure that it is the best answer
fun part2(): List<String> {
    val vertices = graph.keys
    var bestSet: Set<String> = emptySet()
    for (test in 0 until 100_000) {
        val curSet = HashSet<String>()
        val first = vertices.random()
        curSet.add(first)

        var newAdded = true
        while (newAdded) {
            newAdded = false
            for (v2 in vertices.shuffled()) {
                if (curSet.contains(v2)) {
                    continue
                }
                var connected = true
                for (v in curSet) {
                    if (!graph[v2]!!.contains(v)) {
                        connected = false
                        break
                    }
                }
                if (connected) {
                    curSet.add(v2)
                    newAdded = true
                    break
                }
            }
        }

        if (curSet.size > bestSet.size) {
            println("Test ${test}. New best size: ${curSet.size}")
            println("New best set: " + curSet.sorted())
            bestSet = curSet
        }
    }

    return bestSet.sorted()
}