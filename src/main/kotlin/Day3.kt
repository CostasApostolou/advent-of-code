import kotlin.math.max

fun main() {
    println("123!".toCharArray().takeWhile { it.isDigit() }.joinToString(""))
    val lines = sampleInput.lines()
    val partNumberList = lines.flatMapIndexed { row, line ->
        val partNumbers = mutableListOf<PartNumber>()
        var column = 0
        while (column < line.length) {
            if (line[column].isDigit()) {
                val id = line.toCharArray().takeWhile { it.isDigit() }.joinToString("")
                val end = column + id.length
                val yRange = IntRange(column, end - 1)
                partNumbers.add(PartNumber(line.substring(yRange).toInt(), yRange.map { Coordinate(row, it) }))
                column = end
            } else {
                column++
            }
        }
        partNumbers
    }.onEach { println(it) }
}

private data class Coordinate(val x: Int, val y: Int) {
    private val xRange: IntRange = IntRange(x - 1, x + 1)
    private val yRange: IntRange = IntRange(y - 1, y + 1)

    fun isNeighbour(other: Coordinate): Boolean =
        other.x in xRange && other.y in yRange
}

private data class PartNumber(val number: Int, val coordinates: List<Coordinate>) {
    fun isValid(symbol: Coordinate): Boolean =
        coordinates.any { symbol.isNeighbour(it) }
}

private val sampleInput = """
    467..114..
    ...*......
    ..35..633.
    ......#...
    617*......
    .....+.58.
    ..592.....
    ......755.
    ...${'$'}.*....
    .664.598..
""".trimIndent()