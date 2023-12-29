fun <T> List<List<T>>.transposed(): List<List<T>> =
    if (this.all { it.size == this.first().size }) {
        first().indices.map { index -> map { row -> row[index] } }
    } else {
        error("Malformed matrix")
    }