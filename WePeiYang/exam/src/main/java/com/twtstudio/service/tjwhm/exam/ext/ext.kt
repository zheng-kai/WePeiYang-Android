package com.twtstudio.service.tjwhm.exam.ext

/**
 * Created by tjwhm@TWTStudio at 11:29 AM,2018/8/5.
 * Happy coding!
 */

fun String.toLessonType(): String {
    return when (this) {
        "3" -> "网课"
        "2" -> "党课"
        "1" -> "形势与政策"
        else -> "其他"
    }
}

fun String.toProblemType(): String {
    return when (this) {
        "0" -> "单选"
        "1" -> "多选"
        "2" -> "判断"
        else -> "其他"
    }
}

fun Int.toProblemType(): String {
    return when (this) {
        0 -> "单选"
        1 -> "多选"
        2 -> "判断"
        else -> "其他"
    }
}

fun Int.toSelectionIndex(): String {
    return when (this) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        3 -> "D"
        4 -> "E"
        5 -> "F"
        else -> "W"
    }
}

fun List<Int>.toSelectionIndex(): String {
    var result = ""
    repeat(this.size) {
        result += when (this[it]) {
            0 -> "A"
            1 -> "B"
            2 -> "C"
            3 -> "D"
            4 -> "E"
            5 -> "F"
            else -> "W"
        }
    }
    return result
}

fun String.multiSelectionIndexToInt(): List<Int> {
    val result = mutableListOf<Int>()
    for (i in 0 until length) {
        result.add(this[i].selectionIndexToInt())
    }
    return result
}

fun Int.toSelectionIndexForTrueFalse(): String {
    return when (this) {
        0 -> "T"
        1 -> "F"
        else -> "W"
    }
}

fun String.selectionIndexToInt(): Int {
    return when (this) {
        "A", "T" -> 0
        "B", "F" -> 1
        "C" -> 2
        "D" -> 3
        "E" -> 4
        else -> 6
    }
}

fun Char.selectionIndexToInt(): Int {
    return when (this) {
        'A', 'T' -> 0
        'B', 'F' -> 1
        'C' -> 2
        'D' -> 3
        'E' -> 4
        else -> 6
    }
}

fun Int.toMode(): String {
    return when (this) {
        0 -> "顺序练习"
        1 -> "模拟测试"
        else -> "其他"
    }
}