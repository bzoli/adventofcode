package day_6_1

fun main(args: Array<String>) {

// tesz inputok (day1 es day2)
//    val listOfTimeDistancePairs = listOf(
//        Pair(7.0, 9),
//        Pair(15.0, 40),
//        Pair(30.0, 200),
//    )
//    val listOfTimeDistancePairs = listOf(
//        Pair(71530.0, 940200),
//    )

// eles inputok (day1 es day2) U.az a kod csinalja mindket napot
//    val listOfTimeDistancePairs = listOf(
//        Pair(38.0, 234),
//        Pair(67.0, 1027),
//        Pair(76.0, 1157),
//        Pair(73.0, 1236)
//    )
    val listOfTimeDistancePairs = listOf(
        Pair(38677673.0, 234102711571236),
    )

    var product = 1
    listOfTimeDistancePairs.forEach {
        val time = it.first
        val distance = it.second
        var ta1p = (-time + kotlin.math.sqrt(time * time - 4 * distance)) / -2
        val ta1 = if (ta1p == ta1p.toInt().toDouble()) ta1p + 1 else kotlin.math.ceil(ta1p)
        val ta2p = (-time - kotlin.math.sqrt(time * time - 4 * distance)) / -2
        val ta2 = if (ta2p == ta2p.toInt().toDouble()) ta2p - 1 else kotlin.math.floor(ta2p)
        val winnings = ta2 - ta1 + 1
        product = (product * winnings).toInt()
    }
    println(product)
}
/*

s = v * tm
v = ta
t = ta + tm = 7
tm = 7 - ta
s = ta * (7 - ta)
s = -ta^2 + 7 * ta  (gyokor: 0 es 7)
sd = -2 * ta + 7  (s derivaltja)
ta_smax: (mely ta erteknel van f(s) maximum?)
0 = -2 * ta_smax + 7
ta_smax = 3.5
smax = -3.5^2 + 7 * 3.5 = 12.25  (-ta^2 + 7 * ta)


9 = -ta^2 + 7 * ta
0 =  -ta^2 + 7 * ta - 9
(-7+-sqr(49-36) ) / -2  (gyokok: 1,697224362 es 5,302775638)


Time:      7  15   30
Distance:  9  40  200

Time:        38     67     76     73
Distance:   234   1027   1157   1236

 */