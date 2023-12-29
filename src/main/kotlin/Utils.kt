fun <T> List<List<T>>.transposed(): List<List<T>> =
    if (this.all { it.size == this.first().size }) {
        first().indices.map { index -> map { row -> row[index] } }
    } else {
        error("Malformed matrix")
    }

fun <T> List<List<T>>.draw() {
    indices.forEach { row -> println(this[row].joinToString("") { it.toString() }) }
}

fun String.toTwoDimensionalMatrix(): List<List<String>> =
    lines().map { line -> line.map { it.toString() } }