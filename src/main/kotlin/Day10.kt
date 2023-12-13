fun main() {
    println(solvePart1(sampleInput1.toMap()))
    println(solvePart1(sampleInput2.toMap()))
    println(solvePart1(input.toMap()))
}

private fun solvePart1(map: Map): Int {
    val start = map.start
    val distances = IntArray(4) { 0 }
    var paths = listOf(
        map.at(start.coordinates.up)?.let { ElementsPair(start, it) },
        map.at(start.coordinates.right)?.let { ElementsPair(start, it) },
        map.at(start.coordinates.down)?.let { ElementsPair(start, it) },
        map.at(start.coordinates.left)?.let { ElementsPair(start, it) },
    )
//    paths.forEachIndexed { index, elementsPair -> println("$index: $elementsPair") }
    while (!paths.haveReachedFinish()) {
        paths.forEachIndexed { index, elementsPair -> if (elementsPair != null) distances[index]++ }
//        println(distances.toList())
//        println()
        paths = paths.map { it?.next(map) }
//        paths.forEachIndexed { index, elementsPair -> println("$index: $elementsPair") }
    }
    return distances.max() + 1
}

private fun String.toMap(): Map {
    val lines = lines()
    val map = Map(
        Array(lines.size) { row ->
            Array(lines.first().length) { col ->
                MapElement(lines[row][col], Coordinates(row, col))
            }
        }
    )
    return map
}

private fun List<ElementsPair?>.haveReachedFinish(): Boolean {
    val (p0, p1, p2, p3) = map { it?.cur }
    return (p0 == null || p0 == p1 || p0 == p2 || p0 == p3)
        && (p1 == null || p1 == p0 || p1 == p2 || p1 == p3)
        && (p2 == null || p2 == p0 || p2 == p1 || p2 == p3)
        && (p3 == null || p3 == p0 || p3 == p1 || p3 == p2)
}


private enum class Movement {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT,
    TOP_TO_BOTTOM,
    BOTTOM_TO_TOP
}

private data class ElementsPair(val prev: MapElement, val cur: MapElement) {
    fun next(map: Map): ElementsPair? {
        val movement = when {
            prev.coordinates.x == cur.coordinates.x && prev.coordinates.y == cur.coordinates.y - 1 -> Movement.LEFT_TO_RIGHT
            prev.coordinates.x == cur.coordinates.x && prev.coordinates.y == cur.coordinates.y + 1 -> Movement.RIGHT_TO_LEFT
            prev.coordinates.x == cur.coordinates.x - 1 && prev.coordinates.y == cur.coordinates.y -> Movement.TOP_TO_BOTTOM
            prev.coordinates.x == cur.coordinates.x + 1 && prev.coordinates.y == cur.coordinates.y -> Movement.BOTTOM_TO_TOP
            else -> error("")
        }

        return cur.move(movement, map)?.let { next -> ElementsPair(cur, next) }
    }
}

private data class Coordinates(val x: Int, val y: Int) {
    val up: Coordinates
        get() = Coordinates(x - 1, y)
    val down: Coordinates
        get() = Coordinates(x + 1, y)
    val left: Coordinates
        get() = Coordinates(x, y - 1)
    val right: Coordinates
        get() = Coordinates(x, y + 1)
}

private data class MapElement(val char: Char, val coordinates: Coordinates) {
    fun move(movement: Movement, map: Map): MapElement? =
        when (movement) {
            Movement.LEFT_TO_RIGHT -> transitionFromLeft(map)
            Movement.RIGHT_TO_LEFT -> transitionFromRight(map)
            Movement.TOP_TO_BOTTOM -> transitionFromAbove(map)
            Movement.BOTTOM_TO_TOP -> transitionFromBelow(map)
        }

    private fun transitionFromAbove(map: Map): MapElement? =
        when (char) {
            '|' -> map.at(Coordinates(coordinates.x + 1, coordinates.y))
            '-' -> null
            'L' -> map.at(Coordinates(coordinates.x, coordinates.y + 1))
            'J' -> map.at(Coordinates(coordinates.x, coordinates.y - 1))
            '7' -> null
            'F' -> null
            '.' -> null
            else -> null
        }

    private fun transitionFromBelow(map: Map): MapElement? =
        when (char) {
            '|' -> map.at(Coordinates(coordinates.x - 1, coordinates.y))
            '-' -> null
            'L' -> null
            'J' -> null
            '7' -> map.at(Coordinates(coordinates.x, coordinates.y - 1))
            'F' -> map.at(Coordinates(coordinates.x, coordinates.y + 1))
            '.' -> null
            else -> null
        }

    private fun transitionFromLeft(map: Map): MapElement? =
        when (char) {
            '|' -> null
            '-' -> map.at(Coordinates(coordinates.x, coordinates.y + 1))
            'L' -> null
            'J' -> map.at(Coordinates(coordinates.x - 1, coordinates.y))
            '7' -> map.at(Coordinates(coordinates.x + 1, coordinates.y))
            'F' -> null
            '.' -> null
            else -> null
        }

    private fun transitionFromRight(map: Map): MapElement? =
        when (char) {
            '|' -> null
            '-' -> map.at(Coordinates(coordinates.x, coordinates.y - 1))
            'L' -> map.at(Coordinates(coordinates.x - 1, coordinates.y))
            'J' -> null
            '7' -> null
            'F' -> map.at(Coordinates(coordinates.x + 1, coordinates.y))
            '.' -> null
            else -> null
        }
}

private class Map(private val map: Array<Array<MapElement>>) {
    private val numOrRows = map.size
    private val numOrColumns = map.first().size

    val start: MapElement
        get() = map
            .indexOfFirst { row -> row.any { it.char == 'S' } }
            .let { x -> MapElement('S', Coordinates(x, map[x].indexOfFirst { it.char == 'S' })) }

    fun at(coordinates: Coordinates): MapElement? =
        if (coordinates.x in (0..<numOrRows) && coordinates.y in (0..numOrColumns)) {
            map[coordinates.x][coordinates.y]
        } else {
            null
        }
}

private val sampleInput1 = """
    -L|F7
    7S-7|
    L|7||
    -L-J|
    L|-JF
""".trimIndent()

private val sampleInput2 = """
    7-F7-
    .FJ|7
    SJLL7
    |F--J
    LJ.LJ
""".trimIndent()

private val input = """
    .7--77|.|-|77F|.J.|.FF.J-|7F--7F7.J----7FJ77J..F77-77-F.F|7-|F-.|F777FF7|-.-|-7|..FFL---L7-7FL7L-|-77.FL-7FF-7FJF|-7|7-FF|-|.J-FLJ7.F7.F7--L
    L||.L-7FJF|7L--|F||.777L||F7J.LJJ7JLFJL7J|LF-FJ.77||F.LF|J7FF|L-|||L7-F-L7FLJL-JFFFJ..J7|.FL-7-|J|JF|-|J.L7F7|7.|J7|L7J|7F7...7LJFF7JJF7.|F|
    FLL|7-F7.JLF-77|FLF.L777L7LJ|||LL|J.|--L.|.|-L|-|JFJ7..FJLLFL7J||J|J|-|J|L|LJ----7J-F--7L--7L--77L7LJ7JJ|7LL7LL-JFFL-L-L7LJ7F7|..|LL-7LJFJ-7
    |L|||FJ|-LF-7L7L-7.F7F77.J7.7|L--|.L|J.||.FJ7--.F7LJ|FLJ.F|LLJJF|--7L7J7F|FJ-F|.-J.F7FF7J.|FJF||--7JFFJF|F|JL-|JFJJ7FJL|||LJJFJF7|.J.L--J7F|
    |-F-F|J||F7J-|JF||.|FJ|F7.F-7-|.F.-7|7F||-F7-77-77-7J7F-L77JF..7JJL7J|LF-7J..|J.|7FLJ-LJ|F-7-7-JFFL-L|-|-LJ-|-|.|LJLF7|LF-|F7-|L.L7--7LJLF-J
    |7L-|7FLL7JF|L7FLFJFL7|||F|FJF77|F7F-J.FF-|JLL-7J|-L-J-LL7J-7.FFJ|.|.77L||.L-J7FF-J-|.LF7JLF7JLLFL-L7||L-L7L--J--LF-|7F-77L||7|..L|FL7|FF-||
    F77JLL--LL-L7|-LJ|7F-||||FJ|FJL7-J-|L|FFJ-LF-J7L-77LL-L|J||F7-7J.---J.F7FJ|J|FFF|.|.|-F|--L|L77.|J.|LJ-L7|FF7..FJFL-|JL|.L-LL-|7F.LF7LF7|7L7
    -7|-7..7.|-L-J7F-F---JLJ|L7LJF-J7|FF7F7LF7-JJ.JJ7L|77F-77.FJ|-JF-.7|L-F-77|7|-L7JJJ7|.F7J.F|FJL-.|F|.|..LFF7-FF.-L7.7FF|7|FF7.7-77.|J-FJFLL7
    .L|7.7FL77L|JFJ-L|F7F-7FJFJF-JJ-F7FJLJ|F||.||F7FL-JLF|.L7-L7|.L7|F-L-.L7|-7FF7LL-|7F7F|||F-JL777---F--7|LFL7-LJ-7|L-7-FJL--FLJ|||F7J7FJ.LJ-F
    FLFF-L7F7JF-F|7J|LJ||FJL7L7L---7||L--7|FJ|F7F777J.|FJ|FF|JFJ|F7-L-L|J.|||.LFLJLLFF-J|FJL7L--7|F77-|F7LFJ-L|||-F7F|J-|-7J.LJF-LL7L|.|-LJL-L7J
    LL-..|FJ--7L|.J.LJLLJ|F7L-JF---J||F-7|||FJ||||77JF7LF7F--7L7|||7LL-F77FJ|FF7J|.LFL7FJ|F-J7LFJLJ|F7FJL--77J|F7.L-FJL-L7||7J|L7F|7.7JL-JFF-.|7
    .|-FFFJL|LJF--7F|FJ-FJ|L--7|F7LFJ|L7||LJ|FJLJL7F7F7FJ|L7FJF|||L7.FFJL7L7|FJ|F-7F7FJ|FJL7F7|L7F-J||L-7F-J7J|LJ-7L||F|FJ7-F7-.LFLF7.LFJF-|-FF-
    J.LL-J|JL7L|J|FF7J-|L-JF7FJLJ|FJFJFJ|L-7||F--7||LJ|L7|FJ|F7|||FJF7L-7|FJ||FJL7|||L7||F-J||F7|L7FJL--JL--77LF.FF-7-LJJLJ-7J-J.|L.F|F7.|L|-J||
    J7.|J|F-||F..-F|L----7FJ||F--JL7L7L7|F7|LJL-7LJL7FJFJ|L7||LJLJL7||F-J|L7||L7FJLJL-JLJ|F7|||LJFJL7F------JFF77F-77|JF||LFJJ77F-7F-F-J|J7L.JJ7
    .L7JL|7-J-|.-.LL----7|L7|||F-7FJFJFJLJ|L7F--JF7J||JL7L-J|L---7FJ|||F-JFJLJFJL---7F---J|LJLJF7|F-JL-7LF7|LFJL7|FJF7F--7L|LFF-|LJLL|J7J.FJJ7F7
    7-J7.J||L.L7.F.L|FF7||FJLJLJFJL7L7L-7FJ||L-7FJL7||F7|F--JLF7FJL7|LJ|F7L7F7|F----JL--7FJF---JLJL-7F-JFJL7FJF-J7LL|||F-J--FF7F|7-F--JL--||-J--
    ||-7.|L7JLLJ77-F--JLJLJF7F--JF7L7L7FJL7FJF-J|F7||||LJ|-F7FJ||F-JL7FJ|L7LJ||L---7F7F7|L7L-7F7F7F7|L7FJF-J|FJF77.FJ|||F77LJ|LLJFFL-JL7|FF|7J||
    -.77F7JJL|FJL7-L-------J||LF7|L7|FJL7FJL7L-7||LJ||L-7L7||L7LJL7-FJL7|FJF7||F--7LJ||LJ|L-7||||LJ||FJ|FJF7|L7||7FL7LJLJ|77LLJ7FJ.|.FFJ-7-|7.--
    LJ---L-F7L7J--7F-7F-----JL-JLJFJ||F7||F7L7FJ|L77||F7|FJ||-L-7FJFJF-J|L7|LJLJF7|F-JL7-F7FJLJ||F7||L-J|J||L7||L7FFJF--7|777FLF77|7.LJ-F7L-77F|
    .|J7F|JL7||.77FL7|L-----7F-7F-J||LJ||||L7|L7|FJFJLJLJL7|L--7|L7L7L-7|FJ|F--7|LJL-7FJFJ||F--J||LJ|F--JFJ|FJ|L7|FJFJF7LJJL--7LL-L77.|.FJFL-LJJ
    -|JFL7F--7-FJF-7|L-----7LJ-||FF7L7FJ|||FJL7LJL7L-----7||F--J|FJFJF-J|L7LJF7|L7F-7||.L7|||F-7||F7|L7F7L7|L7L7||L7||||F7..J.F||.||L.|.|-JJ.L-|
    .LJ|L7J.F|.|7L7LJF7F--7L7F-JL7||F||FJLJ|F-JF7FJFF-7F-JLJL7F7|L7L7L-7L7|F7|LJFJL7||L7FJLJLJFJ|||||FJ|L7|L7|FJ||FJL-JLJ|7F.FF7-7L||F7.|.LF7.F7
    ..FJJJ.F7.J7-LL--JLJJLL7|L-7FJ|L-J|L7F-JL7FJLJF7|FJL-7F7FJ|LJFJFJF7|FJ|||L7FJF7||L7|L---7FJFJ|||||FJFJL7LJL7|||F-----JF777.LLL-|LJ7--F--|-|J
    FL|LL.L--7.|-LF---7J.F-JL-7|L7L--7|-|L-7L|L-7FJ||L7F7LJ|L7|F-JFJFJLJ|FJ||FJL7||||FJL7F-7|L7L7||||LJFJF-JF-7LJLJL7F-7JFJL7J.7FF-7JFF.||.F7J|.
    7-J.L7LF7LF|7-L--7|F7L---7LJFJF7FJL-JF-JFJF7|L7||FJ||F7L7|||LFJFJ7|FJ|FJ|L-7|||||L-7|L7|L7|FJLJ||F-J.L--J-L-7F--J|FJFJF-J7FL|JF|F7|.JJF7.L|.
    J.LFJ|F|---LF----J|||F7F-JF7L7||L-7F7L-7|FJLJ7||||FJLJL-JLJ|FJFJF7FJFJL7|F7||SLJL7FJ|FJ|FJ|L-7FJ|L-7F--7F7F-JL-7FJL7|FJFF77J|FF||LJ77F--7||7
    LF.7.LFJJ|-F|F---7LJLJ||F7||FJ||F7LJ|F-J|L--7FJ|||L--7F--7FJ|FJFJ||FJJFJLJ|||L--7|L7|L7|L7L7FJL7|F-JL7FJ||L-7F-JL7FJ||F7||JFF7F7J7LF|JJLF-|7
    7-JJ7-|.|F7FLJF7FJF--7|LJLJ|L-J||L--J|F7|F--JL7LJL-7-LJF-J|FJL7|FJ||F7L-7FJ||F77|L7||FJ|FJFJ|F7LJL7F7|L7||FFJL-7FJ|FJLJLJ|F7|LJ|L7..|JFJJ7L.
    |L7L7F-L-LF---J|L-JF-J|FF7-L7F-JL---7LJLJ|F7F-JF---JF7LL-7LJF-J||FJ|||-FJ|.||||FJFJ|||FJ|FJ|||L---J||L7|||FJF--JL7||F----J|||F-J.L-F7-7J-F-J
    7.F7|F7|J|L-7F7L-7.L-7|FJL--J||F-7F-JF---J||L-7L7|F7|L-7FJF-J7FJ|L7|||FJFJFJLJ||FJFJLJL7||.FJL---7FJL7|||||FJF7F7||||F7F-7|LJL--7JFJ|FL7L|-J
    L---|J|JF-LFLJL-7|F--JLJF-7F7L7L7|L-7L--7FJL7FJFJFJ||F-JL7L--7L7L-JLJ|L7L7L--7||L7|F--7||L7L7F---J|F-J|LJLJ|F|LJLJLJLJLJFJL7F---JFJFJ-LJ.|.7
    FLJ-J7..|J.F---7|LJF-7F7|FJ|L7L7|L7F|F--JL-7|L7|.L7||L-7L|F--J|L--7F-JJ|FJF--J||FJLJF7|LJFJFJL-7F7|L7FJF---JFJF7F7F7F7F-JF7||F---JFJJ-J.-F7J
    L7J7..L-7-FJF-7|L-7|-LJ||L-J.L7LJFJFJL-7F7FJ|FJL-7|||F-JFJL-7-F7|FJL7F7||FL-7FJ||LF-J|L7FJJ|F--J|||FJ|FJF-7FJFJLJ|||||L--JLJ||F7F7|J|FF-F7||
    .JL|.|.LL.L-JFJL--J|F--J|F---7|F7L7L7F-J||L7|L7F-J||||F7L--7L7||FJF-J|||L7F7|L7|L7|F7L7|L7FJL7F7|LJ|FJL7|FJ|FJ-F7LJ|||F7F-7FJ||LJ|||FJJ.LLJ7
    L-7J-L||.FJJFJF-7F-JL7F7||F-7|||L7L7||F7||-||J||F7|||||||F7|FJ|||FJF7|||FJ||L7|L7|||L-J|FJL7FJ||L7FJ|F-J|L-JL--JL-7LJLJLJ7LJFJL7FLJ-F7.F.|LF
    |FL7|F77-F7FL7L7LJFF7LJLJLJFJ|LJFL7LJ||LJL7|L-J||LJ||LJ|FJ|||FJ|||FJ||||L7||FJ|FJ||L-7FJL7FJ|FJ|FJ|FJL7FJF--------J.F---7F7FJF7L-7J..7J-L|-J
    -FJF-JFJ.|J7LL-JF--JL--7F-7L7L7JF7L77LJF77|L--7|L7FJL7FJL7LJ||FJ|||7|||L7LJ||-||FJ|F7||7.|L7|L7LJFJ|F7||FJF7F--7F7F7|F--J|LJFJL--J.-JJ-||L|7
    F|-|J..FJJ.-7.|LL-----7|L7|-L7L-JL-JF-7|L-JF7FJL-J|F7|L7FJF-J|L7||L7||L7L-7LJFJ||FJ|LJL7FJFJL7L-7L7||LJ|L-JLJF7||||||L7F-JF7L--7F7-L||FFJF-J
    FJ.L----.LF-|7FF7F7F--JL7||F7L------JFJL7F7||L---7||||FJL7L7FJFJ||FJ|L7L7FJF-JFJLJFJF7FJ|FJJFJF7|FJ|L7FJ|F7F-JLJ|||||FJ|F-JL7F7LJL7JLJ7LL7L7
    7--|7.|.FFLFJ-FJLJ|L---7||||L--------JF7LJ|||F7F-JLJ||L7FJFJL7L7||||L7L7LJJL7FJF--JFJ|L7|L-7|FJ||L7|FJ|F-JLJF7F7|||||L-JL77FJ|L7F7|.7.F-L7F7
    JJFF7FF|-F.L-FL--7L----JLJ|L-------7F7||F7|||||L7F--JL7|L7|F7L7LJ||F-JFJ7F--JL7L--7|-L7|L--J|L7LJFJ|L-JL----JLJLJLJLJF7F7L7L-JFJ|LJJJFJ7.L-7
    |L-J|F7|F|.L-F---JF------7|F7JF7F--J||||||||LJL7|L-7F7||FJ|||FJF-J|L-7|F7|F7F7L-7FJL7FJL---7L7L-7L-JF--7F----7F-7F-7FJ||L7L--7L-JJ.|-FLL7LJ.
    FF7LJ-L|JL-7-L---7|F-----JLJL7||L---JLJLJ|LJF--JL-7||||||FJ|||7L-7L-7|||LJ|||L7FJL7FJL7F-7FJJ|F7|7F7L-7|L---7|L7||L|L7|L7|F--JF7LLL|.F7L-7|.
    LJ7.|FJ.J-J|F|F--J|L---7F-7F7|||-F--7F7F7L-7L--7F-JLJ||LJL7||L-7FJF7|LJ|F-J||FJ|F7||F7||||L-7||||FJL--JL----JL7||L7L-J|FJ|L7F-J|LL-|FL-JF-7.
    |.FL-J|F|J.L|-L7F-JF7F7LJFLJ|LJL-JF7LJLJL7FJF-7|L--77LJF--J||F7|L7||L-7|L7-LJL7||||||LJL7L7FJ||||L---------7F7LJL7L--7LJ7L7LJF7|7.F.L7|.F-JJ
    J7-77LL7L-||L-FJ|F-JLJL-----JF7F--JL---7FJL-JFJ|F7FJF7.L7F7|||||FJ||F-JL7|F---J||LJ|L7F-JFJL7||||F7F-------J||F7|L---JF--7|F-J|L--777F-..F|J
    F|F-7||7|FJ-|FL-JL-7F-7F--7F7|LJF-----7||F7F-JJ|||L-JL-7LJ||LJ||L7||L--7LJL---7|L-7L7|L-7L7FJLJLJ||L--7F---7|LJL---77FJF7||L-7|F7FJLF|.FF7J|
    FJ|F7JJF7JJLFF-----JL7||F-J|LJF7L--7F7LJ|||L--7|||F-7F-J7FJL7FJL7||L7F7L7F7F--J|F-JFJ|F-JFJL-7F--JL---J|F--J|F-7F-7L-JFJLJ|F-JLJLJJL|L|7F7.L
    |F|-||-L|LJ|LL-----7FJLJL--J-FJ|F--J||F7LJL---J|||L7LJJF7L7FJL7FJ||FJ||FJ||L---JL7FJF|L-7|F7FJL--------JL---JL7|L7|F--JF7|LJF7|F7-LF-J.7-|F|
    L|JL|J--F7L||F7-F--J|F7F----7L7|L---JLJL-----7FJ|L7|7F-JL7||F-JL7||L7||L-JL---7F-JL7FJF7|||LJF7F7-F---7F--7F7FJL-JLJF7FJL-7J|L-J|7.|J-F|.F|.
    |||.|.LLJJ.|7|L7L---J||L---7L-JL-7F----7F----JL-J.|L-JF--JLJL7F7|LJFJ||F7F-7F-JL7F7|L7|||||F-JLJ|FJF-7LJF7LJLJFF--7FJLJF--JFJF--J|F||L-JF|LJ
    LJLL--FJLLF-7L7|FF---JL7F7FL7F7F7|L---7|L----7F7F7L--7L-----7LJ||-FJFJLJ||FJL--7LJ|L7||||LJL---7|L7|FJF7||F---7L7FJ|F--JF--JFJJF777F7FJ-LJ||
    |L7JL-77.FJFJJ|L7L----7|||F7LJLJ|L----JL--7F7LJLJL7F7L-7F---J7-LJFJFJF--J|L7F7FJF-JFJLJ||F-----JL-J|L-JLJLJF--JFJ|FJL---JF-7L--JL-77||F7||F7
    L7|.FJ|-FL7|F7L7|F-7F-JLJLJ|-F7-L------7F7LJ|F-7F7LJL-7|L----7F--JFJLL--7L7||LJFL-7L--7|||F7F7F7F--JF---7F-J-F7L7||F-----JJL7F7F7FJL7L-7J7|F
    LF7-JJ|FF-JLJL-J|L7|L7F---7L-J|F7F--7F7LJL-7||FLJL-7F-JL-----JL--7L-7-F-JFJ|L-7F7FJF--JLJLJLJLJ||F-7L--7|L-7FJ|FJ|||F7F---7|LJ||LJJFJLL7.F-|
    FL||F||LL-7F-7F7L-JL7LJ-F7L7F7LJ|L-7LJL7F--JLJF7F7FJL-7F----7.F--JF7|FL7FJJ|F-J||L-JF7|F-7-F7F7LJL7L---JL--J|FJL7LJLJLJF-7L-7|LJ.|.|.|LJFJ7.
    -JLL7FF7|-LJFJ|L---7|F7FJL-J||F7L-7L-7FJL-7F7||LJ|L7F7||F--7L7L--7|||7FLJLFJ|F-JL--7||FJFJFJLJL---JF7F7F--7L||7FJF7F-7FJFL7FJ7F-77-F--77|L|7
    .-FL||LFJ.F7L-JF7LFJLJLJF-7FJLJL-7L--JL---J|L-JF7L7||LJ|L-7|FJF--J||L77LLJL7||F7F-7LJ|L7L7L-7F-7F7FJLJ||F-JFJL7|FJLJ-LJF-7LJF7L7L7F7-|-JJ-|J
    JF-7L7.||F|L---JL-JF7F--JFJL7F---JF7F7F----JF7FJL7|LJF7|F-JLJJL7F7|L7|..FF-J|||||FJF7L7L7L-7LJJLJ||.F7LJL--JF7LJL--7F--JFJF-JL-JFJF--7-J.-J.
    |-L-|----FJF----7F7|||F--JF-JL----JLJ|L7F---JLJF7LJF7|LJL--7|JL||LJ-LJ7.-L7FJ||LJ|FJL7L-JF7L--7F7LJFJL-77F-7|L7F7F-JL7F-JFL7F---JFJF-JF-F7.7
    FF7|J|-JFL7||F--J|||||L--7|F7-F7-F--7L-JL------JL7-||L7F-7FJ7-FLJJJ|.L77|FLJLLJF7LJF7|F7FJL7F7LJL--JF--JFJFJL7LJ||F7FJL-7F7|L7F7FJFJ..|FJFJ7
    -JF7.F.F-|LJFJF-7|||LJ7F7LJ||FJL7L-7L-----7F7F--7L-JL-JL7LJ|J.LJ7|.J.7L7-LJ|FF-JL--JLJ|||F7LJL-7F7F7L--7L7L--J|FJLJLJF--J|LJFJ|LJFJL-7L-LL7|
    |FLJFJ-F.F--JFJFJ|||F--JL--JLJF7L--JJF7F-7LJ|L-7|F7F7F-7L---7-7|7.L7.|J|7L-J-L----7F7FJLJ|L----J|||L7F7L7L--7F7L-7F-7L---JF-JFJF7L-7.|.FF--J
    77|-LJ||7L7F7|LL-JLJL--7F-7F--JL-7F7FJLJFJF7L--JLJ|||L7L--7FJ.F|F-|J-FFJ7-JJF|-F--J|LJ-F7L-----7|LJL||L7L---J||JFJL7L7F--7L--JFJ|F-JJJFL7--J
    LJ7|.F7.|.LJLJF-7F7F7-FJ|FJ|F--7FLJ||F--JJ|L----7JLJL7L--7LJJ-J.JFLF-J|L|7|FL-7L---JF7FJL-7F7F7||-F7LJ.|F----JL7L-7L7|L-7L----JFJ|JJ7J|L7--7
    LJL|7LJF|7JFF7L7LJLJ|FJFJL-JL-7|F-7LJL----JF----JF7F7L--7L77|-FLFJ7L.F|7LLJFLLF-----J|L--7LJLJ|||FJL--7LJF7F7F-JFFJFJL--JF----7L-JFFJ7|FL-7|
    .LF77..L|-F7||LL---7LJFJJF7-F7||L7L-7F-7F-7L7F7F7|LJL--7L-JF7F7|L-LJF|JF|LL-JLL-7F7F7L--7L-7F7LJ|L-7F7L--JLJLJF-7L7L-----JF--7L-77FJF7-7J7-J
    |7L|FJJF|7|LJL-----JF7|F7||FJLJL-JF7LJJLJLL-J|LJLJF-7F7L---J|||FJ7|-|--FLFF7.7.F||||L-7FJF7|||F7L7FJ|L--------JFJ-L--7F---JF7L--J7|7|JLF-777
    -7|L|J.|JFJF7F7F----JLJ|LJ|L---7F7|L7F7F7F7F7|F---JJLJL-7F--J||JL7-.|.FJ7J|FL--FJ|LJFFJL-JLJ|||L7|L-JF--------7L7F--7|L----JL-------7.L|FJF7
    |F-7J|FLFL-JLJLJF7JF-7FJF7|F--7LJLJFJ|||||||||L--------7|||F7||7F|F77F-.7-|7-L-|FJF--JF7F-7FJ||FLJ.F-JF------7L7LJF-J|F-7F-7F7F-----J7F7JF||
    F-.|-JJ..JLFLF7J|L7L7|L-J|LJF7L--7J|||||LJLJLJF-7F-----JLJFJLJL-7-|--F|-J--7F7JLJ7L7F7|||FJL7LJF7F7L--JF----7L7|F7||FJ||LJFLJ|L-----7F7.FJL-
    FJ7|.|-F-FFFFJ|FL7|FJL7F7L--JL--7|FJFJLJF7F-7FJFLJF----77FJF--7FJ-|JJLJLJ-L-J||JL-7LJ||||L-7|F-JLJ|F-7FL---7L7||||L7L7|F7JF7LL7F7F-7L7-7J|.|
    |J-|F|-.F--7L7L-7|||F7LJL7F-----J|L-JF--JLJFJL--7J|F--7L-JFJ.LLJJ|L|.F77FFJ.FL-7FF77FJ|LJF7LJL---7|L7L-----JJLJLJL-JFJ||L-JL-7|||L7L-JLL7FFJ
    -7-F-7-FL-7L7L-7||||||F-7|L--7F-7L7F7L---7FL-7F7|FJL-7L---JF-.||FL-|-FJ7--J--JFFFF--JFJFFJL7F7F--JL7L--7F-7F7F7F---7|FJ|F-7F-JLJL-J7.|.F77L7
    .J7|LJFF-7L7L--JLJ|LJLJFJ|F77LJFL7LJ|F7F7L-7FJ||LJF-7L7F---7J-JLF7FJ||L7J|.LL-L--L--7|F7L-7|||L-7F7L---J|FJ|||||F--JLJFJ|.LJF7F7F---77F7LFFJ
    |L7JL--L7||L-7F7F7L7F-7L7|||F7-F-JF7LJLJL--JL7|L-7L7L7||F--J|.F7LJL|7|F77F..|||L|L|LLJ|L--JLJL-7||L7F---JL7|||LJL7F-7-L7L-7FJ||LJF-7|-7F7||.
    |||J|.F||L7-|LJ||L7|L7|FJLJ|||FJF-JL---7F---7|L7FJFJ||LJL--7JFFJ.L|L|JFFF7-F77|F7F77F7L-7F7F-7FJLJLLJF--7FJ||L7F-J|FJF7|F-J|FJ|F-JJLJLJL|LJ7
    FF--7FF7L7|FF7FJ|FJL-J|L--7LJ|L-JJF---7LJF--JL-JL7L7FJF----J-|JL|F7-J.LFJL7-FF7|LJL-JL--J|||FJ|F7F7F7L-7||.|L-JL--JL-JLJ|F7||FJL7|J||F|7|7L7
    FJL-FFJL-JL-J|L-JL---7L-7|L-7|FF-7L--7|F7L-7F7F-7|FJL7L---7|-L-JL7.LF7FL-7L-7||L-----7F--JLJL7LJLJLJL--JLJFJF7F7F7F--7F7LJLJLJF7L77F-7-L---L
    7..L-|F7F---7|F7F-7F-JF7L--7|L7L7|F--JLJL--J|LJFJLJF7L-7F7L7F|JF|LFF|L7-FJF-J|L7F-7F7LJF7F7F7|F-7F7F7F7F-7|FJLJLJ|L-7|||F-----JL7L7JF|.|FJ||
    LJ77LLJLJ||FJ|||L7|L--JL--7|L7|FJ|L---7F7F-7L7FJFF-JL-7||L-JF77|-F--JFJFJFJFFJFJL7|||.FJLJLJLJ|JLJLJLJLJ.||L---7|L--JLJ||F-----7L-JF-77F7.FJ
    F|JJL|LL.F7L7||L-J|LF7F7F7||L||L7|7F7FJ|LJFJ|||F7L7F7FJLJJF7|L7JJL--7L7L7L-7|FJ7FJLJL7L-7F-7F-JF--77F7-F7LJF7F7L---7F77LJ|F----JF-7L7|L7|-|7
    FJ.FF77.F|L-JLJF-7L-JLJLJLJL-JL-JL-JLJFJF7L--JLJL-J|LJJF7FJ||FJ-FF7|L7L-JF-J||F-JF---JF7LJ|LJF7|F7L-JL-JL--JLJ|F--7LJL7-FJL7F7F7|FJFJL7JF.F|
    F|-L.||-FJF7F-7|FJF-7F7F--7F7F-----7F7|FJL------7F-JF-7||L7||L7F-J|F7L--7L-7||L-7L7F7FJ|F7F--J|LJL-----------7||F7L7F7L-JF7LJLJLJL-JF-J7.--J
    JJFJ7LL7L-JLJLLJL7|JLJLJF-J|||F---7LJLJL--7F7F7FLJF7|FJ|L7|LJFJ|F-J||F77L-7|||F7|FJ||L7LJ|L--7L------7F------JLJ|L7|||F7FJL-7F--7F-7L-777L7|
    |LJLL.FF--------7LJF7F7|L-7|||L7F7L--7F7F7LJLJL---JLJL7L7||F-JFJL-7||||F--JLJ||||L7|L-JF-JF7FJF-7F--7|L---------JFJLJ||||F7FJL-7LJLL--J7JF-J
    |.|-LJ7L7F-7F7F7L--JLJL7F-J|LJ-LJL--7||||L7F7F7F7F7F--JFJLJL-7L7F7||LJ|L----7|||L7||F-7|F7|LJFJFJ|F-JL--7F--7F---JF-7|||LJ|L-7FJF--7F--7-JJ.
    L7.7.FF-JL7LJLJL---7F-7LJF-JF-------JLJLJFJ|||||||LJF7-|F7F7FJFJ|LJL-7||F7F-J|||FJLJL7LJ|||F7|FL7||F---7|L-7||7F7L|FJLJL-7L7FJL-JF7||F-J.FF-
    ||F|7FL--7|F-------JL7L7FJF7L----7F--7F7FL-JLJLJ||F-JL-J|||||FJFJF7F-JL7||L7FJ|LJF---JF7||LJLJF7LJLJF7FJL-7||L-JL-JL----7L7|L-7F-JLJ||-|.-7.
    L-L|7|LFFLJL-------7FJ|||J||.F7F7LJF7LJL--7F7F7JLJ|F7F--JLJLJL7L7||L--7|||7|L7L-7L---7|LJL-7F7||F7FFJLJF-7LJL-7F7F--7F--J-|L7-|L-7F7|L7||7|7
    |..|FJ7F-|JLJF-----J|F-J|FJL-JLJL--JL7F-7FJ|LJL-7L||LJF7F7F7F7|FJ||F7||||L7L7L--JF---JL---7LJLJLJL-JF7FJ7L7F7LLJLJF7LJF--7|FJFJF7LJLJFJ7LFJ7
    F7-FL-77L|FLFL7F7F7FJL-7|L-7F7F-----7|L7LJFJF7F-JFJL7||LJLJ||LJ|FJ|||FJ||FJFL---7L7F7F7JF7|F-7F-----J||.F-J|L-----JL--JF-JLJFL7||F-7FJJLF-J|
    |7JLL7-J--F7L|LJ||||FF7LJF7LJLJ|F---JL7L7FJFJLJF7L7FJFJF7F-JL-7||FJ|||FJ|||F----JFJ|LJ|FJ|LJJLJF-----JL7L7-|F----------JFF--7JLJ||FJL7F77L-J
    |77L|J.L7JL-F---J|||FJ|F-JL----7L--7F7L7LJFJF-7||FJL-JFJLJF---JLJL7|LJL7|L7L7F-7FJ.|F-JL7L-7-F7L-7F--7FJ-L-JL---7|F7F---7|F-JF-7LJL--JFLF7..
    -J7---F7|77LL----JLJL7LJF7F7F-7L---J||JL--J|L7|||L-7F-JJF7L-----7FJL7F7LJFJ-LJFJL-7|L7F7|F-JFJL-7LJF-J|F---7F---JFJLJF7FJ|L-7|FJ|F7F-77.||L-
    |FJ7L-JLJ777.|F7F7F-7L7FJLJ||LL7F---JL-----7FJ||L7F|L7F7||F7F7LFJL7.LJ|F7L7F7FJF7FJ|FJ||||F7L--7|F7L--JL--7|L7F--JF--JLJFJF-J|L7FJ||FJ7F|7|J
    LLL77J7JLJ|F--JLJ||FJFJL7F-JL-7LJF--7F-7F-7|L7|L7|FJFJ|LJ|||||FJF7|F--J|L7LJ|L7|LJFJL7||||||F7||||L-7|F7F-JL7||F-7L---7JL7L--JFJL7LJL77-J-7|
    F.FL7.|F|FFL----7LJL7L--JL7F--JF-JF7LJLLJ.LJFJL-JLJFJLL7FJ||||L7|LJL---J.L7FJ-||F7L-7|||||||||FJ|L-7L7||L--7LJ|L7L-7F-JF7|F---JF-JF-7|J|F--J
    7FF7||---L|.F--7L--7L-7F7LLJF7||F-JL7F7F7FF7L----7FJF7L||FJLJ|FJL7FF7F7F-7||F7||||F7|LJ|||||||L7L-7L7LJ|.F7L--JFJF7LJF-J|||F---JF-J-LJF-JJLJ
    LLJJ-J7L-JL-L-7L---JF7|||F--JL7LJF7FJ|||L-J|F7F7FJL7||FJ|L-7FJL-7|FJ|||L7LJ||||||||||F-J||||||FJF-J.|F7|FJL----JFJ|F-JF-J|||F---JF77|L|.LF|.
    |LJ7F|-7FF.FJ7|F7F--J||||L---7L7FJLJ||||F--J||||L-7|||L7|F7|L7F-J||FJ||.L-7||LJ|||||||F7|||LJ||FJ-F7LJ||L-------JFJL7FJF-JLJL----J|JF7J7FF77
    L-|-FF-JL.FJ.FJ|LJF7FJLJL----JFJL--7FJLJL7F7||||F7|||L7|||||FJL7FJ||FJ|F7|||L-7||||||LJ|||L-7|||F7||LFJ|7F7F7F7F7L--JL-JF---------JFJ|-|7L|7
    LFJ7FLJ-|-F.FL-JF-JLJF7F---7F7L---7LJF7F-J|||||||||||FJ||||||F7||||||FJ||FJ|F-J|||||L7FJ|L7FJ|||||||FJFJFJLJ|||||F---7F7L--7F7F7F-7|FJ-FFJL-
    .|L|-LJ7J|LFF7F7L--7FJ||F-7LJL---7L--JLJF7|||||||LJ||L7|LJ||LJ|||FJ||L-J||FJL-7|||||FJ|FJFJL7LJ|||||L7|-L7F7LJLJ|L7F7LJ|F-7LJLJLJFJ||F7F7.JJ
    LF7J-L-J7F-7||||-F7|L7LJL7L------JF7|F77||||||||L7FJL7||F-J|F-J||L7||F--J||JF7|||||||FJ|FJF7L-7|||||-||F7LJL7-F7L-J||F7LJ7|F7F7F7L-JLJLJ|-J|
    |JJ7J.F7FL7LJLJL-JLJFJ|F7L-------7|L7|L7|||||||L7|L7FJ|||F-JL7FJ|FJLJ|F7FJ|FJ||||||||L7|L7|L7FJLJ||L7|LJ|F-7L-JL---JLJL7F7LJLJ|||F-7F7F-J-F7
    |7J77.L7.|L7F7F7F-7FJF-JL--------JL7|L7||||||LJFJ|FJ|JLJ|L--7||FJL7F-J||L7|L7||||||||FJ|FJL7|L-7J||FJL-7LJF|F---7F-7F7FJ|L--7LLJLJF|||L-77JF
    LJF7-.7.FF7LJLJ||J||FJF-7F7F-7F--7FJ|FJLJLJ||F7L7|L7L--7|F7FJ||L-7|L-7||FJ|FJ|||||||||FJ|F7||F7L7||L7F7L--7|L7F7LJLLJ|L-JF-7L----7F||L7FJ7-.
    .F7|LFJ7FJL---7|L7||L7L7LJLJJLJF-JL7||F----JLJ|FJ|FL7F7||||L7LJF-JL-7||||-|L7||||||||||FJ|||LJ|FJ||||||F7FJL-J||F--7F|F7FJFL--7F-JFJ|-LJJ|77
    FJL7--7FL-7F-7LJFJLJL|FJF7FF---JF7FJLJL7F7F7F7|L7|F-J|LJ|||FJFFJF7F7|LJ|L7|FJLJ||LJ||LJL-J|L-7|L7LJFJ|LJ|L-7F-JLJF7L7|||L-7F-7LJF7L7|.|LL|-|
    LJ.LLJ-F7-||FJF7L7F-7||FJL7L7F-7||L--7F||LJ|||L7LJ|F7L-7|||L-7L7|||LJF-JFJ||.F-JL-7||F----JF7|L7L-7L7|F-JF7LJF--7||FJLJL7FJ|FJF-J|JLJ-||.-7L
    F.F|J.||L-J|L7||FJL7LJ||F7L-J|FJ|L7F-JFJL-7|||FJF-J||F-J|||F7L7|||L-7L-7|-LJFJF-7FJLJL---7FJ|L7L-7L7||L7FJ|F7L-7LJ|L--7FJL-JL7|F-JFJJ.7-.LJ|
    |-FFF-LL7F7L7LJ|L-7L--J||L7F7|L7|FJL7|L--7|||||7L-7||L-7LJ||L7||||F7L7L|L7F-JFJFJL7F-----JL7L7|F7|FJLJFJL7|||F7L7.L7F7|L7F-7FJ|L7LJJF77.F7J7
    -J|L7FJJ|||FJ.FJF-JF7F7LJ|LJ|L7|||F7|F---J||||L-7FJ||F7L-7LJ7LJ||LJ|FJFJFJ|F7|.L-7|L----7F7|||LJ|LJF--JF-J||||L7L7.LJLJ7|L7|L-JFJFL-7|L7|J-L
    LFL.J-|.LJLJJFL-JF7||||7F7F7L7|LJ||||L7F7FJ||L7FJL-J|||F7L----7|L7FJ|FJFJ-LJ|L7F-J|F----J|||FJF7L-7L-7FJF-J|||FL7L---7F7|FJ|F-7L-77JF|-|J7FL
    L-J7JL|-FJJL-F---JLJLJL-J||L-JL7FJ||L7||||FJ|FJ|F---J|||L7F-7FJL7|L7|L7L7F-7|FJL7FJL7F7F7|LJL7||F7|F-JL7L-7|||F7L7F--J||LJ|LJ-L7FJ|7L|J|.-7J
    -JL-7.|.|-FLJL--7F7F7F7F7LJF7F7||FJ|FJ||||L7|L7|L-7F-J|L7LJL|L-7|L7LJ|L-JL7LJL-7LJF7LJ|||L7F-J|LJLJ|F--J|FJ||LJ|FJL---JL-----7FJL--77|LF7J--
    L-7LF-F-|-7JFJLFJ|LJLJ|||F7|||LJ||FJ|FJ||L7LJFJL7-LJF7|FJF-7|F-JL7L--7F---JF-7FJF-JL--J||FJ|F7L---7||F---JFJL7FJL7F---7F7F7F7|L-7F-JJ7.7J|F7
    LFJ.LJF-L7LJL--|FJ.F7.LJLJ|||L7JLJL7|L7||FJF-JF7|F--JLJL-JFJ|L-7LL-7FJL---7L7|L7|F7F7F7|||7|||F-7FJLJL7F7FJF-JL7FJL--7||LJ||||F-JL77L7F|.-J7
    F|J7FF|7.LJL7JFLJF7||F7F--J|L7L-7F-J|FJ||L7|F7|||L7F7F7F7FJ.|F7L-7FJ|F----JFJL-JLJ||||||||FJ||L7|L---7|||L7L7F7|L7F-7||L-7LJLJL-7FJ7F|-77|FJ
    F||FL-JJJ-7.|-F--JLJLJLJF-7L7|F-JL-7||FJ|FJ||||LJFJ||||||L-7|||F-JL7||F7F7FJF-----J|||||LJ|FJL7|L7F-7|||L7L7||LJFJL7||L--JJ|LJJFJ|JJFJJ|7L|F
    LF77J|-LJFL--7L7F-7F7F-7|FJFJ|L7F--J||L7LJ|||LJF7L7|||LJ|F7|LJ|L-7F||LJLJ||-|F7F-7FJ|||L7FJ|FFJL7|L7||||FJFJ|L-7L7FJ|L---7-|FLFL-JJ.--FF--|7
    LLLJ|LJF7J.FL|-LJLLJLJ-LJ|FJFJFJ|F--JL7L--7||F-JL-J|||F-J|||F-JF7L7||F---JL7LJ||L||7|||FJL7L7|F7|L7|LJLJL7L7L7FJFJL7|F-7FJFL7J.L7|J-|F|L--JL
    7.LLJJFJL7-F.L.LL-F|LF---JL7L7L7LJF7F-JF--JLJL--7F-J||L-7|||L-7|L-J||L----7L-7||FJL7LJLJF7|FJ|||L7|L7F---JFJFJL7L7FJLJFJL--77.FLJ77.--|JL-FL
    L.F7|FJ|F.F7-J..L7L7LL-7F7FJFJFJLFJLJF7|F7LF7JFFJL7-||F-J|LJF-JL-7.LJF----JF7||||F7L--77|LJL7||L-JL7|L7F-7L7L7FJFJL-7FL-7F7|-77|L--7.7LFJ.||
    L-FL-JJFL-JLJF7-LLF7JLFJ||L7L7L7FL--7||LJL-J||FJF7L7LJL7FJFFJF7F7L7|-L7F7F-J|||||||F-7L7L7F7||L--7JLJFJ|FJFJ-|L7L7F-JF7L||LJJ||7J.LF7FJ|F--7
    |LJ|7L7|.|L|L-|F-|JF-LL7||FJ.L-JF7F-J||F7F7FJFJFJ|FJF--JL7FJFJ||L7L7LFJ||L-7||||||||FJFJFJ||||F-7L7LFJFJ|FJLFL-JJ|L--JL7LJ7JF-J|L7.-7|FFL7FJ
    ||.F|LJ-7FJ.J.J||L7|LLLLJ||J7JF-J|L-7|||LJLJL|FJ7|L7L--7FJL7|FJL7L7|FJFJ|F7||||||||||FJ.L7||||L7|FJ-L-JL||F7|LF--JF-7F7L---7L-J|||FLLJF7.-||
    F7FJL7||--7FJF|J7-F7L|J|LLJJ|LL-7L--J||L7F---JL-7|FJJF-JL-7||L7FJFJ||FJFJ|||||LJ||LJ|L-7-LJ||L7|||7|7|7J|LJL7-L7F7L7|||F7F7L7|F|7J|7LF-J-F77
    |L|LJLF-7|-F-JJ-J7---7FL7-JFJ.|LL7F-7|L7||F7F-7FJLJ.FJF7F7||L7|L7L-J|L7L7|LJLJJFJL7LL--JJ|JLJ.||LJJ-FJF-JF--JJL||L-JLJLJLJL-J7L||--7L7.|.|J|
    7.LF|-|J7LJJ.F|FLJJ.F.|L|--LL-J.L|L7||F||LJ||FJL7J--L7|LJ|||FJ|FJ-JL|FJFJL-7-|.L-7L---7..|FJF-JL7L|FLFL-7L7-L-L||J7.L7LJ||.|.L-FJ|.J7L777|-7
    LL7L7.|FJ.F7.FL-J--LJ-F-FJ-F|FL|-L7||L7|L7LLJL7FJ||.|||7FJ||L7LJJFLLLJLL--7|LLL-FJF--7L7-7.F|F7FJ-L7-7JLL-JFL7FLJ.FJJLJL|---7J.||-7FF-|L|77|
    7JLF|F--L7|.FF-L7F7J77.LL-77F|7J|L||L7||FJ-LLFJ|JFJ-FLJ-|FJL7L-7-|J|LJ7J||LJ7.LFL7L7-|FJJ|FFLJ||.L-FJJ|FJLJ7||7|7FJ.|-FF|F7.|L--F7J-J.L-LJ-|
    |JF.|-J|FFF|.|FLL|J|JL7.L7LFJL-.LFJL7||LJJ777L7L7JL.|.JJ||JLL7FJ.J7-F.F.FFJ77|---L7L7LJJLF7J..LJ-LF--FJ-JJ.FF|JL-|.||FJ--L7-L7|J|LJ..FL7|F|J
    LFJ7J|JF-|-|-F-|LL-7.FL7LJ7-7-|-LL--JLJFL.-F-L|FJ-J.FFJ|LJ.FLLJJ77F-||J7FJ.FLF77LFJFJ-|7-||JFL-J.LL|LJ..J.F|-J|F.|.F7J||.J|.LJ7-F7||--.7-7-7
    F-7-7F7|-|.--J---F7.77.L7.LJJJ||FLJ|.FJ7L|J|L.LJ|L|7L--|-|.FF-FLLFJ.FL7F|F|J.LLF.L-JJJ|-7LJ|FJ-L77L--.FJ.FJL|LJ---7|L7L7-LL7|L|-LJ-JJFJJL7.F
    F77||J||.7-LLJJJ.JJF7J77|7JF-7F-JJ|F|JL7.|||L--LL-|L-|.|-JFF|-J|-|-7.|FF|JJF7--||JFLLF|L-7F-|||..|F|.F--LF7FJ.|JL7L7J7F7.L.LF.LL7FF7|LJ.-L-7
    LJJF7.J-F-.F|FF--J|||.-L-777FF|L--LJJ7.L7--JF|..L|JJF-J.-.|FJFLF-F-77|LFJFFJJ.L-J-|.FF|-L||-J77.-LJ-L|--JL|.|J.|L-7-7FL-7.7..F.FL7LJ-LJ|JJF|
    |.|||.|-JJ7-|7|FJ|FJF7.FLLJ.F-JJLF7|LJ7FLFL7FJ|.|||.FL.-LLL-JF-7J|7JFLJ|-FF7L7-L|.L-FJL.LLL-7F|-J77|L||.FF7FL7FJL|.LL--J.L|F|LF|L|J-||.|..--
    -F7-F-7JLL7-J7JJ|-L|LF.|JLJJLL7--L-L-LJ7JJL-JFL-J--|J---7---F--|L-J-JJ.L-JLJ-7-LJ.-L.F.|.FLL--JJL-J-.J7-|JL-LJ-.LJ7J.L7J-.L-JL7J-LJ.L-J.LL-J
""".trimIndent()