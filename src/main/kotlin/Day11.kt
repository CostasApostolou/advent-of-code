import kotlin.math.abs

fun main() {
    println(solvePart1(sampleInput))
    println(solvePart1(input))
}

private fun solvePart1(input: String): Int {
    val cosmos = Cosmos.create(input)
    cosmos.expand()
    cosmos.enumerate()
    return cosmos.sumAllDistances()
}

private data class XY(val x: Int, val y: Int){
    fun distanceFrom(other: XY): Int =
        abs(x - other.x) + abs(y - other.y)
}
private class Cosmos private constructor(private val rows: List<List<String>>) {
    private val backingField = rows.map { it.toMutableList() }.toMutableList()
    private val galaxiesLocations = mutableListOf<XY>()

    fun expand() {
        expandRows()
        expandColumns()
    }

    fun enumerate() {
        var num = 1
        backingField.onEachIndexed { x, row ->
            row.onEachIndexed { y, element ->
                if (element == "#") {
                    galaxiesLocations.add(XY(x, y))
                    row[y] = num.toString()
                    num++
                }
            }
        }
    }

    fun sumAllDistances(): Int =
        galaxiesLocations.mapIndexed { currentIndex, galaxy ->
            galaxiesLocations.mapIndexed { otherIndex, it ->
                if (otherIndex > currentIndex) {
                    galaxy.distanceFrom(it)
                } else {
                    0
                }
            }
        }.sumOf { it.sum() }

    private fun expandRows() {
        val emptyLinesIndexes = rows
            .mapIndexed { index, chars ->
                index to chars.takeUnless { row -> row.any { it != "." } }
            }
            .filter { it.second != null }
            .map { it.first }
        val emptyLine = List(rows.first().size) { "." }
        emptyLinesIndexes.reversed().forEach { backingField.add(it + 1, emptyLine.toMutableList()) }
    }

    private fun expandColumns() {
        val emptyColumnsIndexes = (0..<rows.first().size)
            .map { column ->
                column to rows.map { it[column] }.takeUnless { it.any { it != "." } }
            }
            .filter { it.second != null }
            .map { it.first }
        backingField.forEachIndexed { i, row ->
            emptyColumnsIndexes.reversed().forEach { index -> row.add(index + 1, ".") }
        }
    }

    fun drawCosmos(withNums: Boolean = false) {
        backingField.forEachIndexed { index, row ->
            println("row $index ${row.mapIndexed { i, c -> if (c == "." && withNums) i else c }}")
        }
    }

    override fun toString(): String {
        return backingField.joinToString("\n") { it.joinToString("") }
    }

    companion object {
        fun create(input: String): Cosmos =
            Cosmos(input.lines().map { line -> line.map { it.toString() } })
    }
}

private val sampleInput = """
    ...#......
    .......#..
    #.........
    ..........
    ......#...
    .#........
    .........#
    ..........
    .......#..
    #...#.....
""".trimIndent()

private val input = """
    ...........................#.............#..........#.........#..................#.......#.............................................#....
    ..............#...........................................................................................#...............#.................
    .....................#......................................................................................................................
    ...................................#................................#................................................#....................#.
    .................#.......................................................#..........#..................#.............................#......
    ...............................................................................................#.................#..........................
    ......................................................#........................#........................................#...................
    ........#.....................................................#...........................#.................#...............................
    .........................................#.....#...............................................................................#............
    ...........................#.........................................................#...................................................#..
    .....#................................................................#............................................................#........
    .............#..........................................#.......................................#.................#.........#...............
    ............................................#...............................................................................................
    .#...............#.....................#....................................#.............................#................................#
    ............................#....................#..........................................................................................
    ...................................................................................................#..........#.............................
    .......#............#.........................................#........#.........................................................#..........
    ..............#.................................................................#..........#............................#...................
    ................................#........................................................................................................#..
    ...............................................#.....................................................#...........#..........................
    #...............................................................#...........................................................................
    ............#......#...................................#....................................................#.............#.........#.......
    .......................................................................................#....................................................
    ....................................#........................#......#...........................#...........................................
    .........#.....#.............#............#.....................................#.....................................#.....................
    ..........................................................................................................#.....#...............#..........#
    ........................................................#...................................................................................
    ..........................#.................................................................................................................
    ....#..................................................................#..................................................#.................
    .....................#..........................................#...........................#.........#.....................................
    ...........#.........................#.......................................#......#.................................................#.....
    #...............................................................................................#..................#............#...........
    ............................#.............#.........#.......................................................................................
    ........#...........................................................#.......................................................................
    .........................................................................#..........................#.......................................
    ..#..........#............................................#.....#.....................#...............................#.....................
    .....................................#...............................................................................................#......
    ....................#......................#....................................................................................#...........
    .....#......................................................................................................................................
    ................................#....................#............................#.........................................................
    ........................#......................................................................#.............................#..............
    .......................................................................#....................................................................
    ..............#.................................#......................................................#.......#..................#.......#.
    ............................................................#............................#..................................................
    ..........................................................................................................................#.................
    ..#.................#.........#............................................#.......#........................................................
    .......#.............................................#........................................#.......................#.....................
    ...............................................................#.................................................................#..........
    ...........................#...............#.......................................................#.....................................#..
    .................................#..............#...............................................................#...........................
    ...............#........................................................................#...................................................
    .........................................................#......................................#..........#................................
    ..#......................................#................................#.................................................................
    ...............................#............................................................#..............................................#
    ..........#........#............................................#...............................................................#...........
    ................................................................................#..................#...........#..........#.................
    ..................................#........#................................................................................................
    ............................................................................................................................................
    .....................#............................................#............................#.......#.....................#..............
    ............................#........................#.....................#..........#..........................#.....................#....
    .........................................................................................................................#..................
    .#.........#...........................#.......................#...................................#........................................
    ................#........#.....................................................................................................#..........#.
    ...........................................................................................#.................#..............................
    ...............................#......................................#............................................#........................
    .........................................#.......#......................................................#...................................
    ..........#..................................................#.................#............................................................
    #.......................#..............................................................#...........................................#.......#
    .................................#......................#........................................................#........#.................
    ............................................................................................................................................
    ...............................................#...............................................#............................................
    ................#.....................#.......................................#.............................................................
    ......................................................#...............#.....................................................................
    ...................................................................................#.............................................#..........
    ..................................................................#........#....................................#...........................
    .#.......................................................#.................................#............#...................................
    ............................................................................................................................................
    ........#.......................#................................................#.........................................#................
    ..................#......#......................................................................#...........................................
    ......................................#...............#..........#.....................................................................#....
    ...#....................................................................................#...................................................
    ...............#..............................#.............................#...............................................................
    ...................................#..............................................................#.....#........................#.........#
    .......#............#...................................................................................................#...................
    ...........................#.................................................................#................#.............................
    .....................................................#...............................................#......................................
    ...........#..............................................#.................................................................................
    ..............................................#.................#...............#.........#.................................................
    ................#.......#...........#.................................#...................................#.................................
    .....................................................................................#...........#...................#......................
    ........................................................#......................................................................#.......#....
    .....#......................#...............................................................................................................
    .............#........#..........................#..........................................................#.............#................#
    ............................................................................................................................................
    ........#............................#............................................#..........#..............................................
    ............................................#...................................................................#...........................
    ............................................................................................................................................
    ..#..............#......#.........................................#...............................#.........................................
    ....................................................#......................#.......................................................#......#.
    ...................................#........................#................................................................#..............
    .....................................................................#...........#..........................................................
    ...............#.............................#...........................................................#..................................
    ..........#..............................................................................#.....#.................#..........................
    .....................................................................................................................................#......
    .#................................#.........................................................................#...............................
    ........................#.........................................#......#.........#................#.......................................
    .................#........................................#..................................................................#..............
    ........................................#...................................................................................................
    ....#.....#........................................#............................................#...........................................
    ............................................................................................................................................
    ..............................................................................#.....................................#................#......
    .....................#................................................................#.........................................#...........
    .............................#.........#........#.........#.............#...........................#.......................................
    ................................................................#.........................#..................#............#.............#...
    ........#.......................................................................#...........................................................
    ........................#..................#............................................................#...................................
    .....................................#.................................................#.........#................#.........................
    ...................................................#.....................#....................................................#............#
    ............................................................................................................................................
    ................#..............................................#.................#............................#.............................
    ....#........................#..........................#............#......................................................................
    ....................................#........................................................#..............................................
    .......................#..........................................................................#.....#...........#............#..........
    ........#..................................#................#.............#...........................................................#.....
    ................................................#.......................................................................#..................#
    ..................#.................................................................#.......................................................
    ............#........................#...........................................................................#.............#............
    ...#........................................................................................................................................
    ..........................................#.................................#................#..............................................
    .......................#............................#.............#...................................................#.............#.......
    .................................#....................................................#........................#............................
    .................................................................................#........................................................#.
    ..........................................................#.............................................#...................................
    ...#............#...........................................................................................................................
    ..............................................#...................................................#.........................................
    .........................................................................#.....#.......#.........................................#..........
    ............#.........#......................................................................#........#.....................................
    ...........................#........................................#........................................#.......................#......
    .....#.................................#..............#........#.....................................................#......................
    ...............................#............................................#........#....................................#.................
""".trimIndent()