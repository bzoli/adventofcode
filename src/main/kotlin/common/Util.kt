package common


fun <T> timing(block: () -> T) {
    val start = System.currentTimeMillis()
    try {
        block.invoke()
    } finally {
        println("It took ${System.currentTimeMillis() - start} ms.")
    }
}
