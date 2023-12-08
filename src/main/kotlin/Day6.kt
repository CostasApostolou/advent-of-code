import kotlin.math.ceil

fun main() {
    println(solvePart1(input))
    println(solvePart2(input))
}

private fun solvePart1(input: String): Long {
    val (times, distances) = input.parseInputForPart1()
    return solveOptimized(times, distances)
}

private fun solvePart2(input: String): Long {
    val (time, distance) = input.parseInputForPart2()
    return solveOptimized(listOf(time), listOf(distance))
}

private fun solveOptimized(times: List<Long>, distances: List<Long>): Long {
    val results = IntArray(times.size)
    times.forEachIndexed { index, time ->
        val distance = distances[index]
        for (first in 1..<time) {
            val second = ceil(distance.toDouble() / first).toLong()
            when {
                first + second > time -> if (results[index] != 0) break else continue
                first + second < time -> results[index]++
                first + second == time -> if (first * second > distance) results[index]++
            }
        }
    }

    return results.fold(1) { acc, i -> acc * i }
}

private fun String.parseInputForPart1(): Pair<List<Long>, List<Long>> {
    val times = lines().parseInputForPart1("Time")
    val distances = lines().parseInputForPart1("Distance")
    return times to distances
}

private fun String.parseInputForPart2(): Pair<Long, Long> {
    val times = lines().parseInputForPart2("Time")
    val distances = lines().parseInputForPart2("Distance")
    return times to distances
}

private fun List<String>.parseInputForPart1(linePrefix: String): List<Long> =
    first { it.startsWith(linePrefix) }
        .substringAfter(":")
        .trim()
        .split("\\s+".toRegex())
        .map { it.trim().toLong() }

private fun List<String>.parseInputForPart2(linePrefix: String): Long =
    first { it.startsWith(linePrefix) }
        .substringAfter(":")
        .filter { it.isDigit() }
        .toLong()


private val sampleInput = """
    Time:      7  15   30
    Distance:  9  40  200
""".trimIndent()

private val input = """
    Time:        51     69     98     78
    Distance:   377   1171   1224   1505
""".trimIndent()