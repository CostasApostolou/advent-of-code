fun main() {
    println(solvePart1(sampleInput))
    println(solvePart1(input))
    println(solvePart2(sampleInput))
    println(solvePart2(input))
}

private fun solvePart1(input: String): Int =
    input
        .toTwoDimensionalMatrix()
        .originalToNorth()
        .roll()
        .calcTotalLoad()

private fun solvePart2(input: String, cycles: Int = 1000000000): Int {
    val set = mutableSetOf<List<List<String>>>()
    var loopStart = -1
    var loopEnd = -1
    val original = input.toTwoDimensionalMatrix()
    var matrix = original
    set.add(matrix)
    for (counter in 1..cycles) {
        matrix = matrix.cycle()
        if (!set.add(matrix)) {
            loopStart = set.indexOf(matrix)
            loopEnd = counter - 1
            break
        }
    }
    val loopSize = loopEnd - loopStart + 1
    val mod = (cycles - loopStart) % loopSize
    matrix = set.elementAt(loopStart)
    for (counter in 1.. mod) {
        matrix = matrix.cycle()
    }

    return matrix.originalToNorth().calcTotalLoad()
}
private fun List<List<String>>.calcTotalLoad(): Int =
    sumOf { it.calcRowTotalLoad() }

private fun List<String>.calcRowTotalLoad(): Int =
    mapIndexed { index, s -> if (s == "O") this.size - index else 0}.sum()

private fun List<String>.rollRow(): List<String> {
    val newList = listOf("#") + this + listOf("#")
    return buildString {
        newList
            .findIndicesOf("#")
            .windowed(2)
            .forEach { (from, to) ->
                val zeroes = newList.subList(from, to).count { it == "O" }
                val subListSize = to - from - 1
                append("O".repeat(zeroes))
                append(".".repeat(subListSize - zeroes))
                append("#")
            }
    }
        .map { it.toString() }
        .dropLast(1)
}

private fun List<List<String>>.roll(): List<List<String>> =
    map { it.rollRow() }

private fun List<List<String>>.originalToNorth(): List<List<String>> = transposed()
private fun List<List<String>>.northToWest(): List<List<String>> = transposed()
private fun List<List<String>>.westToSouth(): List<List<String>> = transposed().reversedRows()
private fun List<List<String>>.southToEast(): List<List<String>> = reversedRows().transposed().reversedRows()
private fun List<List<String>>.eastToOriginal(): List<List<String>> = reversedRows()

private fun List<List<String>>.reversedRows(): List<List<String>> = map { it.reversed() }

private fun List<List<String>>.cycle(): List<List<String>> =
    originalToNorth()
        .roll()
        .northToWest()
        .roll()
        .westToSouth()
        .roll()
        .southToEast()
        .roll()
        .eastToOriginal()

private val sampleInput = """
    O....#....
    O.OO#....#
    .....##...
    OO.#O....O
    .O.....O#.
    O.#..O.#.#
    ..O..#O..O
    .......O..
    #....###..
    #OO..#....
""".trimIndent()

private val input = """
.....#...O..O...O.....O#.O.#..O..#...O#.#OOO.O..##O.O##O..O....#..#...O.##..O.....O#..O..O.O..O#...#
O.OO...#OO.OO..#.OO.......O......O#..#O.#.OOO.#O.##.O#OOO...O..#...O#..#.OOO..#...#....O........O.O#
.#.#.O##O...##.#.OO..O.O#..OO.O.OO..O..O#.##O.....#..##.#.##.#..O.O...#.O..#...#.....#....OO#OOOO..#
O..O.O.#.#...O.OOO.O.O..O.O..OO#O...........#O.O.O.....#O..O....#O..#OO.O...O#...O.#..#..#..O...#...
..O.#....OOO..OO....#OOO.#O...#O...O#.........O..#.##...OOO.OO.......OOO........O.##..O.#.O.OO.#O.O.
...###...........#.O#..OO....OO##O.##..O..O...#O#.OO.O.O...#.#OO.#O.#.......#O..#.##O.O....#........
.O...#.#.O.......O.O..O#..#O..#.....O.O...O#.#.O.....#..#.O...#......#..O.#..O###O..O....##.O#.#O...
#..#OO.O#..##..OO..O...#.OOO..##O#...#..#......OO#...O.O..O#.#.#....OO.O...O.....O.OOO.#O.O#O.....#.
OO.O.#.O#.O.####O.O..O..O......#.OOOO#...O....O..#....O......O..OO.O..#.....O..#...O#.O..O##.......O
O..##.#.....##...O.O##....OO..O#..#......O.#.O#..#....##...#..........O.O#O..#O.O...O...O...#O...O.O
.......O#.O.#...O.OO#.OO...O...##OO..OO...#.O....##O..O.O#..#...O#......#.O.#O..##.#OO.O....##O.#.O.
..OO.O.#..#...O.OO...#.#..#O....#.O....O..O..O..O#.##.O..O...O......##.......#.#.....O.#..O..#...O##
.#...#...O..#.O.O......O..O.O...O...O.O.O..OO....OOO.....#..O...O..OO....#..#.O..O.OOO.#..O..#O..O..
O...OO.#...O.O#O..#.#O.#.O.O#.#..#....O#.O....O..O........#..OO.#..#..O.#OO.#.O.......#...#OO.......
.O..#.O..##OO....O.O#O........O..#O.#...O#.O#..O.O..#..#O....O...O#.....O.O.O.###....O#OOOO...#...O.
.....O.....#O.#.O.#..O....O.#..##..O.O#O..O.#O....#..#...#O#..O......#OOOOOO...O....####..#.O...#O..
O...O..#...#O......#....OO...OO.#..#..#...##..#.....O##.#.O.#.O..#.O...#.#..#.OO..O....#...O.#...OOO
.O..O.................O.O##O.....O..O#.O..#...##OOO...O.O#O.OO..........#O.....#....#.#.#..O.#O#...O
#OO.O.O#O#.#......##.#...OO.........#..#.O......O..O.#OO#.#....O.#..O.O..#..#O..OO..#...O.....#OO...
##.........O..OOOO......#.#....#.#.......#O..#.#..O..O.O....O..#...##......O..O.OO..O.......##......
....O.##........#...#.O.#OO#..#.....O.#....O...O.#...........#..#.#O.#.OO#.O...#..O#....##..#.O..O.#
.......O....O.#O...#.#O#..O#..O.#...#O.O..O..............OO#O..#.##.O.#O...O.......OO..O...O....OO..
.......O.OO#..#.....O.....#....O..OO.O.OO..O..###..O..O.#.O##OO....OO.OO.####...O.....O.O....#....O.
####....O.OO..OO.....O..#..#....O.#.O.#.O....#...O.O....#..#.O...#......O...#.#.O.#...O..OO...#.....
.#...##.##O.O#..#.#.OO....O..O###....##O..#..#.O...O...O.OO.....##..O.......O.O#....#.OO#.#...O.#...
#.O.O.OO..O..O..O...O..#........#...O..##O###.O#...O#.....#.....O#O.#..#.#...O......#..O..#..O.O..#.
#......O..#..O.......O..#.OOO...O.#........O...#..OO...O#O....#........##O...#..##..O.#..OOO#..O.#..
.O...OO.O..O#...O....O#..#...............#O...OO........O....OO#.#...O#...O#..OO.O.O.O.#.#O.#O......
..##..O.......##..OOO#..O..O#....OO..O.#...........#...O...#.O...OO.#.O..#.#.......##O##O.OO#..O....
OO...O....#......#.O.#.#..........#..O..O..O...O....O#......O......#.....#..#..OO..O........O.#.#O..
...OO..OO#O.O..O#O.#....O.#.O......O.#...O........O....#.....OO......O..#.OOO........#.O.O...O#....O
.#.#....O.###...##.....O......#.O........OO.O..OO#...O.#....O...##...O.O.#..O..O..O....OOO.#.#O..O.#
O.OO##.O.......O#.O.#O..O..O.....##O....O..#.O..#..#.....O.#O#...##..#O.O..O...OO#O.##.##.O#..#....#
......O..##.....#.#.#OO....#..O....OO..........#...##..........#OO.OO...#.O.OO..O..#O..##..O.O....O#
OO.O.O#.O.#.O#O#...##......O.#O...OO..O...O....#...OO...OO..O....#..OO..#OO..O.O.....OO#..O#..#O..O.
.O..#O#...O####..#...#..#.O...............#....#OO##OO..O.O.#...O.....OO.#OO..O..O.O.O.O..#..O....O#
....#.#.OOO....#......#...O.O..O.###..OO..#..#.........#.O.O.O#.O......O.#..O....#..O.#.......#.....
O#O.#OO#O.O.#O#.O#O...OO#O.O.O....OO....O.#..O....O..O#........#..O#.....#O.O..O#..O......O..O.O...#
.......#O...O....#..O....#O#O...#O#.O#...##.O...O.O.O...O.....O....OO...O..............O.O##.##....#
...O.O........O.##.O.O.O.....O.#O#....O.....#..##...OO....O#O..#..#..O..#O##OO..O.....#..O#...#.#O..
......O.O...O....#..OO.O#....O.O.#.....O.#O..O#...#.#.O...........##.#...OO#O..O......O.........O.O.
..O.O#......OO#.O#O.....#O...OOOO#.#O.#.##O.....O#..#.O.....#.....O#....#OO...#.O....O.....O.....#..
..O.OO.......###OO..#.#..O##OOO.#..O...OO..O#O#....#.......OO.....O.O.O..#...OO...O...O......#..O.O.
OO#.O#O....#O#.O.#....O..O.OO.#..O..#O....##O.#OOO..#....O......OO...O...O.#..OOO.OOO###.........O..
##..##O...O#......O..#O..O..O#O....#........O..##.O.#...#.#......##O##..#.#.##.....O....#O..#.....OO
........#.O#..#...#...O..#..#.O.#O#..#.O..O#......#....O.O.O#O.O....##...##.O..O..#.OOO.........O...
.....O......#.#OOOO#O...OO..#...#......O..O..#O..O#..O.......#.#O....O#.##.O...O..#O.O...#..O.#OO..O
..#.O....O...#...##.##O#.O#.O...OO.#..OO.O..O.O...#.O..O.OO##.......#.OOOO....O.....O......O....#..O
.#..#.#.....OO.###....#.##..O.....#...O#.#.#...OOOO#.#.O..OO.....OO......OO......O.O.OO.#.....#OO.O.
..O#.##..#.OO#..O.O.O...O#..O..#..O.#.#O...#...O.O..#.##...OO##..OO#....OO#.O.O#..O#.O.....O....#.O.
O.O.#..O#..O.#.....#.O.#..O...#..O.O.O....##O.##....O..#OO#OO...#..O.#O........O#.#...#OOO...O#.O...
#O.####..O.#..........O..#.....#..#O.O..OOO#...O.O...##....O...O...#..#....O...#........O.O..O.O..#.
.O..OOO.O.##O..O....#.##OO..O...OO.......#.O#..........#.O..OO#..O...#...O.#..#........#..O.#OOOO..O
.#....#.O#.....O...O.OO.#OO#..O##...O.#.#O..O..O.....O.OOOO.O.#.......OO..#..O.#O.....#..O..OO.#.#.O
................#OO.#......#...#...#...O.......O.....#...#O..O.O.O.##....O.O..O##...#....O.....OO#O#
..O......O#O..O.....O..#O#..##.O....O#.O.............#.O.O#OO.O.....O.#.O....#..O......O..O#.....#O#
O.#..O#.OO..........#.#O.O#OO.....#...OOO....#.O..O.....##..OO###......#....O...#......#O.......#.O.
.#O#.OO.O.O#O...O....OO#.O..#O......OO..O.#.....O..#O..O.#....O.OOO.O.....O.O#.O#O.O.....OO.OO.O.#.#
.O......O..O...O#O.#....O...#..##...OOO#.O.#...#.#....#.##...O..OO.O..O..O.##O#.OO...#.O......O.#..#
.###.#......#..O.....#.O.OO#O...#.####....O.#O..#.........O...#.O...#.##OO...O##...............#....
...#...O............O.....OOO...#........O#.....#....O..##....#....O.O.O.O...O.#....OO..O#.....OOO#O
..........#....O.#.O#O....#O...........#OOO........O#OOO...#...O.#.#....O..O..O....O..O.#.O.....O#..
..OO....O....O#O..O........O.#.#.#.##O.#.#...#.##....#.#.O...O.O..#.#.##..O#.....O.O.O..#.#O#.......
O.O...O...O.........O.#..OO...#...#O.#.#.#..#....OO..OOO....OO..O...O...O#.O#......O...#..#O...O..O.
#...O##....O.#.#.O.#OO...#O..#O#O.O#..#.........OO#..OOO.OO.....#...O......##.......#.OO....O....O#.
..O..##...........#.OO...O..#OO....#O.#...#.O....#.O......#..O.O......##O..O.O#O.O...........OO.#O.O
...O.........#.#.#.##..OO......O.#O.....#.OO..O..#..O.....O..O.#.#OO.....#...#.O...O..#........#....
......O.###...O...#.O....O....O#...O#......OO...O......O..O..O.#.....O..O....###.O.#O#O..O.#.O......
#.OO#..O..#.O........O....O.....O.#OO..#.#...#.##O...#..O..#.....#....#.....O.OO.......O......O.....
O..O........O..O...#..O..#...#.#.....#..O...##O.#....O.....#.....O..#.O....O...O......##.O#..#...O..
...##O.OO...#.#...#.O.....O..#.OO#....O.OO.#.O##....O....O..#....#.O..OOO....OO##OO......OO..#..#...
.O..O#O..#......O....OO.#....O##O#.....###..........##....#O.O.###..#O...O.....OOO#..#....#.#O##.#..
#.O..O....#..#.O.#....#OO....##.O....O.....#...O#....O#OO....##O.O.#.....#O#O#.#O....OO..........#O#
..#OOO..O.O.#.#.O.#.......#.O#....#..##...........O...........#..#O......O...O.#.OO#O.O#....O..O..#.
#......O.O...O....O.##.OO.OO.OOO....OOO...O...#..#.......O.O............O.OOO...#..#.O.#...#O#.....#
...#.O..#..#..##.............O.##..#.#......O.O....#.....#...........O#O#OOO...#.##.#.#OO.#O.#.#.#..
.#..##OO#O.....#..#..#....O.#.#...O..O..#..#....##.O...##..###OO..O.OO...OO#OO....#OOOOO.O#O........
#.#......O..#.#.OO...O.#....O#O.#.#O#........#.....##O.#.O.......##.#O#.OOO..OO.O..OO..O#..OOOO#....
.##.#O.#....O.#...OO.#...OO.O..O#..#..O.O#....OO.OO.#O.O...OO.#.#O#..O..O..O#..#.#.O....###O.#...O.#
..............O.O....##.#.#......O..OO.....#...O.O..O..#..O#...O....O##.O#.....O...#..O...O#.#.O#..O
O...O..O.O....O.O.......#..#...#O.....#...........O..O....#......O#....O..#O....OO.#.##.O.O.O.##.O..
.#...O.O##O.#...#.....O.#...#...##...O#O..#O....O..##.O.O...#.......OO#O......#O.....OO...#O#O##...O
#...##.#O...#..#.O.#O...#......#..#..#.#.....#.#.#.....OOO...###..#..O...#..O#......O.O....O#O.OO..O
..O..O.........#...#.#.O.O.O...#.O....O#OO#..O.#.O.....O.O#O..#.#.##.O#...OO.....##...##...O.##....#
O...#.#O.......##.........O.O.#....O..##.O#...OO.#.O#OO.#.OO#OOO...O.O....O.....#O.#O........O.OO.##
.O......O..OO.O.#.OOO.#O.........#..#O..O###O.O..O##.#..O........O#.....#..#..#.O#..#..#O..........O
O#..O.OO...#...O.#.............##......OOOO#OO.O.O...O...#.....#O.O#.##O...O..........O....O..OO..#.
#.O.O..#O..#..##O.......O.O#OO....#..O...O.#....#............##O......#O.#......O.......##..O....O..
O..O...O#O.OO#....O...O#......##...O..O.#O.....OO.O...##..#.O..O#O#..#O......O#.#.#OO..OOO.O.....O..
..O...O.....O..#..#....OO.O....O.O...O#OO.....OO#.O......O.##.O...O..O.........##...##.O.##.O...O..#
..##...##O.O..O..#.....O.O.O...#......#OO.#OO...O#.O..#.....#...O.#.O#...OO.O....#..#.#O......#..O.O
OO.....#...........O#..O#...O......O.O..#.O#............#..O....OO.O.....O.....OO##O..#.O.#..O...OO.
......O.O.....OO#..O#.O....#.O....#....OO..O...#..#..#.O..O#O...O..O#.O..#.#......#.O...#.....O#...O
O...O...O#OO.#..O..O.OOO.##.#.#..#....#...O.O...O#O...#.......OO...O....###.#..O#...###O.....###.O..
OO#.O..O#...O...O..O..##O........O#O#O....O.O...#.....##...##.#.#.O#..#.O..#......O.#...........O...
.O...O#OO.O#.OOOO..O..O....#...O.O..O#.O.....#O.O.#...OO.O.O#....#O.#....O..OO..##.O...O...O..#....O
..###.....#.#.#.##......##O..OOO....##.O.O##.#..#.#.O.O.#O.....#.#O...#O.O.#..O....#.O....O...OOO...
......O#.##.O..#.O#.#......O.OO..##O#.O.O..#..###..........O..OO..O..O...OO#.......#.O...#..O#...O..
O......#....O...##.O.#..O......OO##.#.O....OO#O....O.O..#O#O##.....###..O..O.O.OOO#O#......O.OO.#O#.
...O...OO..#OO..#...O..#...##...#....O.#.O.O.#..#.###.O#.#.#O..O....#..##..##.OOO...O...O..O#O...#.#
    """.trimIndent()