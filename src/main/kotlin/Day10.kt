import jdk.swing.interop.SwingInterOpUtils

fun main() {
    parseMap(sampleInput1)
}

private fun parseMap(input: String): Array<Array<MapElement>> {
    val lines = input.lines()
    val map = Array(lines.size) { row ->
        Array(lines.first().length) { col ->
            MapElement(lines[row][col], Coordinates(row, col))
        }
    }
    val start = map
        .indexOfFirst { row -> row.any { it.char == 'S' } }
        .let { x -> MapElement('F', Coordinates(x, map[x].indexOfFirst { it.char == 'S' })) }

    start.explore(map)

    return map
}

private fun MapElement?.explore(map: Array<Array<MapElement>>) {
    if (this == null) {
        println(null)
        return
    }
    println("current: $this")
    up(map).explore(map)
    down(map).explore(map)
    right(map).explore(map)
    left(map).explore(map)
}

private data class Coordinates(val x: Int, val y: Int)
private data class MapElement(val char: Char, val coordinates: Coordinates) {
    fun up(map: Array<Array<MapElement>>): MapElement? {
        print("up ")
        if (coordinates.x - 1 < 0) return null
        return when (char) {
            'S' -> map[coordinates.x - 1][coordinates.y]
            '|' -> map[coordinates.x - 1][coordinates.y]
            '-' -> null
            'L' -> map[coordinates.x - 1][coordinates.y]
            'J' -> map[coordinates.x - 1][coordinates.y]
            '7' -> null
            'F' -> null
            '.' -> null
            else -> null
        }
    }

    fun down(map: Array<Array<MapElement>>): MapElement? {
        print("down ")
        if (coordinates.x + 1 >= map.size) return null
        return when (char) {
            'S' -> map[coordinates.x + 1][coordinates.y]
            '|' -> map[coordinates.x + 1][coordinates.y]
            '-' -> null
            'L' -> null
            'J' -> null
            '7' -> map[coordinates.x + 1][coordinates.y]
            'F' -> map[coordinates.x + 1][coordinates.y]
            '.' -> null
            else -> null
        }
    }

    fun right(map: Array<Array<MapElement>>): MapElement? {
        print("right ")
        if (coordinates.y + 1 >= map[coordinates.x].size) return null
        return when (char) {
            'S' -> map[coordinates.x][coordinates.y + 1]
            '|' -> null
            '-' -> map[coordinates.x][coordinates.y + 1]
            'L' -> map[coordinates.x][coordinates.y + 1]
            'J' -> null
            '7' -> null
            'F' -> map[coordinates.x][coordinates.y + 1]
            '.' -> null
            else -> null
        }
    }

    fun left(map: Array<Array<MapElement>>): MapElement? {
        print("left ")
        if (coordinates.y - 1 < 0) return null
        return when (char) {
            'S' -> map[coordinates.x][coordinates.y - 1]
            '|' -> null
            '-' -> map[coordinates.x][coordinates.y - 1]
            'L' -> null
            'J' -> map[coordinates.x][coordinates.y - 1]
            '7' -> map[coordinates.x][coordinates.y - 1]
            'F' -> null
            '.' -> null
            else -> null
        }
    }

    fun nextFrom(direction: Direction): Coordinates? {
        return when (direction) {
            Direction.UP -> transitionFromAbove()
            Direction.DOWN -> transitionFromBelow()
            Direction.RIGHT -> transitionFromLeft()
            Direction.LEFT -> transitionFromRight()
        }
    }

    private fun transitionFromAbove(): Coordinates? =
        when (char) {
            '|' -> Coordinates(coordinates.x + 1, coordinates.y)
            '-' -> null
            'L' -> Coordinates(coordinates.x, coordinates.y + 1)
            'J' -> Coordinates(coordinates.x, coordinates.y - 1)
            '7' -> null
            'F' -> null
            '.' -> null
            else -> null
        }

    private fun transitionFromBelow(): Coordinates? =
        when (char) {
            '|' -> Coordinates(coordinates.x - 1, coordinates.y)
            '-' -> null
            'L' -> null
            'J' -> null
            '7' -> Coordinates(coordinates.x, coordinates.y - 1)
            'F' -> Coordinates(coordinates.x, coordinates.y + 1)
            '.' -> null
            else -> null
        }

    private fun transitionFromLeft(): Coordinates? =
        when (char) {
            '|' -> null
            '-' -> Coordinates(coordinates.x, coordinates.y + 1)
            'L' -> null
            'J' -> Coordinates(coordinates.x - 1, coordinates.y)
            '7' -> Coordinates(coordinates.x + 1, coordinates.y)
            'F' -> null
            '.' -> null
            else -> null
        }

    private fun transitionFromRight(): Coordinates? =
        when (char) {
            '|' -> null
            '-' -> Coordinates(coordinates.x, coordinates.y - 1)
            'L' -> Coordinates(coordinates.x - 1, coordinates.y)
            'J' -> null
            '7' -> null
            'F' -> Coordinates(coordinates.x + 1, coordinates.y)
            '.' -> null
            else -> null
        }
}

private enum class Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT
}

private val sampleInput1 = """
    -L|F7
    7S-7|
    L|7||
    -L-J|
    L|-JF
""".trimIndent()