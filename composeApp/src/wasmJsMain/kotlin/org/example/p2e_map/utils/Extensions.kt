package org.example.p2e_map.utils

fun Double.formatted() = toString().let {
    when {
        it.length < 4 -> it
        else -> it.substring(0, 4)
    }
}