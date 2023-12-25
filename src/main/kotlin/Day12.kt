fun main() {
    println(solvePart1(sampleInput))
    println(solvePart1(input))
}

private fun solvePart1(input: String): Int =
    input
        .lines()
        .map { line ->
            val (word, groups) = line.split(" ").map { it.trim() }
            generateAllPossibilities(word) to groups.split(",").map { it.toInt() }
        }
        .sumOf { (pos, groups) -> pos.findMatches(groups) }

private fun generateAllPossibilities(x: String): List<String> {
    val possibilities = mutableListOf(x)

    while (possibilities.any { it.contains("?") }) {
        val temp = possibilities.toList()
        possibilities.clear()
        temp.forEach { word ->
            possibilities.add(word.replaceFirst("?", "."))
            possibilities.add(word.replaceFirst("?", "#"))
        }
    }

    return possibilities.toList()
}

private fun String.toGroups(): List<Int> {
    var sum = 0
    val groups = mutableListOf<Int>()
    this.forEach { char ->
        when {
            char == '.' && sum != 0 -> {
                groups.add(sum)
                sum = 0
            }

            char == '#' -> sum++
        }
    }

    sum.takeIf { it != 0 }?.let { groups.add(it) }

    return groups
}

private fun List<String>.findMatches(groups: List<Int>): Int =
    map { it.toGroups() }.count { it == groups }

private val sampleInput = """
    ???.### 1,1,3
    .??..??...?##. 1,1,3
    ?#?#?#?#?#?#?#? 1,3,1,6
    ????.#...#... 4,1,1
    ????.######..#####. 1,6,5
    ?###???????? 3,2,1
""".trimIndent()

private val input = """
    ?.?????#???#? 1,1,2,2
    ????.#????#?????#??# 1,2,1,1,7
    ???#?.???????.? 3,1,1,1,1
    ????#.??.? 1,1,1
    .?#??????.? 3,1,1
    #????#???????? 1,9
    ?.?##????????#?. 6,2
    #..??#?#?#????? 1,3,5,2
    ?????????#????????. 3,1,4,1,2,1
    ????#.???????????? 1,1,1,2,8
    ?#..#????.?#?##????? 1,1,1,5,1,1
    ????##.#???? 1,4,1,1
    ??#...?#?#? 1,4
    ?.#.????#??????#??? 1,1,4,1,3,1
    ??.?#?.???#?????#?? 3,10
    ????????#????? 1,3,2,1
    ???.?????##??.?? 1,7
    ????????#.?????#?? 8,2,2
    ???????.????.#?# 3,1,1,3,3
    ????????????..? 1,6
    .#??#?.??????# 1,3,3,1
    ?..#.???.? 1,2,1
    ??????#??. 1,1,4
    .??#??##?????????.?# 1,8,2,1
    ???#?#?.????????.??# 6,1,3,2,1,1
    ??###????????#??.#? 9,5,1
    ?#?##???.#.?#???? 1,5,1,4
    ?.??.??#?????? 1,1,6,2
    ????.????? 1,4
    ??????????#.??? 1,4,1,2,1
    ?#??????#.????# 1,1,2,3,1
    ?.??..??#.? 2,2
    ???.?????.?? 2,1,1,1
    ?#.?#??.????.????? 1,3,1,1,3
    ???#??#?.#?????#.? 7,4,2,1
    ??#??#??.#?#??#???? 1,1,1,4,1,1
    ??.#?.?#..??.#?#?#. 1,2,1,1,5
    ????????#?#?.????? 2,8,1
    ?????..?#?#?.. 3,5
    ?????????#?#?#????? 1,9,1,1
    ??????##?#?.???????# 2,6,1,1,1
    ??????..????????# 3,1,3,2
    #?..?#?????#???. 1,1,6,1
    ??.??.????. 1,2
    ?..????????? 1,1,3
    ???#?#??.??? 1,1,1,1
    ????#??##.???? 1,6,1,2
    .????.?#????. 3,2,1
    ?#?.???.?.. 2,1,1
    ????##.?.???#?#? 1,4,1,6
    .#?#.??####??????? 3,9
    ?.?#?????.??##..# 2,3,3,1
    ..#???#?#?#??. 2,8
    #.??????????.?## 1,1,1,1,3
    .?????.???#?##?# 3,7
    ?##.??.#????#??#? 2,1,3,2,1
    .???##?????. 1,2,2
    #.?###????#?...# 1,9,1
    ???##.??#?#?#. 1,3,5
    .????##??????.. 4,3
    ?.??.??#??????? 1,2,3,2
    #####???.. 5,1
    ?####???.????? 4,1,1
    ????????#?. 2,2
    ???.??????.????? 1,3,1,4
    .?????????#????.#?? 12,1,1
    ?#..?#????#???.???# 2,8,1,2
    ???#???#??#??????..# 12,1
    ?#..?#??##?#??.?. 2,6,1,1,1
    .????##???? 2,5
    ?????#????? 1,2,1
    ?#??#????#?#???#? 11,3
    ?????.?.##??? 1,2
    ###???..?#?#???##?? 3,1,9
    #.??#..?##?? 1,1,4
    .##??????.#??##?#? 3,1,7
    ???#????..? 1,2,1
    #???#?.?#.? 1,2,2
    ?#.??#????? 2,4,2
    ???#??#?????#???? 4,8,2
    ??????????.# 4,1
    #?????????#?##?. 1,2,1,1,5
    ???.???.?.?.. 2,1,1,1
    .?#????????? 1,2,1
    ..?.??.?#?? 1,1,2
    ??#?#?????.??. 9,1
    ????#?..???#????. 1,1,8
    ??..#.??#?#???.?#??# 1,1,7,1,1
    ?#??#????.??? 2,5,1
    #?..???##???.??? 2,1,5,2
    .??#??#?#.?.????##. 7,5
    ?????.##??#? 4,3,1
    .#??##??#?????..??.? 1,10,2
    ???###??#?. 1,3,2
    ??#?????## 1,5
    ??#???????.??? 1,1,2,1
    .???#?.??.???. 3,2
    #?#??#..?#?????#?? 3,1,2,1,1,1
    .???????#??#?? 1,5,1
    ?.??#.???#????? 1,1,1,2,1
    ?#?????#?.?????#?? 4,1,7
    .?#.???#??##? 1,2,3
    ?##?..????.?#?????? 3,2,1,6,1
    #.?.??###??.#???.?? 1,1,4,1,1,1
    ??????.??.??? 2,1,1,1
    ?#??.????#.? 2,3
    .??.#?.????#?##??## 2,2,5,6
    ##????.??#?? 2,1,1,1
    ??#?#???#?? 4,4
    ?????.????????#? 1,3,2,1,1
    .??..#????#???.??? 1,9
    ???.?#?.????. 1,3,1,1
    .???????#?.???# 4,2,2,1
    .##?#????..??#??#?. 2,5,1,1,1
    ??????#?#???..????? 10,1,4
    .?#?..??.??.???#?? 3,1,2,6
    #.??#?#??????#?. 1,5,1,1
    ??###????????????? 4,2,1
    .?.???.#??.#???.?# 1,1,2,1,1,2
    .???.??#??????.? 2,1,4,1
    ?.???##??# 1,6
    ?.#?#??.?#?#??.#?.?# 1,2,4,1,2
    ?..#??#???? 1,2,2
    ?#?###?#?.????# 2,3,1,5
    .?????.???#??### 1,1,1,1,7
    ????#?????#?? 1,5,1,1
    ?????????? 2,2,2
    ??##??????????#???? 4,3,5
    ##????##???.. 5,4
    .???#??#???. 1,5
    ??##.?.?#? 4,1
    .???#?#??.???#? 4,3,4
    ?##?##??????..??? 11,2
    ?.???.###???#?.? 1,1,7
    .???????????#?? 1,1,5
    ???#.?.???? 1,1
    ??.??#?????.. 1,8
    .??.????????.? 1,1
    ??#????#.# 2,1,1
    .????.?##????.? 1,1,4,1,1
    .?.????????? 1,3,1,2
    ???.#??..#???. 1,3,3
    #???#?.??. 1,3,1
    ???.?#???.????? 1,4,1,1
    ?.????#?#?.??? 1,8,1
    #?.?#???.#..???? 2,2,1,1,1
    ?.??#?????.???.. 1,1
    .??#?.#???????..? 1,2,1,6,1
    ?##.#?.?#?##???##?. 2,1,10
    .##????.#?.??#.##?? 6,2,3,2,1
    ???????#??? 4,3,1
    ????.??.?? 4,1
    .?#??????#???#? 1,1,3,4
    #??.?..##??????# 3,1,2,3,1
    ?#?#??.???? 2,1,3
    ?#?#????..##???? 4,2
    ?#????.??##?#?.??#. 2,1,5,1,1
    ??#???????????.? 9,1
    ?.??##?????.?. 4,3,1
    ?????#???##? 6,2
    ????#??.??.?#??.#? 1,1,2,1,3,1
    .?.??.?????????? 1,1,2,2,1
    ?.?.?????#? 1,2,1
    .???.?##??#???????. 1,12
    .#????????.#??.???? 1,6,1,1,1
    ???##??.???#????? 7,6,1
    .#????#???????.. 2,9
    .?##???#?.???#?#. 7,1,3
    .#???????? 2,2
    ##.#.?#?#??????# 2,1,6,3
    ?#???????? 1,2,1
    ?????????#??# 2,7
    .???.#...??#.??#?? 1,1,1,3,1,3
    ??#?#?#.?#.??.? 6,1,1
    ????.???#?.#?? 1,1,2,2
    ?????.??..#??# 3,2,4
    #?.????##???##?????. 2,7,2,4
    ?#.#?#???##?.????? 1,1,7,2,1
    .#?##.???? 4,1
    #?????#???# 7,1
    ??###????.??? 5,1,1
    ??#????#??????.?? 8,2
    #?##??#?#??##.?. 1,7,2,1
    ????????.?? 7,1
    ??###???##????.???? 12,2,1
    ??????###??#??? 7,3
    #.#?##?..???#.# 1,1,2,4,1
    ?.??#?#?##??????.? 1,10,2,1
    ?#??????.???#?? 4,1,6
    ??.?#??#??#?????. 1,8,1
    ???.?#?#??????##. 2,4,3
    ????????.#????? 1,2,1,2,1
    #???.??#???? 1,1,1,2
    #..??????#??? 1,1,8
    #?.#?..###? 1,2,3
    ??????...?#?? 2,2
    ?#????#????? 1,1,5
    #?.?##?????? 2,3,3
    ????.??#??? 1,1,4
    .??#??..??????#.. 4,3
    ???..??..#?.?? 3,2,1,1
    #.#??#???#? 1,1,6
    .??????#.????#.?.?# 1,4,1,1,1,2
    ???##.???##????? 3,7
    #.??????????#???? 1,1,10
    ????????##??###??? 1,4,8,1
    #???????.??###?? 1,2,4
    .?#??????.?.??? 3,1,2
    ??..#.#????#?#?.? 2,1,2,4
    ?#?#?#???.?#???#??#. 6,2,2,2,1
    ??#???.?##.??#?? 2,2,2
    ????.?#??.. 2,3
    ????.?##???.? 2,4
    ..?#?#???????? 2,9
    ?#???.####?###? 2,9
    ???##?????##???.??? 4,1,6,1,1
    ?#.?????.???? 2,2,1,1
    .??.???????#?#?#?.?? 1,2,8,1
    ?#???...?????????? 4,3,2
    ??...#??????.? 1,1,5,1
    ?.#???##???????? 1,1,6,1,1
    ????????.#????? 6,1,1
    ?###???.##??? 5,2,1
    ???#.#?#???#???? 4,3,1,1,1
    ?.?????##??? 1,3,3,1
    ?#??##.??#.?? 5,2,1
    ?????.?????????#..? 4,5,1
    ??????##???# 4,3,2
    ?####??#?#.?#?##??? 7,1,2,3,1
    #?????.#?????#. 3,1,1,1,1
    #????#??.#???# 8,2,1
    ??.#?.?????? 2,1,6
    ??#???.?#?###?#?#??. 5,9
    ?.#?????#??#?????#?? 14,2
    ?####??.?..? 5,1
    ..?.?#?##?????##???? 12,1
    ?#???????#?#?? 4,8
    ??#??.?#??#?? 4,3,1,1
    ???..??.??##? 2,1,2
    ??#.?##??###.# 2,8,1
    #???????#?. 1,1,2
    .????????#.? 1,4,2,1
    ??#?##???.???#.???? 1,1,5,1,1,2
    .?#????#????? 4,1,1
    ?#??###?.???.? 2,3,3
    ..???.?##?# 2,4
    ..???#????#. 3,2
    .???.?##??? 3,2
    .?#????.?? 1,1,1
    ??##???#??# 8,1
    ????##??#??#????.?? 13,1,1
    ??.??###????#?#?? 1,4,6
    ??..????.?. 1,2
    ???#?.#????? 1,1,1,1
    ?#????#???#..#.#?? 3,4,1,1,3
    #?#??#?????. 1,1,7
    #????##??#?#??.???? 13,2
    ??.??#?#?#?.#?#??? 1,1,1,4,4
    #.?..????? 1,1,1
    ???##???.#???. 6,1,2,1
    #?.#?.??##?? 1,1,1,4
    ?.???.????#??#??#? 1,1,1,1,2,4
    #?#?#?????...#?#??## 6,3,3,2
    #?#??????#???#.? 1,2,1,1,2
    ?#?.?#?#????.#. 2,6,1
    ????????#?###???? 1,1,8
    #??#????##? 1,2,3
    ???#??.???#?? 2,1,1,1
    #??#?.?#??#?..?#???? 4,6,4
    .????..??? 1,1,1
    ??#???#????#? 7,2
    #.??.???????#??##??? 1,1,15
    .?#...?###????? 2,8
    ???.?#?.???? 1,3,1
    ???.??.??.?. 2,1,1
    ##?#??#??#?#?.?#? 7,4,2
    ???????????.?#?# 10,4
    #????????#? 1,2,1
    #?.?#??????? 1,9
    ..???#??##??.#?#??#? 1,1,3,1,6
    ??.??????? 1,6
    .?.??.?????##?#?? 2,9
    ?.????????? 1,4
    #.#??????#??#??? 1,8,4
    ???##????????.??# 8,1,1
    ???#??????? 2,2
    ???.????..? 1,2
    ??#???.??#?????# 2,1,9
    ?..?#????.#????? 3,1
    ?.#..?##.???? 1,3,1
    ????#??????#??#.??## 14,4
    ??#?#.??????????..? 1,1,1,1,8,1
    ??.??..?..?#?? 1,1,4
    ??##????.?#? 8,1
    ???##????? 4,1
    ?.?????.#?????.???? 4,1,2,2
    #.???#??##??#?# 1,8,3
    ???.?.#??????????? 1,1,4,5
    ?.???..??.??.? 2,1,2
    ???#???#???????? 7,1
    ?.???#?.?##??????#. 1,2,7,1
    ?#?.????.#.??? 2,3,1,2
    ???.???#?.#???# 2,1,1,1,1
    .??#???..?.? 1,2,1,1
    .????##???##???? 1,2,5,1
    .?#.?.?#?. 1,3
    ?##???.?#???. 4,2
    ..#??.???.??????# 1,1,3,2
    ??.???#??????#? 2,4,1,1,2
    ???????#????#??.??#? 10,3,2
    ?.?#???????????#??? 2,1,7
    ??????#??##??###??? 15,1
    ..#?##??????? 4,3
    #???#?.?#?#? 1,4,3
    .???????#? 4,2
    ??.#?##?.?##?????#?? 1,5,8,1
    #.??..???????##??? 1,1,9
    ???????#?. 6,1
    ?#?#????#.#.?? 3,3,1
    ??????????.?.?. 1,1
    ??.#?#?##? 1,7
    ?.#??#??????????# 1,4,1,5
    ??.??.??????. 1,1,4
    ?#??#????#????..?.#. 1,8,1,1
    ?#.????????..?????? 1,4,5
    ??#??????? 1,4
    ?..??#????###? 1,2,6
    ##?#??..??????#. 5,1,1
    ????.??.?#??? 2,5
    .???????.#.?????? 1,4,1,6
    ??????##??#.?#? 8,1,1
    ?????.#?#?#? 5,1,1,1
    ##??#.?.???#?# 2,2,1,5
    ##.???.#???..????# 2,1,1,2,1,1
    ?#???#..??.???. 2,3,2,2
    ?#??????#? 1,1,2
    ???????.?? 1,1,1
    ???##??##???.??##? 5,4,5
    ??????.??##? 3,4
    #?????#?#???#? 3,1,3,2
    #?#???#??.????? 3,2,2,1
    ?.?##??.?????#.?. 4,4
    ?????.??#?.??# 3,3,2
    ???#?#????????#?##? 3,4,1,6
    ??#??#..??. 4,1
    #???????###?.?#? 2,1,7,1
    #???#?##???. 1,1,5,1
    .??#?##????.? 6,1,1
    ???#?????#??#?#???? 7,7,2
    #.??.?#?#.?#..?#? 1,4,2,2
    .?#?##?????.? 4,1,1
    ??..?#?..? 1,3
    ???.?????#?? 1,5
    ?????..#??#?## 2,1,7
    ??.?.?#?#? 1,5
    ??.#.?#?.??# 1,1,3
    .??.?#??#???? 1,1,3,1
    ?#???###???? 3,7
    .?#??.?..??###? 1,1,4
    ?##??????# 3,1,2
    ???.??##???? 2,2,1
    .??#??#??##?#?? 2,8
    ????#??#???#?? 6,1
    #????????#?#?.? 7,4,1
    .##?????.??#???????? 3,2,1,6
    ?#?#?.????????? 5,1,2,1
    ????#??.?# 2,2
    ?.???###????#?????? 12,1
    ????#????????###?#? 1,1,1,1,7
    ??.?#??.#?? 1,3,3
    ????#?????????? 3,5,1,1
    ?????????.??.???#?? 7,1,1,1,2
    .????###?#????? 1,1,3,1,1
    ????????.?????#?# 1,1,4
    ??.??.?.?? 2,1,1
    .??????#??????? 2,9
    #.???##?#????.??.?? 1,5,3,1,1
    ?????.??###.??#???? 1,1,4,1,1,1
    ??#?.?#??? 1,2,1
    ???#???#???.?????? 1,1,1,1,1,6
    ???##????.?#??????? 1,6,6,1
    ???#???##???.?????? 4,4,1,2,2
    ?????????.??? 1,1,2,1
    ????.#??.# 3,1,1
    ?.???.?.????.?..?#?? 1,1,3
    ??.#???###?##???..? 2,1,8,1,1
    ?????.??????? 1,7
    ????.????????.? 2,1,1,3,1
    #?#.?..#?#??###??? 3,1,1,1,4,1
    .?#????##?#???..???. 10,1,2
    .??#.????.??????# 2,2,5,1
    ??#.???????#?????? 1,10
    ????#?#.???#??##?. 5,6
    ??#???????## 1,2,2
    .?.?.?.??? 1,1,1
    .????????.?.#?? 6,2
    .???????#?.?.? 6,1
    ?.?#??#?.??#.# 5,1,1,1
    ??????????????.?.? 1,4,2,1,1
    ..??#???.???#???#? 2,6
    ????#??##??.#??#? 1,1,4,2,1
    .?#???#?.?? 1,1,2
    ?????.??.#.???? 5,1,1,1
    .#?.???#????#??#??#? 2,5,1,1,6
    ?????.?##??.? 1,1,4,1
    .#????????????#??? 7,4
    ???..##?#?? 1,4,1
    .?.??#??.#???? 1,2,3,1
    ???#?????##??#.. 5,1,4,1
    .?##?.??.? 4,1
    #..?.?#??# 1,3,1
    #????#?.#.???.?# 1,3,1,1,1
    #???#?.????#???#??? 2,1,2,9
    ???.??.#??? 2,1,4
    #???#?#.??????? 1,1,3,3
    ??#?#??.??#??.# 4,3,1
    ..???##?.?#.? 6,1
    .#???##????. 6,1
    #..??.??????#????? 1,1,1,4,1,1
    .????.?.#?.?#? 1,1,2
    ?#.?#???????#????? 1,14
    #???#.?..?.???.? 5,1,1
    #???????###. 2,6
    ?#??#?????#?.#?#?? 8,1,1,1,1
    ?????..?.?#???#?## 3,1,8
    ..???????. 3,2
    .?#?????.??????#??.? 1,2,1,3,3
    ???#.????#? 2,3
    #.#??.?.?##?#????? 1,1,1,7,1
    ????#???#??????#.. 5,8
    ?#???#??#???###?. 3,6,3
    ????#?.?????? 4,5
    #??.?????##??#??#?? 1,1,1,1,9
    ?#?????#???.? 3,4
    ..???.??###?? 1,3
    ??.??.#?#?? 1,3
    #??????????#..? 1,1,3,4,1
    ?????#???##?? 1,1,2,4
    ?.????????????#?###? 2,5,6
    ?#?.##???.?? 2,4
    ???????.#?? 1,1,2
    ???#????.#.??..?? 1,5,1,1,1
    ??.??.?.???#? 2,1,3
    ???????.?.#???#????? 3,1,1,2,4,1
    .##?????.?? 5,1
    ??????????? 2,4
    ?????#????????? 1,1,6,2
    ???????#???.???. 5,1,2,1,1
    ??##??????.? 2,1,1
    ?#??#?##?#?#??#???. 2,8,1,1
    ??##????????.???.. 5,3,3
    ???????#???#???.?# 1,1,5,1,1,2
    .???..???#?#?### 1,1,1,7
    ????#?.?.?#? 4,1,1
    ????#?#??#?#??????? 1,12,2
    ??????.???? 1,1,2
    ...???..#? 3,1
    ?####???.? 4,1,1
    ?.?#??###????.?? 3,3,1
    ????#????#.???.## 2,6,1,1,2
    ?.???.???#??..??? 1,2,6,1
    ?#??#???.????? 8,1
    ?#.#????.#????????#? 1,1,3,7,1
    ???#????#???#?#??? 10,1,1
    ????.?#?#?#?? 2,7
    .???.#???##??????.? 2,8
    .?#...#???? 2,1,1
    ?#??#??.??##?????# 2,1,1,3,1,2
    ????###???#??? 6,2
    ?????????????.##??. 3,4,2
    ???.??#??#?.#.???# 1,1,1,1,1,4
    ?????##??.?. 2,4,1,1
    ??.???????????# 2,3,2
    ?.???????#??#?????. 1,3,10
    ??????????##?#???. 1,1,9,1
    ?#?#..????##?#?# 2,1,9
    ???##?#???.??????? 7,1,3,2
    ???????#.?? 3,1
    ??#?#.???????? 5,1,1
    ??#?.???####??#?.? 1,1,1,6,1,1
    .??#???.#? 5,2
    ?????#??..#?##???? 4,8
    .??????##?#?###?# 4,11
    ??..#.?#???.#?#? 1,1,1,1,4
    .??##?????.??.?????. 9,4
    ?.?#????#???#..?. 1,1,6
    ?#?#?#??????#? 1,4,2,2
    ?#...#.??#.??? 1,1,2,2
    ??????.?#?.?##???? 1,2,3,3,1
    ?#?#?..#?# 4,1,1
    ?..#.#??#??###?# 1,1,2,6,1
    ##??#.????????????. 5,2,3,1,1
    ??????????? 2,1,1
    #.??????#??? 1,7,1
    #..??.???.??. 1,2,2,1
    ??#?????#????????? 5,1,5,1,1
    .??#??.??. 3,1
    ???..?..?##?#??. 1,4
    ?????#?#?#? 6,2
    ???#???.??????. 6,2
    ?.????#?##?#?#?????? 1,1,8,1,1,1
    ???#.???##?#??#. 4,7
    ???..?##??#? 3,5
    ???..#?#??.???. 3,5
    ?????.??#??????####? 1,1,7,5
    .??##????????##? 1,3,7
    ?#?????#?.???#.?? 9,1,2,1
    ??.##??#???????? 1,2,2,1,2
    .?#??###.??#.? 6,1,1
    ???.?#??#.???#####? 2,1,2,7
    ?#???????#.?? 1,1,2,1
    ..????##?.??.??# 3,1
    .?..????????#??????? 1,13,1
    #???.???#?.??# 4,5,2
    .?#.?#????? 1,1,2
    ??.???#?#?? 1,2,1
    ???.?????#?#?# 1,1,9
    ?????.??????#?#? 4,4
    ????#?.???#????#? 1,2,1,7
    ?????????????.??? 4,1,2,1
    .?#???.??# 5,1
    #?????????. 2,1,1
    ????#?.?.???#????? 5,1,2,1,1,1
    ##????.??.?? 3,1,1,1
    .##?#??##..??#?#?#?. 8,7
    ?????.#??.???.?#???. 5,1,1,1,2,1
    ..##??..?? 2,1
    .?#????#?#????? 2,1,9
    .##???????? 3,3,1
    #??.?#???#??#?? 3,1,5
    ##?##????????#?????? 8,1,1,1,1,1
    ??#?..???# 2,3
    ???#????..? 1,5
    ##?#??.#????? 6,3,1
    ???#?#?#??#?#??. 2,1,1,1,5
    ??#?#??#????#????#? 5,2,8
    ..??#?#??.??? 6,1
    ??????#????? 6,1,1
    ????????#????#?#.? 3,2,2,1
    ?.????#??.??#?? 1,1,1,3,3
    ?#???#??????? 10,1
    ?.?????.?. 1,3,1
    ##??.??????#. 4,1,1
    ???##???#??? 1,7,1
    ?#???.#?##??#? 2,1,5,1
    .??...??##? 2,4
    ????#??.???? 1,2,3
    ?#????????###????? 4,11
    ?#.??.?#.#?#????? 1,2,2,4,1
    ??????????? 3,1
    ##?#???#?###.#.?? 12,1
    .??#??.???.??? 4,2,1
    #?##?????..#.?? 5,1,1,1,1
    #??#?#??.????#.??## 7,5,1,2
    ???#..????? 4,4
    ?.#??????#?. 4,2
    ?#??????.?. 7,1
    #.?.??.#?? 1,1,1
    ?#?.??##.?#?.?. 2,4,2,1
    ????????##???.??#?? 1,2,1,5,1,2
    #?.?.#???#?##?????? 1,1,12
    ???????????.###?#??? 1,1,7,5
    ????.?#??? 1,1
    ??#?????????#?#???? 2,11
    ???????#?#? 3,3
    ?.#?????#??????.?? 2,3,2
    ?.????.#?? 1,2
    .???##???.?.#.. 3,1,1
    ???.??#??????#?#. 1,1,4,2,1
    .?#??#?#?.??#? 8,2
    ??#?#??.????#????# 1,1,1,2,1,4
    #?#?.?.???#??#..?? 1,1,1,6,1
    ?#??#??..????? 7,3
    .????.?#??? 1,1,3
    ???.?##.?? 3,2,1
    ?.??#???.??.?? 5,1
    .#?????..?.#??? 4,1
    ?.???????????# 1,5,1,1
    ?#??#???#.?#..? 1,5,2
    ?#??#?.?????.#????.? 2,1,4,1,1,1
    #??.??#???#????? 2,4,1,2
    ????#??#??#.??#??? 4,4,1,1,2
    .????#.??? 5,1
    ??##.???##? 3,5
    .?#??.????????????. 3,2
    .?#??..?#???##?????. 3,1,7
    ?.???.????#??##??#? 1,1,1,10
    ??##???##? 2,3
    ???#??##?#??#????? 12,1
    .#?.??##?##..??. 1,1,5,1
    ???..??????? 1,1,1,1
    ??###.?#???##. 4,7
    .??#?.?#??? 3,1
    #?#???????? 6,1
    #????#.#??#??????# 2,1,1,3,4
    ???????##??#??###?? 9,7
    .??#?.?#..?????..??? 2,1,2
    ??#??##??#?#?#??#? 10,4
    .?#?#?????#??#????# 7,2,2,1,1
    ??##?.?#?#..#?# 2,4,3
    ???#???#????.??? 1,3,5,3
    ?.??#??.?????.? 5,2
    #.??.#??.? 1,2,2
    ??.#?????.#?###? 1,1,1,2,6
    #?#??#?#???#?# 3,9
    ???.?.?????????.?. 2,1
    ????.???.?#. 3,2
    ..?#??#?.??#????.? 2,2,4
    ?????#.?#..? 6,2,1
    ?.#.##???????.??? 1,1,6,2,2
    .??#?#??.#??? 5,1,1
    ?#?.??.#??.#?#?? 2,1,1,4
    #??.??#????#???..?. 2,11,1
    .??????????##?#? 2,1,2,2,2
    ????#?????#? 1,4,1
    ?#???#?.?#.???????#. 6,2,5
    ????..????. 1,4
    ???#??#.?# 1,1,2
    ???????#?.? 4,1
    ?.?????#.?.#####?? 1,1,1,1,5,1
    ??#??.??#???#.???#? 5,3,3,2
    ?#??#?.??????##? 5,5,2
    ?.???????? 4,1
    ?..????????.???#??#? 1,1,4,1,1,2
    ??#.??..??? 1,2,1
    ??#????.??#?. 4,3
    ???#??????????.#?? 10,2,1
    ??###??.#.??? 3,1,3
    ?#??###??#.? 2,4,1,1
    ##?#...?.???#???? 4,1,1,1,1
    .?#??..??.???#? 3,2,1
    ????.???????????.#?? 1,2,4,3,2
    ?.??###?.??. 5,2
    ?.#?#??.?#??#????? 1,1,1,7,1
    ?..?????#?? 2,2
    ???????..?#..?? 2,2
    ?????????##????# 8,2,1,1
    ?.????##?##???##???# 1,15
    #.?????#..?????# 1,2,2,6
    ????.#??.???.#.?? 1,3,3,1,1
    ?#??..?.??? 4,2
    #?.#?.?#?? 2,1,3
    #????#???? 6,2
    ?.###?????.?# 6,1
    ??##???#???.? 1,6,1
    .?#??????????## 4,2,1,1,2
    #???#?.??#???#???? 6,1,1,2,1,1
    ?#???????.?? 6,1,1
    .?##?????????.???? 5,1,1,4
    ????##??##???????. 2,2,4,2
    ????#???##. 2,6
    ???#?????#?. 3,3
    ????##???#?.? 1,2,1,1
    ???#?????????#?#???? 4,1,1,9
    ????????.? 1,5
    .#?.?.?.?.? 1,1,1
    ???????????????? 1,1,7
    .?.???#????#?##??? 1,14
    #.????.#??#? 1,1,1,2
    ?????.???#? 4,2
    ..???.#??????? 2,1,1,2
    ?#?????.?? 4,1,1
    #??##??.??#??#?.??? 7,1,1,2,1,1
    ?.?#?#.?.??##?#??.?? 1,3,1,7,2
    .#??????.#?? 6,1,1
    ?#?#??#????????.?. 5,2,4
    ##??##?#???#?.?#??? 9,2,2,1
    ???????#?##?##? 2,9
    ##?..???#.??#? 3,1,1,4
    ???#.?..?#?#???. 2,4,1
    ???#.?.?.##?. 1,2
    ???#.???.??# 1,1,2,2
    ????..?.??#?#??? 3,1,2,4
    ?????#??.?. 6,1
    ????????.???#?????? 3,1,1,6,3
    ???###.?.?#..#.??? 1,3,1,1,1,3
    #???#?#?#??#?.? 2,6,1,1
    ???##??.#??#?? 2,4
    ???#?##?..???. 1,6,1
    ???#??????..??#?? 1,4,3
    ????#.???. 1,1,1
    ????..??.???????## 2,1,6
    ?????#?????.??? 2,6,2
    ???.??#?##??###? 1,7,3
    ##??##.??.#.???. 6,2,1,1,1
    ???.???????????# 2,1,1,5,1
    ##?.??.???#??? 2,1,4
    ????#?.?????#?.??.?? 5,1,3,2
    .??????#???? 2,4
    #??..?#?.?.?? 3,2,1
    ?..#?????#????? 1,1,4,1
    ??????.??????#? 1,1,1,1,2
    ?.#??????##??? 3,5,2
    ???..?????###? 1,8
    ?????#?.??.???#? 1,3,1,5
    ?#?.????#??#.#???.? 3,1,4,4
    ???????????????? 3,2,1,2
    #??#???????#.???##?# 8,1,1,1,2,1
    .??..?#??? 1,3
    ..?.??#..??????# 1,2,2,1,1
    ?.#??..?#???.. 2,3
    ??.#?.#??#.?#?. 1,1,2,3
    ?????????.? 3,1
    ???###???????#??#? 1,10,4
    ???????#?.??#?..#??. 3,1,3,2
    .???.??#????..?? 2,6
    ??????##??.?#.?????? 9,1,3
    #??.?#????##?. 2,3,2
    ?.??#??#??.?????#??# 1,6,1,1,6
    ?#?#????#??????###. 2,2,2,1,3
    #???##?#?#?#?.????#. 1,10,2,1
    .??#.?.???? 1,1,2
    ??##??????????# 1,2,2,3,1
    ???????#?.?.???? 9,2
    ??##.#.?????.???? 3,1,2,1,1
    ?.?????#.? 1,1,3
    ????.????#???????# 1,1,4,3
    .????#???#?.#?##??? 8,5
    ??#??#??????????? 3,2,3,1,1
    .##?.???#. 3,2
    ?.????#?.##.?? 1,6,2,1
    ??.??#???.?? 1,4,1
    ..###?.???????#??#? 3,1,5,2
    ?#???.?#..# 3,2,1
    ??#?#?#??#???##??? 7,1,7
    .#.?..?.#????. 1,2,1
    ?????.??????#?. 4,2,3
    .?#???..???#????.. 2,4
    ??????????? 1,2,2
    ..?????..?#?. 1,2
    .?..#??.?.##?. 3,2
    ???#???#????? 3,1,2
    .#??..?#?? 1,1,1
    .#???#?#.?.??#?## 1,2,1,1,6
    ?#???.??#? 1,2,2
    ???#?#.?.?#????.?. 3,2,1
    #??????..?# 3,3,2
    ????????.?#??## 1,2,2,3,2
    ????#??#?.#?.#??. 7,2,2
    ?.##.?#????##? 2,8
    ?????.#?#??#?#???# 1,1,1,1,8
    ????.???#?#?????.?.? 3,7,2,1
    .??#?#?##?#.??## 9,3
    ??.??.?##?? 1,5
    ??##?..????? 5,1,3
    ???????#??????. 1,1,6
    .??#?.?????#? 3,4
    ?##??.?#??. 5,1,1
    ?????.???. 2,1
    ??#?.###???# 4,4,1
    ?#.?????##?#?? 1,1,1,5
    #?.??.??.#??? 1,2,1,4
    ????.#?.?.?????### 1,1,1,8
    ?#?????#??# 4,3,1
    ????##.???.? 6,2
    ?#?????????..#??. 6,1,1,1
    ??.????##?#?##. 4,4
    ????.????#???#?? 1,3,2,1
    ??###..?????# 5,3,2
    .????#.#??#???# 1,1,1,8
    ??#?.????#?.. 3,1,2
    ????##?#?#????...? 5,4,1
    ???#??#?#.##??#??##? 2,3,5,2
    ???#?##?.? 5,1
    .#.?????#??##?#? 1,2,1,3,2
    #.???.???#????? 1,1,8
    ????#.????? 1,3,2
    .??##??#?.#??.?????. 1,2,2,2,4
    ???#??????. 3,3
    ??...???.###?? 1,2,5
    ????????????#??. 3,1,1,1,1
    .???##???#?.???. 9,2
    ?.?#?#.?.?#?#????#. 1,2,1,5,1
    ???#???.????#??? 3,2,7
    ????#?#??????????? 2,2,2,1,2,1
    ????.???..?.??.??#? 1,1,2,1,2,3
    ?.???.?.?###??# 2,7
    ?#?.???.???#?# 2,1,4,1
    ?????.?##?# 2,1,5
    ??#???##??????####?# 2,12,1
    ????#?...??...?? 2,2,1,2
    ?..???????? 1,1
    .??#?.?????? 3,1
    ??#???#?.. 3,2
    ??.??.#??.??.#?# 1,2,1,3
    ??.#?.?#.#?? 1,2,1,1
    ?#?##???.#?.. 6,2
    ?????..????? 2,2
    ?????????????.?#?. 1,1,5,1,1
    ??.#.?#.??? 1,1,2
    ??.#.??#?.?????#? 1,1,4,1,4
    ?.??.?????????? 1,2,1,1,2
    ????????#?#??? 1,7,2
    .????#?##??# 2,5,1
    ??????????. 2,2
    ??#?#?.???????.?.??? 6,1,2,1,2
    ??.?.????##??#.??. 1,1,1,2,2,1
    ??????#?..?.?#?#?# 1,1,4,1,6
    ?#?###???? 5,1
    ??##????#???????#?#. 5,1,1,1,4
    ?.#???###?#??????? 10,2
    ?##?#????.??????#?. 8,3
    ?????#?.?.??.#??? 3,3,1,2,1
    .?#?.??##....?. 1,4,1
    ??..??.??? 1,2,1
    ??????.?#??? 1,2,1
    ?.??.#??.??????? 1,1,5
    ?..?#??#.??????. 1,5,5
    ??#?..???? 1,1
    ???????##? 5,3
    ??#?.??????##????. 1,1,1,1,3,1
    ?????#???##??.# 1,4,3,1,1
    ??#.??#???#??? 2,2,3,1
    ?#.?.??#??.??.??#??? 1,1,5,2,4,1
    ..???.???.#??.??? 3,1
    ??#?#?#??#??#?.#??? 8,1,2,1,2
    ??#????.?#? 2,2,3
    ?#??#?.?#??????##? 5,1,2,2
    #??#??#?.#???????? 1,4,3,1,1
    ##?#?????#?##? 2,1,1,6
    .?????????????.#? 8,2,2
    ??.?##.??##??##? 2,8
    ?#?.??#??? 1,1,1
    ##???.#?#.?#?????? 4,3,2,4
    ??#??.????????#?..? 1,3,5
    ?##??????? 2,6
    #???.#?#???#?#.???. 4,1,2,4,1,1
    ???????.??#???#?# 1,9
    ??.????##.?#????.??? 1,6,2,1,1
    ?#???###?#..#.#? 2,6,1,1
    ?????#???#? 4,2
    ???#?#..#? 2,1,1
    ?.?#.?.?#??????.?.?? 2,7
    ??#???#?#??#?? 1,7
    ??##?.??.?##.#????? 2,1,3,1,3
    #????.??#. 3,2
    ??#?#????..??##.#?. 9,3,2
    ?#?.????.???? 3,2,1,2
    ?#???##?#.?#? 1,1,4,1
    .????????.? 2,1,1
    ?????????. 1,4
    ??????##??#?## 2,6,2
    ?????##?.??.???. 2,3,2
    ??????#?#.??##?#..#? 3,4,4,1
    ?##?????????. 4,5
    ?..??#??????. 2,3
    ..????.????? 1,4
    ??.????????? 1,1,1
    .##?#???#???.#??? 6,1,1,3
    ?.???..??????? 1,1,1,5
    ?##???????.?? 4,1,2,1
    ??????#??#??#? 1,8
    ?????.???#?? 4,1,3
    ?..?#?..##? 2,2
    ?.?????#???##???.?. 2,9
    ..?????#????. 1,4,1
    #?#?#?????# 6,1
    ??#?##?.???#?? 1,4,5
    ?#?????????. 2,4
    ##???.??.????#???. 4,1,1,4
    .#????.??#?.?.#? 5,1,1,1,1
    ??##.????????.?? 2,3,2,1
    ###??#?.??#?. 3,2,1,2
    ?????#???.?#. 1,6,1
    ??.??##??.?# 1,6,1
    ??#???????#####??? 3,2,9
    #????##??.?#?.?#??. 8,2,3
    ?????????.?.??#?. 4,1
    ????.?????##? 1,1,1,6
    #?????.?#?#?#?.?.##? 6,1,1,2,1,2
    ?##????#??##??????? 8,9
    ??#??#?.#?##? 1,4,1,3
    ??#?#???.#?#???? 6,3,2
    ???..??#?..? 2,2
    ????#??#?#??#.?#.#? 1,1,3,1,1,2
    ?????????#?##??.?# 10,2
    ##????.?#?.??.? 2,2,2,2,1
    ?#????#??????.?##?? 3,3,2,2,3
    #??##?????####???? 1,4,9
    .??.#??#?.?#.###?##? 1,4,2,7
    #???#??#?#?????? 3,1,2,6
    ???#.????#.???? 3,5,1,1
    .#???#?#?.? 7,1
    .?#?????#..#?.???## 1,5,2,5
    #??#??#.???#??.?. 1,4,2,1,1
    ???#?????..#?#???#? 1,1,2,1,4,3
    .??.????##?#??? 1,7,2
    ???##???#???. 5,1,1
    ??????##???. 2,5
    .?????????#.???#?? 1,1,1,3,1,1
    ?##.??#??. 2,4
    ?????????.. 2,1,3
    #??#?????#?? 2,1,1,2
    ????.##????????# 3,3,1,1,1
    ?????..??.?#?#? 1,1,1,5
    ????.##.??? 1,2,3
    ???#?????##?? 6,5
    ??????.?.# 1,1,1
    ??.???????#???#?.??? 1,10,1
    .??##?.##??.? 4,4
    .#..???#.?#??????? 1,1,2,2,1,1
    ??#?????????## 7,2,2
    .?#.??#???? 1,3,1
    ?????#?.#.? 4,1
    ????#??.??##???#???# 1,1,8,2
    ??#??.?.#?#???#?#?? 2,1,9
    #????#??##?????? 1,1,6,1
    ?##??#?.#. 6,1
    ?#?...??#?.?? 2,3,1
    ?????.?#.???#? 2,1,2,5
    ##?#?#???.???#??#.#? 8,1,4,1
    ??#?#??????? 5,1
    ?...?..?.?? 1,1
    ???#??.??? 3,2
    .?.???.#?#???#??? 1,2,1,3,4
    #?##.???.????#. 1,2,3,1,3
    ?#????.?#???#?..?? 4,1,4,2,1
    .?#..???..??#??.??. 1,1
    ??..??.?.??###?###?# 1,1,1,1,9
    ##???.?.????#??#? 3,1,7
    ?#??#??..#?? 5,1
    ??.???#.?.##?? 3,2
    ??#???#?##.?#?##?.?? 7,2,5,2
    .???.#??????????? 2,1,5,1
    ??.??#?#????? 5,2
    #?????#?..? 1,5,1
    .?????????#?? 3,3
    .??.??????.#??#????. 2,2,1,8
    .#.?.??????? 1,1,5
    ??.?#??### 1,6
    #???#?#.???? 1,2,1,2
    ??????#??.?? 2,4,1
    ????.?#?#??.?. 1,1,5,1
    #???.??????##?????? 1,1,8,2,1
    ????????##???...?#? 2,7,1
    ????#?#??#??????#? 6,4,1,1
    #.?????.??#???? 1,1,1,3,1
    ??.??#.?.??????#??? 1,1,1,8
    ????##????????#?? 9,1,4
    .??#???.?..#??.????? 5,3,3
    .?##?????? 6,2
    #.?#???#?? 1,3,3
    ????##???? 3,2,1
    .??#????.??#?.#??# 1,1,1,4,1,1
    #??#??????.???.??? 10,1,1
    ##??????.#??#??????? 4,9
    ?#??#.????????#?? 4,9
    .#??##..?.?? 1,2,1,2
    #.?#?????????#? 1,1,7,2
    ??#?#????? 1,1,2
    .#??#???#???.#?..?? 9,1,2
    ?#?#?#?#?.????? 7,4
    .?????.????.#?.???? 4,3,1,1,1
    .?????#??? 1,4
    ?.???#?#???.# 1,7,1
    ?????#?????.???#? 7,1,2,2
    #?.#?????? 1,1,1
    ?????.?##??????##?# 3,12
    ??#????????? 5,2
    ?..??.?#?????##??? 1,1,9
    ?.#?.????#????.?# 2,1,2,2,1
    ?#?#??#??#?#??.## 3,2,5,2
    ?...??#..?#. 1,1,1,1
    #.???#?????#?#??? 1,12,1
    ?.##?.???????.#? 1,3,1,2,2
    #???#??#??# 1,5,1
    ?????.????#.?? 1,2
    ?.#??##.#?? 1,3,2
    ??#???.???????.#?? 3,1,1,2,2,3
    ????#??.???..??????? 2,1,1,1,6
    ?##??##??#? 3,3,1
    .????##??#?###?.??? 13,1,1
    ?.?#?##?#?????????#? 1,17
    ??.?#.??#?.????#??? 1,1,4,1,6
    ??.??.?.?#.?????## 2,1,1,1,1,2
    ??.###???#??..? 2,7,1,1
    ???.???###?????# 1,11
    #??.#?#?????.? 1,1,3,1
    ????#???????????.. 4,3,1,2
    ?#??#??##??.##??? 10,5
    #..???#????#?#??? 1,9
    ????????#.??? 2,5,2
    ?#.??#.?????. 1,1,1,3
    ??.?.??##???? 1,7
    .??.#?.#.?#?.??.?. 2,1,1,2,1,1
    .?????#???#??.?##?? 1,1,1,1,1,4
    ????#?.?#.?. 5,1
    #..?#?###???? 1,8,1
    ?#?#?????.#..???? 3,3,1,1,1
    .?????##??? 3,3,1
    ????##??????# 2,9
    ?#.?#?????? 1,3,2
    ?#???#.#??#?.#???? 1,3,1,3,5
    ?.??##???????? 4,4
    ?..???????#??.???? 1,1,2,3,3
    ?####??.?#?. 5,1
    .???#.??.??.?????? 2,1,1,2,2,1
    .???.?.?#????#?? 1,1,1,1,3
    ?#???#??.#.?.. 5,1
    #???.#?#.#??#??#.?? 4,1,1,5,1,1
""".trimIndent()