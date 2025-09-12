package org.example.p2e_map.utils

@JsFun("new Intl.Collator('pl').compare")
external fun comparePolish(a: String, b: String): Int

fun <T> comparatorPl(by: (T) -> String): Comparator<T> = Comparator { a, b -> comparePolish(by(a), by(b)) }