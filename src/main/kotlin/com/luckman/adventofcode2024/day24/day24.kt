package com.luckman.adventofcode2024.day24

import com.luckman.adventofcode2024.tools.InputReader
import kotlin.random.Random

const val UNDEFINED_VALUE = -1
val graph = Graph()
val reversedGraph = Graph()
val vertexIndexes = HashMap<Vertex, Int>()

fun main() {
    val lines = InputReader().readAllLines()
    for (line in lines) {
        if (line.contains(":")) {
            val split = line.split(": ")
            val vertex = Vertex(split[0], split[1].toInt())
            addVertex(vertex)
        }
        if (line.isEmpty()) {
            continue
        }
        if (line.contains("->")) {
            val split = line.split(" ") // 0 - v1; 1 - operation; 2 - v2; 4 - result vertex
            val v1 = Vertex(split[0], UNDEFINED_VALUE)
            val v2 = Vertex(split[2], UNDEFINED_VALUE)
            val operation = Operation.valueOf(split[1])
            val v3 = Vertex(split[4], UNDEFINED_VALUE)
            if (!vertexIndexes.containsKey(v1)) {
                addVertex(v1)
            }
            if (!vertexIndexes.contains(v2)) {
                addVertex(v2)
            }
            if (!vertexIndexes.contains(v3)) {
                addVertex(v3)
            }
            val idx1 = vertexIndexes[v1]!!
            val idx2 = vertexIndexes[v2]!!
            val idx3 = vertexIndexes[v3]!!
            val edge1 = Edge(operation, v1, v3)
            val edge2 = Edge(operation, v2, v3)
            graph.edges[idx1].add(edge1)
            graph.edges[idx2].add(edge2)
            reversedGraph.edges[idx3].add(edge1.reverse())
            reversedGraph.edges[idx3].add(edge2.reverse())
        }
    }

    println(part1())
}

fun part1(): Long {
    var result = 0L

    for (v in vertexIndexes.keys) {
        if (v.name.startsWith('z')) {
            val vNumber = v.name.replace("z", "").toInt()
            val value = calcValue(v)
            if (value == 1) {
                result = result or (1L shl vNumber)
            }
        }
    }

    return result
}

fun calcValue(v: Vertex): Int {
    val idx = vertexIndexes[v]!!
    return if (reversedGraph.vertices[idx].isValueUndefined()) {
        assert(reversedGraph.edges[idx].size == 2)
        val operation = reversedGraph.edges[idx][0].operation
        val value1 = calcValue(reversedGraph.edges[idx][0].to)
        val value2 = calcValue(reversedGraph.edges[idx][1].to)

        val operationResult = operation.apply(value1, value2)
        reversedGraph.vertices[idx].value = operationResult
        operationResult
    } else {
        reversedGraph.vertices[idx].value
    }
}

fun addVertex(v: Vertex) {
    vertexIndexes[v] = graph.vertices.size
    graph.addVertex(v)
    reversedGraph.addVertex(v)
}

class Graph(
    val vertices: MutableList<Vertex> = ArrayList(),
    val edges: MutableList<MutableList<Edge>> = ArrayList()
) {
    fun addVertex(v: Vertex) {
        vertices.add(v)
        edges.add(ArrayList())
    }
}

class Vertex(
    val name: String,
    var value: Int
) {
    fun isValueUndefined(): Boolean {
        return value == UNDEFINED_VALUE
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vertex

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

data class Edge(
    val operation: Operation,
    val from: Vertex,
    var to: Vertex
) {
    fun reverse(): Edge {
        return Edge(operation, to, from)
    }
}

enum class Operation {
    AND,
    OR,
    XOR;

    fun apply(val1: Int, val2: Int): Int {
        if (this == AND) {
            return val1 and val2
        } else if (this == OR) {
            return val1 or val2
        } else {
            return val1 xor val2
        }
    }
}