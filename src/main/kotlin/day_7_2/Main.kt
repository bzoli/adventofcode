package day_7_2

import kotlin.math.pow

//fun main1() {
//    while (true) {
//        println("Enter the first number:")
//        val firstHand: Hand = Hand(readLine()!!.uppercase(), 1)
//
//        println("Enter the second number:")
//        val secondHand: Hand = Hand(readLine()!!.uppercase(), 1)
//
//        val card1Larger = firstHand.getHandValue() > secondHand.getHandValue()
//        var compareChar = if (card1Larger) ">" else "<"
//        println("${firstHand.handStr} $compareChar ${secondHand.handStr}")
//    }
//}

fun main() {
    var handStrs = hands.lines()
    var hands = mutableListOf<Hand>()
    handStrs.forEach { handStr ->
        val handStrparts = handStr.split(" ")
        hands.add(Hand(handStrparts[0], handStrparts[1].toInt()))
    }
    val handsSorted = hands.sorted()
    var summa = 0
    for ((index, hand) in handsSorted.withIndex()) {
        summa += hand.bid * (index + 1)
    }
    println(summa)
}


class Hand(val handStr: String, val bid: Int) : Comparable<Hand> {
    val value: Long
    var type: Type = Type.HIGH_CARD

    enum class Type : Comparable<Type> {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND;
        // FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD;
        fun compareToo(other: Type): Int {
            return ordinal.compareTo(other.ordinal)
        }
    }
    companion object {
        val placeValues = mapOf( 'A' to 13, 'K' to 12, 'Q' to 11, 'T' to 10, '9' to 9, '8' to 8, '7' to 7, '6' to 6, '5' to 5, '4' to 4, '3' to 3, '2' to 2, 'J' to 1,)
        val placeValue = 13.0
    }

    init {
        value = getHandValue()
        if (!handStr.contains("J")) {
            type = getType(handStr)
        } else {
            var handStrCandidate = handStr
            type = placeValues.keys.map {
                getType(handStrCandidate.replace('J', it))
            }.max()
        }
    }

    fun getHandValue(): Long {
        var value = 0L
        for ((index, c) in handStr.withIndex()) {
            value += placeValues[c]!! * placeValue.pow(4 - index).toLong()
        }
        return value
    }

    fun getType(handStr: String): Type {
        val sameCards = mutableMapOf<Char, Int>()
        for (c in handStr) {
            if (sameCards.containsKey(c)) {
                sameCards[c] = sameCards[c]!! + 1
            } else {
                sameCards[c] = 1
            }
        }
        return when {
            isFiveOfAKind(sameCards) -> Type.FIVE_OF_A_KIND
            isFourOfAKind(sameCards) -> Type.FOUR_OF_A_KIND
            isFullHouse(sameCards) -> Type.FULL_HOUSE
            isThreeOfAKind(sameCards) -> Type.THREE_OF_A_KIND
            isTwoPair(sameCards) -> Type.TWO_PAIR
            isOnePair(sameCards) -> Type.ONE_PAIR
            else -> Type.HIGH_CARD
        }
    }

    fun isFiveOfAKind(sameCards: Map<Char, Int>) = sameCards.size == 1
    fun isFourOfAKind(sameCards: Map<Char, Int>): Boolean {
        if (sameCards.size != 2) {
            return false
        }
        return sameCards.keys.filter {
            sameCards[it] == 4
        }.any()
    }

    fun isFullHouse(sameCards: Map<Char, Int>): Boolean {
        if (sameCards.size != 2) {
            return false
        }
        return sameCards.keys.filter {
            sameCards[it] == 3
        }.any()
    }

    fun isThreeOfAKind(sameCards: Map<Char, Int>): Boolean {
        if (sameCards.size != 3) {
            return false
        }
        return sameCards.keys.filter {
            sameCards[it] == 3
        }.any()
    }

    fun isTwoPair(sameCards: Map<Char, Int>): Boolean {
        if (sameCards.size != 3) {
            return false
        }
        return !sameCards.keys.filter {
            sameCards[it] == 3
        }.any()
    }

    fun isOnePair(sameCards: Map<Char, Int>) = sameCards.size == 4
    override fun compareTo(other: Hand): Int {
        val typeCompared = type.compareTo(other.type)
        if (typeCompared == 0) {
            return value.compareTo(other.value)
        }
        return typeCompared
    }
}

/*
AAAAA Five of a kind, where all five cards have the same label: AAAAA
AA8AA Four of a kind, where four cards have the same label and one card has a different label: AA8AA
23332 Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
TTT98 Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
23432 Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
A23A4 One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
23456 High card, where all cards' labels are distinct: 23456
*/

// AKQJT: 13+12+11+10+9 = 55 a legnagyobb semmi High card erteke
// A1234 > KAQJT legyen:
// helyi ertek:
// A,  K,  Q,  J,  T, 9, 8, 7, 6, 5, 4, 3, 2
// 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
// 52  36  22  10
// A2345 = 13^4*13 + 13^3*1 + 13^2*2 + 13^1*5 + 13^0*4 = 373897
// KAQJT = 13^4*12 + 13^3*13 + 13^2*11 + 13^1*10 + 13^0*9 = 373291
//a*13 > b*12 + c*13 + d*11 + e*10 + f*9
//A2 > KA  :  13*13 + 2 > 13*12+13  171 > 169
// | 28561 2197 169 13 1
//AKQJT = 13×28561+12×2197+11×169+10×13+9=399655
//22345 > AKQJT
//One pair          + 13^5
//Two pair          + 13^6
//Three of a kind   + 13^7
//Full house        + 13^8
//Four of a kind    + 13^9
//Five of a kind    + 13^10


var hands1 = """32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483"""

var hands = """J3T3T 868
6Q499 630
8J3AA 335
A73AA 593
28445 591
A9QAA 563
47353 830
AQ7A9 230
QT6JJ 588
J6K38 769
5JA65 531
737QJ 827
QQ56J 962
5T666 481
4486Q 282
J6566 843
3TT88 331
KKK8K 129
8Q884 145
T5T74 118
32J22 115
9Q843 220
99J9J 590
3528K 291
95789 959
2986K 26
9T66T 989
632A7 447
74285 217
5275J 629
78337 767
56555 894
5Q555 262
AA4AA 320
4K499 701
JAT22 759
Q852J 423
JJ7JT 22
TJ223 698
T555J 795
5K9K5 549
Q2822 515
5KQ9Q 754
T8T79 979
6JJ72 866
77989 707
8AAAJ 224
KKA2K 869
26J95 572
K93K9 149
44Q5Q 867
69999 778
Q33JQ 506
5KAA6 765
333J3 540
K8JKK 134
9QAQA 169
JQ629 233
64667 735
5A6Q2 43
9KJTJ 994
KQAT7 124
9TTAA 8
55755 193
6J627 742
T59A9 139
78AT9 864
28A82 718
33JJ3 57
58776 385
22AA2 904
7TT77 21
A8AA8 490
T9784 706
K3J3K 76
99444 451
8K497 680
5Q45Q 344
T5843 670
QK647 161
6KQ46 634
2JQQ2 471
KJJ77 123
KQ3Q7 412
522QT 632
52625 642
38A25 508
54A7K 892
44439 958
2JKAA 682
K75K7 677
28A24 743
AA5Q8 722
92892 783
K8828 173
K9A89 425
K6KQ8 657
7K747 833
QJ996 625
35QK4 67
66677 268
QJ89A 610
75A6J 49
J8872 967
4K658 210
29449 832
A3694 566
99J5A 313
J5AA9 1000
57A7A 917
287J4 681
667T7 789
3J322 375
55525 522
K55AJ 565
J9T7T 740
J4353 363
88244 941
JAA5A 977
Q5Q55 922
9TK8T 491
J8533 378
24388 61
8848T 947
KTJ2K 264
76786 744
K6666 739
5K5K5 631
56284 183
T23T2 48
79727 158
AA6KA 92
5A688 140
T7TK7 143
699J6 377
7JJ38 809
759T3 181
8KK9K 732
999KK 901
222J8 329
A3J43 835
AQQ66 266
96Q4J 368
JA5K6 838
69J88 263
AK3KA 108
TAJAT 554
7QAQA 420
3KQ62 819
QJ465 79
JJJJJ 504
TT4TT 663
224J2 801
6AAAA 299
J77T7 902
48444 619
A2785 607
74J8T 875
8J683 250
K2QTK 824
26AAA 215
47Q8A 553
A6TT9 639
266T6 17
QT9JA 24
J8J99 405
Q6666 544
Q44J4 327
66694 684
5TJQA 511
2J25A 757
9666J 103
99995 346
365KA 734
K388T 11
57T5T 828
29992 273
4A36Q 34
39888 204
22A33 306
9Q993 302
72677 435
KKQ8J 94
447J4 289
Q4K22 107
333K9 406
TT5T7 270
KK2J4 101
4K5KK 903
TTT7T 473
6KAKK 141
8Q868 943
T4T45 660
4T968 840
6AA5A 163
TQJTT 812
J8A88 345
88QQJ 276
7JT93 290
58QQ5 534
T7457 849
4444K 479
AQQAT 851
J45JJ 750
J8T98 255
87758 839
QQ8QQ 55
725A2 693
K4TT3 431
88588 482
2K4QJ 502
89955 381
T7869 987
JA74J 408
27438 211
2658A 221
7A7AA 836
77QJT 873
666KK 633
8AAAA 756
QJQQQ 281
937QQ 184
3Q777 308
KK7JQ 208
44355 478
A5JK4 571
TTJ35 726
97KTJ 357
444A4 294
84438 83
66KQ2 517
7866J 575
224T7 434
TT633 42
66972 671
867Q4 106
95J55 775
8J444 168
TT328 213
86A88 198
Q3578 986
QQQ2Q 831
QQ8Q8 898
K69A4 749
AAKAA 475
T3487 275
59K6Q 850
76J4T 248
69Q7Q 97
AK3QJ 182
Q2598 44
54444 486
TJJTT 708
J2JJK 179
KKK88 287
66QQ6 694
2537A 551
2KQTA 501
J9877 171
2456T 355
44Q99 929
999TK 637
6QTQ7 442
7J877 755
22292 574
3QK79 157
JK9K9 300
585K4 85
QK22A 41
A8934 580
T5TK9 920
K4Q5J 122
33257 470
J5JTK 203
9KKKQ 712
677Q3 776
36A36 792
7AAJ8 219
52T55 690
6695J 334
66T68 484
976Q4 667
Q44TQ 105
98J67 753
QJT87 58
86Q92 802
4A4AA 185
63399 390
K6K22 954
QQQ77 815
77797 546
4JK48 997
33A96 53
7A4A4 601
568T4 992
886J8 784
5T575 418
45TQK 367
76A9K 370
56226 449
6KK3K 458
69955 636
K9Q93 249
TAT53 746
KJJ3K 137
84477 332
J5AQ6 325
387AA 382
8833J 142
56Q6A 909
33393 459
K3KK9 398
996AJ 160
37778 499
TTT44 933
AQTQQ 737
3T3T4 596
Q8JQ3 552
55JJ5 897
95995 518
JQQQ4 500
4QQQT 773
A2522 336
AK972 785
7KK3J 587
73K77 448
7Q755 309
9966K 397
TKQJA 228
46666 609
44JJ4 853
TA5AT 19
354Q6 131
99K97 258
J3355 664
K29T6 720
T8635 132
75474 521
6J943 119
77K77 238
84Q47 15
A556T 347
TT7J7 582
2455T 661
JT43T 351
22A22 243
38335 655
9A47Q 133
92T29 887
Q98JJ 394
KKKKQ 781
KK5JK 231
4TJJ4 285
TTJKK 863
3K5TQ 165
J7T32 882
5535T 557
7537A 952
692T6 88
52J55 547
5TTKT 659
68K59 648
77J47 799
TJ348 116
Q838A 304
T255T 46
37682 600
764T6 700
JK939 301
37J77 536
T69T8 35
77Q76 387
378T5 279
7K4K3 259
T8993 568
88J88 328
88988 665
7J8Q5 50
QJ95Q 365
86K6K 207
9K536 485
7Q749 417
8983T 150
QQ2Q6 319
AA2K2 796
J35T5 714
KK99K 925
TK244 199
3QAJ3 823
9T979 315
962KJ 33
Q45QQ 842
Q663Q 350
42334 28
97779 603
78T68 890
6J3A3 855
5555T 908
95J8K 489
A87TT 537
22K23 626
4J848 641
22A26 758
934QT 614
47474 679
7227J 467
5AAAA 606
99J79 341
AKA33 821
6826J 583
55J55 415
K7AT6 724
3TTT8 433
3Q343 807
J8552 439
777KK 232
44678 135
33J3T 969
A2545 516
J596T 686
7KTQQ 703
TKTKK 426
22J44 559
83K33 73
29999 343
K739J 996
55A5A 687
43334 885
T6T6T 396
QQ33Q 923
6A66A 798
2AAAA 844
Q4223 374
8QK45 419
244Q2 683
55545 624
J9999 770
TT242 298
AK9KA 581
22A97 668
5TK87 861
KK4AQ 229
4A66Q 803
KJKJK 794
QQ34Q 272
J4494 545
55K6K 295
33733 594
Q5QQ5 620
96269 78
94773 710
4939Q 246
29959 303
86888 364
T482K 474
94844 407
3QQQQ 685
2AT92 709
8Q888 921
JA53J 63
AK857 893
88TJK 2
JQ555 359
77377 966
TKKKK 859
8T487 982
KA4K4 284
9433J 62
52A4K 5
K6KAJ 197
K4TAJ 252
QTQ8T 530
736Q2 968
TTQQQ 3
65888 762
82828 975
KAK6T 512
TTTJT 487
QQ9QQ 60
34J46 533
AAJA2 871
76K5A 468
952A9 400
8J887 623
J4K45 427
666J6 312
2JK2K 791
J3336 597
37377 56
KKAAK 562
66QQT 95
TA66Q 914
52225 187
76KJJ 189
AQT69 156
A99A9 509
JQAAA 316
33T33 212
22J2J 983
46466 881
QQQQT 314
4949J 373
KAJ2K 87
55252 352
67777 645
Q5T35 376
42444 891
Q29Q9 226
T43KQ 305
9999A 441
AAT5A 453
97K6Q 110
74979 422
777AK 293
TJTJK 476
Q7JTT 870
23222 436
AK5Q9 321
4QQKQ 90
AQ9A9 942
K2QT6 429
Q9Q62 788
74499 186
A52T3 205
652AA 702
J488A 900
64624 584
8QK3K 223
4JQ25 804
TTQKT 810
62946 760
42422 322
A3533 895
77J97 790
55K55 154
5AT22 519
26772 592
76866 813
56AJA 573
KQKQQ 880
7TAQ8 649
Q2229 166
6ATJ6 837
44784 644
6664J 120
T9K6T 274
JKJJK 342
2TK35 577
76666 403
5J8Q3 239
777J7 52
9QQQJ 995
56286 457
6J777 999
26662 349
22QAJ 452
838Q8 808
34339 886
79TQ4 366
27782 148
63868 764
55574 622
K2T9J 939
34225 652
9TTTT 971
4464T 13
J259T 260
979AA 981
75KKT 727
22K49 692
9TTJ9 940
AT75T 498
KK3QK 883
Q8Q4A 970
896K8 155
J9595 704
25J52 817
Q347A 288
K9999 51
6K9K9 98
K49J7 697
A32AJ 410
QQ2QJ 466
363QQ 23
53J3A 725
32343 829
777Q2 69
3K3J3 605
AAAJA 699
7757T 117
Q44K4 280
7577K 147
46649 526
KK66K 541
88Q7K 409
66626 472
54QK4 527
A25TT 12
T6QTT 404
TT7KK 550
22282 391
J5455 37
3T3T3 816
775A5 111
36T98 241
88J58 430
5QTQ7 353
99899 386
JAA88 227
87887 713
J25J7 848
55588 38
6KK8A 389
5AA54 555
KKKQ4 911
699T7 766
5JQTJ 330
QTK98 780
55JJA 752
6279T 857
957T4 621
55AAA 379
J59Q6 29
878J7 818
47AQT 348
KQ48T 955
9T49T 494
A7A42 535
QQQQ4 180
66963 777
6J366 730
37J59 520
8TT88 945
QA4TT 561
473J7 542
4354K 825
46QJ6 437
T8885 669
83K76 751
Q5853 948
64844 36
QJ4J4 77
A7777 372
323J3 946
45387 944
66769 202
93339 497
85Q79 14
J3AJ2 413
98JA8 311
QQQJ3 326
J3QAQ 463
44Q4Q 151
8AA98 96
TATTA 965
7J37A 421
K38Q9 635
J72TK 59
8A85A 164
39999 854
59572 109
57TJ5 74
83888 862
77A2J 763
JJ455 771
TKJA2 834
5QJ85 495
J4444 932
2TT3Q 856
98KT3 438
9TKK7 627
566JA 638
559Q9 628
J6565 245
8JKTA 711
99QQ9 247
34QT4 162
684KK 731
5A57A 254
T9755 297
9A96K 658
34484 240
QJ66J 450
666K4 570
A5AQK 878
JQ662 905
A6622 822
J3Q39 177
85555 858
7T53Q 178
8KA6T 931
29K99 104
TTTQ4 960
AJ555 514
TJT3T 214
KA88J 912
JJJJ8 102
3633A 748
7J447 705
74444 45
2QAQ2 125
TJA7T 721
T6QJK 324
7AAAA 617
4AQAJ 585
7JJ77 576
A32TQ 747
76677 618
AJ5Q9 70
89627 360
QJ5T7 602
A8T36 896
5AA36 411
6255J 841
44644 612
3TJTK 66
4JT4T 640
JT4T8 146
8733J 973
K6944 256
5K5KK 936
KTT6K 388
3JQ35 793
94994 296
55553 930
777Q7 646
A2AAQ 399
555A6 32
22323 75
62262 957
8745T 54
828J8 974
KKKTQ 383
6J6Q9 261
22522 27
QK666 716
78KK5 242
T2Q22 728
J9KKQ 424
389A6 738
98989 292
8883K 68
4344J 578
2Q665 899
58895 938
63363 1
44584 172
JJT83 206
88884 695
J9K8J 579
72532 414
Q9T8A 877
433K3 318
K37Q4 253
9999Q 675
J3QQ5 884
TTT54 4
3K3TT 209
4KJ44 401
5J279 225
222J2 333
67J88 543
662TQ 175
Q696Q 745
T46T6 113
75557 361
A48A4 130
22Q2Q 10
42292 558
QKQQQ 523
66JQA 188
T676T 65
7A5T6 510
426Q9 72
2Q222 934
757TQ 888
899TT 673
59KQ4 723
J666A 927
QT7Q7 286
J886Q 774
JA2QQ 599
K448K 93
QKKQK 689
66556 480
QQ5AA 951
T7T87 462
4784J 216
JT4T6 988
553QK 654
77277 337
A9Q66 317
82J82 64
J7595 935
8A2Q6 81
J2427 152
J22KQ 589
77222 787
7868K 826
75A3J 916
9A292 950
6JT44 237
T7TJ6 846
2JKJA 564
KT844 676
Q4384 538
Q6A68 114
Q5868 505
665TK 820
55544 874
AJKAK 30
82A34 915
Q583Q 251
97TJA 496
45744 608
QQ6QQ 234
QQQ9K 271
3A99A 465
37734 444
5566T 507
KKJKK 20
2J673 492
5J744 416
93K64 369
KKKK7 998
33232 719
A3A33 91
Q53J7 267
J5556 972
44Q8Q 616
T444J 548
TJ3TJ 586
92856 963
K98T5 432
J6J6J 772
4J39Q 643
34535 39
7K7Q7 953
T42J4 805
2442A 47
954A8 395
K4466 991
AQAAA 174
J39KA 532
7K2Q3 672
25577 428
QT659 715
J6698 910
35355 84
8J895 218
J5674 6
K7537 567
8Q634 380
6828J 811
7TJ8A 82
477Q7 674
7748A 244
88288 257
K9933 889
T626T 269
5T42Q 443
2KA9Q 167
K4KKK 16
J2757 159
Q5TQ6 845
44888 560
K65AT 191
6K68Q 176
66JJ8 978
6K53J 615
AAJ22 525
J3J3J 99
98898 906
48JA2 454
86J36 879
66866 524
3AA3A 872
68Q46 733
38JQ3 338
65J75 128
57J77 455
AKA8K 138
22J72 200
9T7A6 310
47777 25
A3685 503
T5A68 100
979J8 528
9JA22 782
KJKKQ 913
64942 907
QQ954 598
5KKKK 876
5T5TT 779
3992T 678
5QQQ9 265
3592K 924
5554A 493
T5698 170
7496J 488
TT474 192
JK95A 80
AAJJJ 40
43644 307
J57K5 650
J8K82 961
8J998 688
T9984 9
33J35 89
J2AT9 985
6239J 662
88JJ8 595
JK852 604
66347 741
Q6T69 278
55665 696
7942J 736
35AK4 358
7755J 384
58A84 613
5TTTT 919
7A9T7 7
K33QK 354
AQQAA 235
39TJT 196
26229 976
6664Q 865
99QQQ 236
374A6 956
8QTT8 964
A76AQ 112
T8668 611
66JJ6 729
886Q6 814
KK44J 993
62AJ8 127
ATJ64 402
7K594 483
96496 990
7424J 761
878K3 653
486K3 980
JQA44 852
2TJ6J 339
44T44 446
73KTK 121
T7787 513
K67QT 717
77287 136
7QQJ8 18
4342T 768
5KK5J 847
Q2Q7T 666
6QJT2 529
7T7A7 392
J6676 445
7733K 647
Q676A 926
QJQJQ 806
QQQJ8 928
48692 691
JAA55 984
J2423 800
32JQ8 362
T2744 937
7J9TQ 277
7K326 86
3K9TJ 860
77JKT 71
46644 31
23J2Q 323
J3K96 393
87QKK 456
TQ5K2 569
AAK9T 949
56932 539
Q5QQQ 356
3Q4A2 126
T53J2 283
8732Q 461
KTTKT 556
Q8J3K 918
JAAA7 340
255KQ 469
8A9AJ 201
Q8965 144
777J2 477
A8AJQ 797
3QQQ9 786
6Q7T4 460
QQQ82 656
4K329 222
A9979 190
6T664 440
4A434 153
Q7JQ2 194
7993J 651
8822K 195
7QQ77 464
52295 371"""