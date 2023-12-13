package day_12_1


fun main() {
    val inputStrings = listOf(".###.##.#...")

    //                      ^\\.*#{3}\\.+#{2}\\.+#{1}\\.*$
    // val pattern = Regex("^\\.*#{4}\\.+#{2}\\.+#{5}\\.*$")
    val pattern = Regex("^\\.*#{3}\\.+#{2}\\.+#{1}\\.*$")
    for (input in inputStrings) {
        val matchResult = pattern.find(input)
        if (matchResult != null) {
            println("Match: $input")
        } else {
            println("No match: $input")
        }
    }
}