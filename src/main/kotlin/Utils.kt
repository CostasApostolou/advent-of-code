fun <T> List<List<T>>.transposed(): List<List<T>> =
    if (this.all { it.size == this.first().size }) {
        first().indices.map { index -> map { row -> row[index] } }
    } else {
        error("Malformed matrix")
    }

fun <T> List<List<T>>.draw(): List<List<T>> {
    indices.forEach { row -> println(this[row].joinToString("") { it.toString() }) }
    return this
}

fun String.toTwoDimensionalMatrix(): List<List<String>> =
    lines().map { line -> line.map { it.toString() } }

fun <T> List<T>.findIndicesOf(elem: T): List<Int> =
    mapIndexed { index, t -> if (t == elem) index else null }.filterNotNull()
