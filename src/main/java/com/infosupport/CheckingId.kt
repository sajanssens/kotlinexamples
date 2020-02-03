package com.infosupport

fun isValidIdentifier(s: String): Boolean = (s.isNotEmpty() && s[0] !in '0'..'9' && !s.contains('$'))


fun main(args: Array<String>) {
    println(isValidIdentifier("name"))   // true
    println(isValidIdentifier("_name"))  // true
    println(isValidIdentifier("_12"))    // true
    println(isValidIdentifier(""))       // false
    println(isValidIdentifier("012"))    // false
    println(isValidIdentifier("no$"))    // false
}